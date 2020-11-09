package com.wazorick.longbox2.Objects;

public class WeeklyItem extends Comic {
    private long datePublished;
    private int weeklyId;

    public WeeklyItem() {
        super();
        datePublished = -1;
        weeklyId = -1;
    }

    public long getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(long datePublished) {
        this.datePublished = datePublished;
    }

    public int getWeeklyId() {
        return weeklyId;
    }

    public void setWeeklyId(int weeklyId) {
        this.weeklyId = weeklyId;
    }
}
