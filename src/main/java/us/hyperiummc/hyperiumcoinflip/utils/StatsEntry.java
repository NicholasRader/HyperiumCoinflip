package us.hyperiummc.hyperiumcoinflip.utils;

import java.util.HashMap;

public class StatsEntry {
    private int winStats = 0;

    private int loseStats = 0;

    private HashMap<Integer, Integer> statRatio = new HashMap<>();

    public HashMap<Integer, Integer> getStatRatio() {
        return this.statRatio;
    }

    public int getWinStats() {
        return this.winStats;
    }

    public int getLoseStats() {
        return this.loseStats;
    }

    public void incrementWin() {
        this.winStats++;
    }

    public void incrementLose() {
        this.loseStats++;
    }
}
