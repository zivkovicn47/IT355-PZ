package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getWeather(@RequestParam(value = "city", defaultValue = "Nis") String city) {
        Map<String, Object> weatherInfo = weatherService.getWeather(city);
        return ResponseEntity.ok(weatherInfo);
    }
}
