package me.rezabayat.pigdice.service.playinggame;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserGameInfo {
    private long currentScore;
    private long score;
    private boolean hold;
    private List<Long> currentDices;
    private long currentThrow;
    private boolean isWin;

    public UserGameInfo(boolean hold) {
        this.hold = hold;
        this.currentDices = new ArrayList<>();
        this.score = 0;
        this.currentScore = 0;
        this.currentThrow = 0;
        this.isWin = false;
    }

    public void addCurrentScore(long score) {
        this.currentScore += score;
    }

    public void calculateScore() {
        this.score += this.currentScore;
        this.currentScore = 0;
    }

    public void increaseThrow() {
        this.currentThrow += 1;
    }
}
