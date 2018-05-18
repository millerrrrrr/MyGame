package com.zhu.mygame.bean;

public class ScoreBean {

    private int id;
    private int score;
    private String spendTime;
    private String gameTime;

    public ScoreBean() {
    }

    public ScoreBean(int score, String spendTime, String gameTime) {
        this.score = score;
        this.spendTime = spendTime;
        this.gameTime = gameTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    @Override
    public String toString() {
        return "ScoreBean{" +
                "id=" + id +
                ", score=" + score +
                ", spendTime='" + spendTime + '\'' +
                ", gameTime='" + gameTime + '\'' +
                '}';
    }
}
