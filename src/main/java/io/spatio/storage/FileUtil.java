package io.spatio.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static void writeBytesToFile2(byte[] data, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, data);
        } catch (IOException e) {
            throw new RuntimeException("Erreur écriture fichier", e);
        }
    }

    public static void writeBytesToFile(byte[] data, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}