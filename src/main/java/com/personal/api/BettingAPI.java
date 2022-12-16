package com.personal.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.personal.model.Game;

public class BettingAPI {
    final String url = "http://localhost:8080/games";
    public List<Game> games;

    public BettingAPI() {
        games = new ArrayList<>();
    }

    public void addGame(Game game) {
        Game newGame = new Game(game.getWinner(), game.getLoser(), game.getLeague(), game.getOdds(), game.getPercentage(), game.getDate());
        games.add(newGame);
    }

    public void post() throws IOException, InterruptedException {
        for (Game game: games) {
            Map<String, Object> params = new HashMap<>();
            params.put("winner", game.getWinner());
            params.put("loser", game.getLoser());
            params.put("league", game.getLeague());
            params.put("odds", game.getOdds());
            params.put("percentage", game.getPercentage());
            params.put("date", game.getDate());
    
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(params);
    
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
    
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
    
            System.out.println("Added game to database: " + game.getWinner() + " over " + game.getLoser());
        }
    }
}
