package io.spatio;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import static io.spatio.Utils.sanitizeHash;

public class SpatioSigner {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public SpatioSigner() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

//    public String sign(String hash) throws Exception {
//        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initSign(privateKey);
//        signature.update(hash.getBytes());
//        byte[] digitalSignature = signature.sign();
//        return Base64.getEncoder().encodeToString(digitalSignature);
//    }
//
//    public boolean verify(String hash, String signatureStr) throws Exception {
//        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initVerify(publicKey);
//        signature.update(hash.getBytes());
//        byte[] digitalSignature = Base64.getDecoder().decode(signatureStr);
//        return signature.verify(digitalSignature);
//    }
public String sign(String hash) throws Exception {
    String clean = sanitizeHash(hash);

    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initSign(privateKey);

    signature.update(clean.getBytes(StandardCharsets.UTF_8)); // ✅ charset explicite

    byte[] digitalSignature = signature.sign();

    return Base64.getUrlEncoder()       // ✅ URL-safe
            .withoutPadding()
            .encodeToString(digitalSignature);
    }
    public boolean verify(String hash, String signatureStr) throws Exception {
        String clean = sanitizeHash(hash);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);

        signature.update(clean.getBytes(StandardCharsets.UTF_8)); // ✅ EXACTEMENT pareil

        byte[] digitalSignature =
                Base64.getUrlDecoder().decode(signatureStr);

        return signature.verify(digitalSignature);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}