package com.personal.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.personal.model.Game;

public class BettingAPI {
    final String url = "http://localhost:8080/games";
    final String getPastNoResultsUrl = url + "/pastNoResult";
    final int HOUR_MS = 1000*60*60;
    public List<Game> games;

    public BettingAPI() {
        // Initialize games list
        games = new ArrayList<>();
    }

    public void addGame(Game game) {
        Game newGame = new Game(game);
        games.add(newGame);
    }

    private String getRequestBody(Game game) {
        // Generate JSON object with correct parameters

        Map<String, Object> params = new HashMap<>();
        params.put("winner", game.getWinner());
        params.put("loser", game.getLoser());
        params.put("league", game.getLeague());
        params.put("odds", game.getOdds());
        params.put("percentage", game.getPercentage());
        params.put("date", game.getDate());
        params.put("result", game.getResult());
        params.put("id", game.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public List<Game> getPastGamesWithNoResult() throws IOException, InterruptedException {
        // Returns a list of past games without the result filled in in the database

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getPastNoResultsUrl))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        
        // send request, get response
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // Convert response to List of Games
        ObjectMapper mapper = new ObjectMapper();
        String responseBody = response.body();
        return Arrays.asList(mapper.readValue(responseBody, Game[].class));
    }

    public void post() throws IOException, InterruptedException {
        // Posts each game in the games list to database

        for (Game game: games) {
            // Build request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(getRequestBody(game)))
                    .build();
    
            // Send request
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
    
            System.out.println("Added game to database: " + game);
        }
    }

    public void update() throws IOException, InterruptedException {
        // Updates each game in the games list

        for (Game game: games) {
            // Build request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(getRequestBody(game)))
                    .build();
    
            // Send request
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
    
            System.out.println("Updated game result in database: " + game.getWinner() + " over " + game.getLoser() + ": " + game.getResult());
        }
    }
}
