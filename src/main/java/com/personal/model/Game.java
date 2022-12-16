package com.personal.model;

import java.util.Date;

public class Game {
    private String winner;
    private String loser;
    private String league;
    private int odds;
    private double percentage;
    private Date date;

    public Game(String league) {
        this("", "", league, 0, 0, new Date());
    }

    public Game(Game game) {
        this(game.winner, game.loser, game.league, game.odds, game.percentage, game.date);
    }

    public Game(String winner, String loser, String league, int odds, double percentage, Date date) {
        this.winner = winner;
        this.loser = loser;
        this.league = league;
        this.odds = odds;
        this.percentage = percentage;
        this.date = date;
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


    public String toString() {
        return "Game: " + this.winner + " (" + this.odds + ")" + " over " + this.loser + "   " + 100*this.percentage + "%";
    }
}
