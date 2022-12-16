package com.personal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.personal.api.BettingAPI;
import com.personal.model.Game;

public class App {
    static final String driverAddress = "C:\\Users\\willw\\Developer\\chromedriver.exe";
    static WebDriver driverESPN;
    static WebDriver driverFanduel;
    static String league;
    static Boolean debug = true;
    static List<Game> combinedGames;

    static String getESPNAddress(String league) {
        return "https://www.espn.com/" + league + "/scoreboard";
    }

    static String getDraftKingsAddress(String league) {
        Map<String, String> leagueToSport = new HashMap<>();
        leagueToSport.put("mlb", "baseball");
        leagueToSport.put("nba", "basketball");
        leagueToSport.put("nfl", "football");
        String sport = leagueToSport.get(league);
        return "https://sportsbook.draftkings.com/leagues/" + sport + '/' + league;
    }

    static String getFanduelAddress(String league) {
        return "https://sportsbook.fanduel.com/navigation/" + league;
    }

    public static void inputLeague() {
        // Request league from user
        // System.out.print("Select a league: ");
        //Scanner input = new Scanner(System.in);
        // String league = input.next();
        //input.close();
        league = "nba";
    }
    
    public static void initializeDrivers() {
        // Initialize two drivers--one for ESPN, another for DraftKings
        System.setProperty("webdriver.chrome.driver", driverAddress);
        driverESPN = new ChromeDriver();
        driverFanduel = new ChromeDriver();

        // Start the drivers
        driverESPN.get(getESPNAddress(league));
        driverFanduel.get(getFanduelAddress(league));
    }

    public static List<Game> getGamesESPN() {
        // Find games for today and their percentages
        List<Game> games = new ArrayList<>();
        List<WebElement> gameCastButtons = driverESPN.findElements(By.cssSelector("div.Scoreboard__Callouts a"));
        int gameCount = gameCastButtons.size();
        for (int i = 0; i < gameCount; i++) {
            Game newGame1 = new Game(league);
            Game newGame2 = new Game(league);
            List<WebElement> gameCastButtonsInLoop = driverESPN.findElements(By.cssSelector("div.Scoreboard__Callouts a"));
            gameCastButtonsInLoop.get(i).click();

            // Set winners and losers of both games
            List<String> teamNames = getTeamNamesESPN();
            newGame1.setWinner(teamNames.get(0));
            newGame1.setLoser(teamNames.get(1));
            newGame2.setWinner(teamNames.get(1));
            newGame2.setLoser(teamNames.get(0));

            // Find percentages
            List<Double> percentages = getPercentagesESPN();
            newGame1.setPercentage(percentages.get(0));
            newGame2.setPercentage(percentages.get(1));

            games.add(newGame1);
            games.add(newGame2);

            // Print
            if (debug) {
                System.out.println(newGame1);
                System.out.println(newGame2);
            }

            driverESPN.navigate().back();
        }

        return games;
    }

    public static List<String> getTeamNamesESPN() {
        // Returns list of home/away team names in that order

        // List<WebElement> longNameBoxes = driverESPN.findElements(By.cssSelector("span.long-name"));
        // List<WebElement> shortNameBoxes = driverESPN.findElements(By.cssSelector("span.short-name"));
        // String awayName = (longNameBoxes.get(0).getText() + ' ' + shortNameBoxes.get(0).getText()).replaceFirst("^ ", "");
        // String homeName = (longNameBoxes.get(1).getText() + ' ' + shortNameBoxes.get(1).getText()).replaceFirst("^ ", "");
        // List<String> teamNames = new ArrayList<>();
        // teamNames.add(homeName);
        // teamNames.add(awayName);
        // return teamNames;

        List<WebElement> nameBoxes = driverESPN.findElements(By.cssSelector("div.Gamestrip__Competitors div.ScoreCell__TeamName"));
        List<String> names = new ArrayList<>();
        for (WebElement box : nameBoxes) {
            names.add(box.getText());
        }
        return names;
    }

    public static List<Double> getPercentagesESPN() {
        // Returns percentages for home/away teams in that order
        List<Double> percentages = new ArrayList<>();
        
        String percentageAway = driverESPN.findElement(By.cssSelector("div.matchupPredictor__teamValue--b")).getText();
        String percentageHome = driverESPN.findElement(By.cssSelector("div.matchupPredictor__teamValue--a")).getText();
        
        percentages.add(Double.parseDouble(percentageHome.replaceAll("%", ""))/100);
        percentages.add(Double.parseDouble(percentageAway.replaceAll("%", ""))/100);
        return percentages;
    }

    public static void closeDrivers() {
        driverESPN.close();
        driverFanduel.close();
    }

