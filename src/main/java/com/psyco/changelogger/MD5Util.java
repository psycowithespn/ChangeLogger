package com.psyco.changelogger;

import java.io.*;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getMD5(File file) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            is = new DigestInputStream(is, digest);
            while (is.read() != -1) {}

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        byte[] md = digest.digest();
        BigInteger bigInt = new BigInteger(1, md);
        String hash = bigInt.toString(16);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (hash.length() + i < 32) {
            builder.append(0);
            i++;
        }
        builder.append(hash);
        return builder.toString();
    }
}
