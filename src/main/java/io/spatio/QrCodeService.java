package io.spatio;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Date;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdOutputStream;
import com.github.luben.zstd.ZstdInputStream;
import com.google.zxing.common.HybridBinarizer;

public class QrCodeService {

    public static BufferedImage generate(String text, int width, int height) {
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(text, BarcodeFormat.QR_CODE, width, height);

            return MatrixToImageWriter.toBufferedImage(matrix);

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération QR Code", e);
        }
    }
    public static BufferedImage generateCertificatQrCode() throws Exception {
        BufferedImage qrCodeImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        SpatioIO spatio = new SpatioIO();
        SpatioCertificate certificat = spatio.generateFromIP();
        qrCodeImage= generate(certificat.getHash(), qrCodeImage.getWidth(), qrCodeImage.getHeight());
        // sauvegarder dans la base
        SpatioDatabase db = new SpatioDatabase();
        db.insertCertificate(certificat);
        // fin de sauvegarde
        ImageUtil.saveAsPng100x100(qrCodeImage, "spatio-"+ certificat.getHash().substring(0,6)+".png");
        return qrCodeImage;
    }
    public static String encodeToQr(Object obj) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // 1. JSON
        byte[] json = mapper.writeValueAsBytes(obj);

        // 2. Compression Zstd
        byte[] compressed = Zstd.compress(json);

        // 3. Base64 URL-safe
        return "SP1." + Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(compressed);
    }
    public static <T> T decodeFromQr(String qr, Class<T> type) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String payload = qr.substring(4); // enlever SP1.
        byte[] compressed = Base64.getUrlDecoder().decode(payload);

        byte[] json = com.github.luben.zstd.Zstd.decompress(compressed, 10_000);
        return mapper.readValue(json, type);
    }
    public static String decode(BufferedImage qrImage) {
        try {
            LuminanceSource source =
                    new BufferedImageLuminanceSource(qrImage);

            BinaryBitmap bitmap =
                    new BinaryBitmap(new HybridBinarizer(source));

            Result result =
                    new MultiFormatReader().decode(bitmap);

            return result.getText(); // ✅ QR content

        } catch (NotFoundException e) {
            throw new IllegalArgumentException(
                    "No QR code found in image", e
            );
        }
    }



}