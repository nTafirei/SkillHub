package com.marotech.skillhub.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionUtil {


    public static byte[] compressToBytes(String data) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data.getBytes("UTF-8"));
        }
        return byteStream.toByteArray();
    }

    public static String decompressToString(byte[] compressedData) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new IOException("Error decompressing GZIP data", e);
        }
    }
}
