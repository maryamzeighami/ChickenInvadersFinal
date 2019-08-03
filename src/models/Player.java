package models;

import java.io.Serializable;

public class Player{
    private Game currentGame = null;
    public String name;
    public int score;
    public int coin;
    public int numberOfBombs;
    public int heartNum;
    public SpaceShip spaceShip;

    public Player(String name) {
        this.name = name;
        numberOfBombs=3;
        heartNum=5;

    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public void setNumberOfBombs(int numberOfBombs) {
        this.numberOfBombs = numberOfBombs;
    }
}
