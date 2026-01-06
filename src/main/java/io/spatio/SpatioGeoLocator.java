package io.spatio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class SpatioGeoLocator {

    public static double[] getCurrentLocation() {
        try {
            URL url = new URL("http://ip-api.com/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JSONObject json = new JSONObject(response.toString());
            double latitude = json.getDouble("lat");
            double longitude = json.getDouble("lon");

            return new double[]{latitude, longitude};

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la géolocalisation IP", e);
        }
    }
}