package io.spatio;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class SpatioDatabase {

    private static final String DB_URL = "jdbc:sqlite:spatio.db";
    private static SpatioDatabase INSTANCE;
    private final Connection connection;
    public SpatioDatabase() {


        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            Path dir = Paths.get(System.getProperty("user.home"), ".spatio");
            Files.createDirectories(dir);

            Path dbFile = dir.resolve("spatio.db");
            String url = "jdbc:sqlite:" + dbFile.toAbsolutePath();

            this.connection = DriverManager.getConnection(url);
            this.connection.setAutoCommit(true);
            Statement stmt = connection.createStatement();
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS certificates (
                    hash TEXT PRIMARY KEY,
                    latitude REAL,
                    longitude REAL,
                    timestamp TEXT,
                    signature TEXT
                );
            """);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'initialisation de la base SQLite", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static synchronized SpatioDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SpatioDatabase();
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }
    public void insertCertificate(SpatioCertificate cert) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT OR REPLACE INTO certificates (hash, latitude, longitude, timestamp, signature) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cert.hash());
            stmt.setDouble(2, cert.latitude());
            stmt.setDouble(3, cert.longitude());
            stmt.setString(4, cert.timestamp().toString());
            stmt.setString(5, cert.signature());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion certificat", e);
        }
    }
    public int getCount() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT COUNT(*) AS nb FROM certificates";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("nb");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            return 0;
        }
    }
        public SpatioCertificate getCertificateByHash(String hash) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM certificates WHERE hash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, hash);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                String timestamp = rs.getString("timestamp");
                String signature = rs.getString("signature");
                return new SpatioCertificate(lat, lon, java.time.Instant.parse(timestamp), hash, signature);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lecture certificat", e);
        }
    }
}