package io.spatio;

import java.time.Instant;

public class SpatioCertificate {
    private final double latitude;
    private final double longitude;
    private final Instant timestamp;
    private final String hash;
    private final String signature;

    public SpatioCertificate(double latitude, double longitude, Instant timestamp, String hash, String signature) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.hash = hash;
        this.signature = signature;
    }

    public double latitude() { return latitude; }
    public double longitude() { return longitude; }
    public Instant timestamp() { return timestamp; }
    public String hash() { return hash; }
    public String signature() { return signature; }

    public void printCertificate() {
        System.out.println("📄 Spatio-Certificat");
        System.out.println("Date        : " + timestamp);
        System.out.println("Latitude    : " + latitude);
        System.out.println("Longitude   : " + longitude);
        System.out.println("Hash        : " + hash);
        System.out.println("Signature   : " + signature);
    }
}