    public static void postBet() {
        BettingAPI bettingAPI = new BettingAPI();

        try {
            bettingAPI.post();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static List<Game> getGamesDraftKings() {
        // Find games for today and their odds
        List<Game> games = new ArrayList<>();
        //List<WebElement> = driverDraftKings.findElements(null);
        return games;
    }

    public static List<Game> getGamesFanduel() {
        List<Game> games = new ArrayList<>();
        List<WebElement> timeBoxes = driverFanduel.findElements(By.cssSelector("time"));

        // Count games
        int gameCount = 0;
        for (WebElement timeBox : timeBoxes) {
            // Text is either something like "7:10 CT" or "Fri 7:10 CT"
            // Check that first char is numeric
            if (Character.isDigit(timeBox.getText().charAt(0)))
                gameCount++;
        }

        List<String> teamNames = getTeamNamesFanduel();
        List<Integer> odds = getOddsFanduel();
        
        for (int i = 0; i < gameCount; i++) {
            Game newGame1 = new Game(league);
            Game newGame2 = new Game(league);

            String awayTeam = teamNames.get(2*i);
            String homeTeam = teamNames.get(2*i+1);
            Integer awayOdds = odds.get(2*i);
            Integer homeOdds = odds.get(2*i+1);

            newGame1.setWinner(homeTeam);
            newGame1.setLoser(awayTeam);
            newGame1.setOdds(homeOdds);
            newGame2.setWinner(awayTeam);
            newGame2.setLoser(homeTeam);
            newGame2.setOdds(awayOdds);

            games.add(newGame1);
            games.add(newGame2);

            // Print
            if (debug) {
                System.out.println(newGame1);
                System.out.println(newGame2);
            }
        }

        return games;
    }

    public static List<String> getTeamNamesFanduel() {
        List<WebElement> nameBoxes = driverFanduel.findElements(By.cssSelector("span.ae.aj.ix.iy.iz.ja.if.ig.ih.il.jb.s.ff.ec.jc.h.i.j.al.l.m.am.o.an.q.ao.br"));
        List<String> teamNames = new ArrayList<>();
        for (WebElement nameBox : nameBoxes) {
            teamNames.add(nameBox.getText().substring(nameBox.getText().indexOf(" ") + 1));
        }

        return teamNames;
    }

    public static List<Integer> getOddsFanduel() {
        List<WebElement> oddsBoxes = driverFanduel.findElements(By.cssSelector("div.af.ag.ba.az.ct.cu.aj.cv.hy.cx.s.fg.ed.gj.jl.bk.y.h.i.j.al.l.m.am.o.an.q.ao span"));
        List<Integer> odds = new ArrayList<>();
        for (WebElement oddsBox : oddsBoxes) {
            odds.add(Integer.parseInt(oddsBox.getText().replace("+", "")));
        }
        return odds;
    }

    public static List<Game> mergeGames(List<Game> percentageGames, List<Game> oddsGames) {
        List<Game> combinedGames = new ArrayList<>();
        for (Game percentageGame : percentageGames) {
            Game combinedGame = new Game(percentageGame);
            for (Game oddsGame : oddsGames) {
                if (oddsGame.getWinner().equals(percentageGame.getWinner())) {
                    combinedGame.setOdds(oddsGame.getOdds());
                    break;
                }
            }
            combinedGames.add(combinedGame);
        }

        return combinedGames;
    }

    public static void analyze() {
        // Prints out games that should be bet on, according to the strategy refernced by "method"
        int method = 0;

        for (Game game : combinedGames) {
            // Do calculations
            Integer odds = game.getOdds();
            Double percentage = game.getPercentage();

            Double expectedEarnings;
            if (odds > 0) {
                expectedEarnings = odds*percentage/100;
            } else {
                expectedEarnings = -percentage*100/odds;
            }
            Double expectedLoss = 1-percentage;
            Double expectedTotal = expectedEarnings - expectedLoss;

            // Check stats
            Boolean shouldBuy = false;
            if (method == 0) {
                Double expectedTotalMin = 0.2;
                int oddsMin = -120;
                int oddsMax = 120;
                Double percentageMin = 0.6;
                Double percentageMax = 0.4;
                shouldBuy = expectedTotal > expectedTotalMin && ((odds >= oddsMin && odds <= oddsMax) || (percentage >= percentageMin && percentage <= percentageMax));
            }

            if (shouldBuy)
                System.out.println(game);
        }
    }

    public static void main( String[] args ) {
        inputLeague();
        initializeDrivers();
        List<Game> gamesESPN = getGamesESPN();
        List<Game> gamesFanduel = getGamesFanduel();
        closeDrivers();
        combinedGames = mergeGames(gamesESPN, gamesFanduel);
        analyze();
    }
}
