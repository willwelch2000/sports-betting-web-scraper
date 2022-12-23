package com.personal.model;

import java.util.Date;

public class Game {
    private Integer id;
    private String winner;
    private String loser;
    private String league;
    private int odds;
    private double percentage;
    private Date date;
    private String result;

    public Game() {
        this("");
    }

    public Game(String league) {
        this("", "", league, 0, 0, new Date());
    }

    public Game(Game game) {
        this(game.id, game.winner, game.loser, game.league, game.odds, game.percentage, game.date, game.result);
    }

    public Game(String winner, String loser, String league, int odds, double percentage, Date date) {
        this(winner, loser, league, odds, percentage, date, "U");
    }

    public Game(String winner, String loser, String league, int odds, double percentage, Date date, String result) {
        this(null, winner, loser, league, odds, percentage, date, result);
    }

    public Game(Integer id, String winner, String loser, String league, int odds, double percentage, Date date, String result) {
        this.id = id;
        this.winner = winner;
        this.loser = loser;
        this.league = league;
        this.odds = odds;
        this.percentage = percentage;
        this.date = date;
        this.result = result;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public int getOdds() {
        return odds;
    }

    public void setOdds(int odds) {
        this.odds = odds;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String toString() {
        String toReturn = "Game: " + this.winner;
        if (this.odds != 0)
            toReturn += " (" + this.odds + ")";
        toReturn += " over " + this.loser;
        if (this.percentage != 0)
            toReturn += "   " + 100*this.percentage + "%";
        return toReturn;
    }
}
