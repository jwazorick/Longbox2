package com.wazorick.longbox2.Objects;

public class Achievement {
    private int achievementID;
    private String achievementName;
    private String achievementImage;
    private boolean achievementUnlocked;
    private long achievementDateUnlocked;
    private String achievementDescription;
    private int total;
    private int progress;

    public Achievement() {
        achievementID = -1;
        achievementName = "";
        achievementImage = "";
        achievementUnlocked = false;
        achievementDateUnlocked = -1;
        achievementDescription = "";
        total = 0;
        progress = 0;
    }

    public int getAchievementID() {
        return achievementID;
    }

    public void setAchievementID(int achievementID) {
        this.achievementID = achievementID;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public String getAchievementImage() {
        return achievementImage;
    }

    public void setAchievementImage(String achievementImage) {
        this.achievementImage = achievementImage;
    }

    public boolean isAchievementUnlocked() {
        return achievementUnlocked;
    }

    public void setAchievementUnlocked(boolean achievementUnlocked) {
        this.achievementUnlocked = achievementUnlocked;
    }

    public long getAchievementDateUnlocked() {
        return achievementDateUnlocked;
    }

    public void setAchievementDateUnlocked(long achievementDateUnlocked) {
        this.achievementDateUnlocked = achievementDateUnlocked;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public void setAchievementDescription(String achievementDescription) {
        this.achievementDescription = achievementDescription;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
