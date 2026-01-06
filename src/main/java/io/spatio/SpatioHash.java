package io.spatio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

public class SpatioHash {

    public static String compute(double latitude, double longitude, Instant timestamp) {
        String data = String.format("LAT:%.6f|LON:%.6f|TIME:%s", latitude, longitude, timestamp.toString());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de hash SHA-256", e);
        }
    }
}