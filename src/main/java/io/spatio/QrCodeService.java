package io.spatio;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Date;

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

}