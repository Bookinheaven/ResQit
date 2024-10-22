package org.burnknuckle.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jxmapviewer.viewer.GeoPosition;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class LocationService {
    private static final String API_KEY = "92806c63bec341498f7a068149a9f404";
    private static final String API_URL = "https://api.opencagedata.com/geocode/v1/json";

    public static GeoPosition getCoordinates(String location) {
        String url = API_URL + "?q=" + location + "&key=" + API_KEY + "&no_annotations=1";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                return parseCoordinates(jsonResponse);
            }
        } catch (Exception e) {
            logger.error(" %s ".formatted(getStackTraceAsString(e)));
            return null;
        }
    }

    private static GeoPosition parseCoordinates(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray results = jsonObject.getJSONArray("results");

        if (results.length() > 0) {
            double lat = results.getJSONObject(0).getJSONObject("geometry").getDouble("lat");
            double lng = results.getJSONObject(0).getJSONObject("geometry").getDouble("lng");
            return new GeoPosition(lat, lng);
        } else {
            System.out.println("Location not found.");
            return null;
        }
    }
}

