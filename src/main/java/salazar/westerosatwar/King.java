package salazar.westerosatwar;

import java.util.ArrayList;

/**
 * Created by Salazar on 07-01-2017.
 */
public class King {
    String name;
    double currentRating=400;
    int battlesWon=0;
    int battlesLost=0;
    ArrayList<String> battleType=null;
    int totalBattles=0;
    String mainBattleType
            ;

    public String getMainBattleType() {
        return mainBattleType;
    }

    public void setMainBattleType(String mainBattleType) {
        this.mainBattleType = mainBattleType;
    }



    public double getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(double currentRating) {
        this.currentRating = currentRating;
    }



    public int getTotalBattles() {
        return totalBattles;
    }

    public void setTotalBattles(int totalBattles) {
        this.totalBattles = totalBattles;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBattlesWon() {
        return battlesWon;
    }

    public void setBattlesWon(int battlesWon) {
        this.battlesWon = battlesWon;
    }

    public int getBattlesLost() {
        return battlesLost;
    }

    public void setBattlesLost(int battlesLost) {
        this.battlesLost = battlesLost;
    }

    public ArrayList<String> getBattleType() {
        return battleType;
    }

    public void setBattleType(ArrayList<String> battleType) {
        this.battleType = battleType;
    }




}
