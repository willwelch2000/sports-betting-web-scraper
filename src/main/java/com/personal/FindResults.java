package com.personal;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.personal.api.BettingAPI;
import com.personal.model.Game;

public class FindResults {
    static WebDriver driver;
    static List<Game> pastGamesWithNoResult;
    static BettingAPI bettingAPI = new BettingAPI();

    private static void startDriver() {
        System.setProperty("webdriver.chrome.driver", FindGames.driverAddress);
        driver = new ChromeDriver();
    }

    private static void updateResults() {
        for (Game game : pastGamesWithNoResult) {
            bettingAPI.addGame(game);
        }

        try {
            bettingAPI.update();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {

        // Start driver, get games to update
        startDriver();
        try {
            pastGamesWithNoResult = bettingAPI.getPastGamesWithNoResult();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String address = "";
        for (Game game : pastGamesWithNoResult) {
            // Get right ESPN web address for the current game
            String newAddress = FindGames.getESPNAddress(game.getLeague(), game.getDate());
            // Only navigate to a new page if it's a different address than previous
            if (!newAddress.equals(address)) {
                address = newAddress;
                driver.get(address);
            }

            // Get results, add result to game object
            List<WebElement> winnerBoxes = driver.findElements(By.cssSelector("li.ScoreboardScoreCell__Item--winner div.ScoreCell__TeamName--shortDisplayName"));
            List<WebElement> loserBoxes = driver.findElements(By.cssSelector("li.ScoreboardScoreCell__Item--loser div.ScoreCell__TeamName--shortDisplayName"));
            Boolean winner = winnerBoxes.parallelStream().anyMatch(winnerBox -> FindGames.alterTeamName(winnerBox.getText()).equals(FindGames.alterTeamName(game.getWinner())));
            Boolean loser = loserBoxes.parallelStream().anyMatch(loserBox -> FindGames.alterTeamName(loserBox.getText()).equals(FindGames.alterTeamName(game.getWinner())));
            game.setResult(((winner && loser) || (!winner && !loser))? "U" : (winner? "Y" : "N"));
        }

        driver.close();
        updateResults();
    }
}
