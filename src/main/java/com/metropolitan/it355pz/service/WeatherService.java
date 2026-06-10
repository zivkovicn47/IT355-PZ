package com.metropolitan.it355pz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> getWeather(String city) {
        Map<String, Object> result = new HashMap<>();
        result.put("city", city);

        if ("TVOJ_API_KLJUC_OVDE".equals(apiKey) || apiKey == null || apiKey.trim().isEmpty()) {
            result.put("temp", null);
            result.put("description", "Missing or invalid API Key");
            result.put("error", true);
            return result;
        }

        try {
            String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                // Parse temperature
                Map<String, Object> main = (Map<String, Object>) response.get("main");
                if (main != null && main.get("temp") != null) {
                    result.put("temp", main.get("temp"));
                }

                // Parse description
                List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
                if (weatherList != null && !weatherList.isEmpty()) {
                    Map<String, Object> weatherObj = weatherList.get(0);
                    if (weatherObj.get("description") != null) {
                        result.put("description", weatherObj.get("description"));
                    }
                }
                result.put("error", false);
            } else {
                result.put("temp", null);
                result.put("description", "Empty response from weather API");
                result.put("error", true);
            }
        } catch (Exception e) {
            result.put("temp", null);
            result.put("description", "API call failed: " + e.getMessage());
            result.put("error", true);
        }
        return result;
    }
}
