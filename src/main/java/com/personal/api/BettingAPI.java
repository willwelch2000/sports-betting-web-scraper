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
    final String getNoResultsUrl = url + "/pastNoResult";
    final int HOUR_MS = 1000*60*60;
    public List<Game> games;

    public BettingAPI() {
        games = new ArrayList<>();
    }

    public void addGame(Game game) {
        Game newGame = new Game(game);
        games.add(newGame);
    }

    private String getRequestBody(Game game) {
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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getNoResultsUrl))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        String responseBody = response.body();
        return Arrays.asList(mapper.readValue(responseBody, Game[].class));
    }

    public void post() throws IOException, InterruptedException {
        for (Game game: games) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(getRequestBody(game)))
                    .build();
    
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
    
            System.out.println("Added game to database: " + game);
        }
    }

    public void update() throws IOException, InterruptedException {
        for (Game game: games) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(getRequestBody(game)))
                    .build();
    
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
    
            System.out.println("Updated game result in database: " + game.getWinner() + " over " + game.getLoser() + ": " + game.getResult());
        }
    }
}
