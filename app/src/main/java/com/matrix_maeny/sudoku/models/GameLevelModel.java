package com.matrix_maeny.sudoku.models;

public class GameLevelModel {
    String levelName;
    int levelState;

    public GameLevelModel(String levelName, int levelState) {
        this.levelName = levelName;
        this.levelState = levelState;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevelState() {
        return levelState;
    }

    public void setLevelState(int levelState) {
        this.levelState = levelState;
    }
}
