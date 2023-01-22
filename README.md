# Sports Betting Web Scraper
## Sports Betting App overall
This application is one part of a larger project, an app that
1. Uses Selenium to scrape the web for predictions and betting odds for athletic events.
2. Interfaces with a MySQL database to store and access this data. 
3. Perform analysis on the data to attempt to find successful sports-betting strategies.

## Purpose of this part of the project
This is the web scraper portion of the project. It achieves the following objectives:
* Gathering game data: FindGames.java
    * Access ESPN's website for daily NBA games and find the predicted percentages that each team has of winning.
    * Access Fanduel's website to find the corresponding betting odds for each of these games. 
    * Post this data to a local server via http.
* Updating previously found games with result: FindResults.java
    * Access ESPN's website to find the result of games in the database but with unknown result.
    * Update the database using the found result.

## Usage: FindGames.java
### When to use
In order to properly function, this program should be run once per day before any NBA games have happened. The web scraper will run into issues if there are any NBA games currently happening or that have already been completed.  
Additionally, if you want the data to be saved, the corresponding http client (called sports-betting-api) should be running.
### How to use
The program asks the user to select from a list of supported leagues; for now, the only option is the NBA.  
Next, it asks if the data should be saved via the http client. Simply answer 'y' or 'n'.


## Usage: FindResults.java
Use at any time. No user input necessary. 