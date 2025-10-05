package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Enter city name: ");
        String city = scanner.nextLine();


        String apiKey = "4a6ebd0c98e80eb2dcd7cfafd5b90393";


        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                     + encodedCity + "&appid=" + apiKey + "&units=metric";


        HttpClient client = HttpClient.newHttpClient();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();


            if (jsonResponse.contains("\"cod\":404")) {
                System.out.println("Error: City not found");
                return;
            }


            String cityName = extractValue(jsonResponse, "\"name\":\"([^\"]+)\"");
            String temp = extractValue(jsonResponse, "\"temp\":([0-9.-]+)");
            String humidity = extractValue(jsonResponse, "\"humidity\":([0-9]+)");
            String description = extractValue(jsonResponse, "\"description\":\"([^\"]+)\"");


            System.out.println("\n===== Weather Report =====");
            System.out.println("City       : " + cityName);
            System.out.println("Temperature: " + temp + " °C");
            System.out.println("Humidity   : " + humidity + "%");
            System.out.println("Condition  : " + description);
            System.out.println("==========================");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String extractValue(String json, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : "N/A";
    }
}
