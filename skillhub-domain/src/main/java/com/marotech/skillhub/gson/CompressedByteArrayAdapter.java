package com.marotech.skillhub.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedByteArrayAdapter extends TypeAdapter<byte[]> {

    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(value);
             GZIPInputStream gzip = new GZIPInputStream(bis)) {
            byte[] decompressed = gzip.readAllBytes();
            out.value(new String(decompressed, StandardCharsets.UTF_8));
        } catch (Exception e) {
            // Handle exception, possibly log and continue or throw a runtime exception
            throw new IOException("Failed to decompress data", e);
        }
    }

    @Override
    public byte[] read(JsonReader in) throws IOException {
        String decompressedString = in.nextString();
        if (decompressedString == null) {
            return null;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(decompressedString.getBytes(StandardCharsets.UTF_8));
        }
        return bos.toByteArray();
    }
}