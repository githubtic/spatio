package io.spatio;

public class Utils {
    public static String sanitizeHash(String hash) {
        if (hash == null) return null;

        return hash
                .replace("\uFEFF", "")   // BOM
                .replace("\uFFFD", "")   // �
                .replaceAll("\\s+", "")  // espaces, \n, \r, \t
                .trim();
    }
}