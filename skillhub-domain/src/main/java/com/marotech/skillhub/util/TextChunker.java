package com.marotech.skillhub.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextChunker {
    public static class SplitOptions {
        private Integer minLength;
        private Integer maxLength;
        private Integer overlap;
        private String splitter;
        private String delimiters;

        public SplitOptions() {
            this.minLength = 0;
            this.maxLength = 1000;
            this.overlap = 0;
            this.splitter = "paragraph";
            this.delimiters = "";
        }

        public SplitOptions(Integer minLength, Integer maxLength, Integer overlap,
                            String splitter, String delimiters) {
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.overlap = overlap;
            this.splitter = splitter;
            this.delimiters = delimiters;
        }
    }

    public static String[] splitChunk(String[] currChunks, int maxLength, int overlap) {
        StringBuilder chunkString = new StringBuilder();
        for (String chunk : currChunks) {
            chunkString.append(chunk).append(" ");
        }
        String chunkStr = chunkString.toString().trim();

        String subChunk = chunkStr.substring(0, Math.min(maxLength, chunkStr.length()));
        String restChunk = chunkStr.substring(Math.min(maxLength, chunkStr.length()));

        int blankPosition = restChunk.indexOf(" ");
        if (blankPosition == -1) {
            blankPosition = restChunk.indexOf("\n");
        }

        if (blankPosition != -1) {
            subChunk += restChunk.substring(0, blankPosition);
            restChunk = restChunk.substring(blankPosition).trim();
        }

        String overlapText = "";

        if (overlap > 0) {
            blankPosition = subChunk.lastIndexOf(" ", subChunk.length() - overlap);
            if (blankPosition == -1) {
                blankPosition = subChunk.lastIndexOf("\n", subChunk.length() - overlap);
            }

            if (blankPosition != -1) {
                overlapText = subChunk.substring(blankPosition).trim();
            }
        }

        return new String[]{subChunk, restChunk, overlapText};
    }

    public String[] chunkText(String text, SplitOptions options) {
        if (options == null) {
            options = new SplitOptions();
        }

        String regex = options.delimiters;

        if (regex.isEmpty()) {
            if (options.splitter.equals("sentence")) {
                regex = "([.!?\\n])\\s*";
            } else {
                regex = "\\n{2,}";
            }
        }

        String[] baseChunk = text.split(regex);

        List<String> chunks = new ArrayList<>();
        List<String> currChunks = new ArrayList<>();
        int currChunkLength = 0;

        for (int i = 0; i < baseChunk.length; i++) {
            String subChunk = baseChunk[i];

            currChunks.add(subChunk);
            currChunkLength += subChunk.length();

            if (currChunkLength >= options.minLength) {
                String[] result = splitChunk(currChunks.toArray(new String[0]), options.maxLength, options.overlap);

                chunks.add(result[0]);

                currChunks.clear();
                currChunkLength = result[2].length() + result[1].length();

                if (!result[2].isEmpty()) {
                    currChunks.add(result[2]);
                }
                if (!result[1].isEmpty()) {
                    currChunks.add(result[1]);
                }
            }
        }

        if (!currChunks.isEmpty()) {
            String[] result = splitChunk(currChunks.toArray(new String[0]), options.maxLength, options.overlap);

            if (!result[0].isEmpty()) {
                chunks.add(result[0]);
            }
            if (!result[1].isEmpty()) {
                chunks.add(result[1]);
            }
        }

        return chunks.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String text = "Offering this service gives your clients more financing choices and drives more traffic to you. Everyone wins!";
        SplitOptions options = new SplitOptions(20, 40, 5, "paragraph", " ");
        TextChunker chunker = new TextChunker();
        String[] chunks = chunker.chunkText(text, options);
        List<String> list = Arrays.asList(chunks);
        for (String chunk : list) {
            System.out.println(chunk);
        }
    }
}