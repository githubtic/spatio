package io.spatio;
import io.spatio.SpatioIO;
import io.spatio.SpatioCertificate;

public class MainActivity {
    public static void main(String[] args) throws Exception {
        // Crée une instance de la librairie
        SpatioIO spatio = new SpatioIO();

        // Génére une signature spatio-temporelle basée sur l'IP publique
        SpatioCertificate cert = spatio.generateFromIP();

        // Affiche le certificat
        cert.printCertificate();

        // Vérifie la signature
        boolean valid = spatio.verify(cert);
        System.out.println("✔ Signature valide : " + valid);
    }
}