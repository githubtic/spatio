package io.spatio;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

public final class HybridCrypto {

    private static final int GCM_TAG_BITS = 128;
    private static final int IV_BYTES = 12;

    private HybridCrypto() {}

    public static String encrypt(byte[] plaintext, PublicKey publicKey) throws Exception {
        // 1) Génère une clé AES
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        SecretKey aesKey = kg.generateKey();

        // 2) AES-GCM
        byte[] iv = new byte[IV_BYTES];
        SecureRandom.getInstanceStrong().nextBytes(iv);

        Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");
        aes.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_BITS, iv));
        byte[] ciphertext = aes.doFinal(plaintext);

        // 3) Chiffre la clé AES avec RSA-OAEP
        Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        rsa.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encAesKey = rsa.doFinal(aesKey.getEncoded());

        // 4) Base64 URL-safe
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        return "v1."
                + enc.encodeToString(encAesKey) + "."
                + enc.encodeToString(iv) + "."
                + enc.encodeToString(ciphertext);
    }

    public static byte[] decrypt(String payload, PrivateKey privateKey) throws Exception {
        String[] parts = payload.split("\\.");
        if (parts.length != 4 || !"v1".equals(parts[0])) {
            throw new IllegalArgumentException("Payload invalide");
        }

        Base64.Decoder dec = Base64.getUrlDecoder();
        byte[] encAesKey = dec.decode(parts[1]);
        byte[] iv = dec.decode(parts[2]);
        byte[] ciphertext = dec.decode(parts[3]);

        // 1) Déchiffre la clé AES avec la clé privée RSA
        Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        rsa.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsa.doFinal(encAesKey);

        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");

        // 2) Déchiffre le message
        Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");
        aes.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_BITS, iv));
        return aes.doFinal(ciphertext);
    }
}