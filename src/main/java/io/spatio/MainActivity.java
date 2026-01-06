package io.spatio;
import io.spatio.SpatioIO;
import io.spatio.SpatioCertificate;

import java.awt.image.BufferedImage;

public class MainActivity {
    public static void main(String[] args) throws Exception {
        // Crée une instance de la librairie
        SpatioIO spatio = new SpatioIO();

        // Génére une signature spatio-temporelle basée sur l'IP publique
        SpatioCertificate cert = spatio.generateFromIP();

        // Affiche le certificat
        cert.printCertificate();

        /**
         *
         * BufferedImage qrCode = QrCodeService.generate(
         *          *         "FACTURE:F2024-001|CLIENT:Ouédraogo Issa|MONTANT:150000",
         *          *         300,
         *          *         300
         * );
         *
         * ImageUtil.saveAsPng100x100(qrCode, "qr_facture.png");
         *
         *
         * */
        // Vérifie la signature
        boolean valid = spatio.verify(cert);
        System.out.println("✔ Signature valide : " + valid);

        // sauvegarder dans la base
        SpatioDatabase db = new SpatioDatabase();
        db.insertCertificate(cert);
        // fin de sauvegarde
        // afficher à partir de la base de données
        String searchCertKey="y0SJOu0NiLZyUD88S92VBGlVC5JsYXsR5JSm00srQ8M";
        SpatioCertificate found = db.getCertificateByHash(searchCertKey);
        if (found != null) {
            System.out.println(searchCertKey + "  trouvé (found from local database) : " + found.getHash());
            found.printCertificate();
        }
        else
            System.out.println(searchCertKey + " non trouvé (not found from local database)");

        //Test QrGeneratorService
        BufferedImage qrCode = QrCodeService.generate(
                "FACTURE:F2024-001|CLIENT:Kabore Steve|MONTANT:1500000",
                300,
                 300);
        ImageUtil.saveAsPng100x100(qrCode, "qr_facture2.png");
        BufferedImage qrCodeCertificationImage = QrCodeService.generate(cert.getHash().substring(0,6)+"|(Long:"+cert.getLongitude()+"Lat:"+cert.getLatitude()+")|"+cert.getTimestamp(),100,100);
        ImageUtil.saveAsPng100x100(qrCode, "spatio-"+cert.getHash().substring(0,6)+".png");


        //


    }
}