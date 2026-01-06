package io.spatio;

import java.time.Instant;

public class SpatioIO {

    private final SpatioSigner signer;

    public SpatioIO() throws Exception {
        this.signer = new SpatioSigner();
    }

    public SpatioCertificate generateFromIP() {
        double[] coords = SpatioGeoLocator.getCurrentLocation();
        return generateFromCoordinates(coords[0], coords[1]);
    }

    public SpatioCertificate generateFromCoordinates(double latitude, double longitude) {
        Instant now = Instant.now();
        String hash = SpatioHash.compute(latitude, longitude, now);
        try {
            String signature = signer.sign(hash);
            // sauvegegarder vers le serveur sécurisé de certification
            return new SpatioCertificate(latitude, longitude, now, hash, signature);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la signature", e);
        }
    }

    public boolean verify(SpatioCertificate cert) {
        try {
            return signer.verify(cert.hash(), cert.signature());
        } catch (Exception e) {
            return false;
        }
    }

    public SpatioSigner getSigner() {
        return signer;
    }
}