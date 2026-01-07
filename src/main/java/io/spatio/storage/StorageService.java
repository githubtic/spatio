package io.spatio.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class StorageService {

    private final Path root;

    public StorageService(String rootPath) {
        this.root = Paths.get(rootPath);
        System.out.println("Stockage interne: "+root);
    }
    public static String filify(String name) {
        return new Date().toString().strip().replaceAll("\\\\", "/").replaceAll(" ","-").replaceAll("/","-").replaceAll(":","-").replace("\"","") +"-excelis-"+name;
    }
    public Path save(byte[] data, String folder, String filename) {
        try {
            Path dir = root.resolve(folder);
            Files.createDirectories(dir);

            Path file = dir.resolve(filename);
            System.out.println("Stockage Path: "+file);

            Files.write(file, data);

            return file;

        } catch (Exception e) {
            throw new RuntimeException("Erreur stockage fichier", e);
        }
    }

    public byte[] read(String folder, String filename) {
        try {
            Path file = root.resolve(folder).resolve(filename);
            return Files.readAllBytes(file);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture fichier", e);
        }
    }
}