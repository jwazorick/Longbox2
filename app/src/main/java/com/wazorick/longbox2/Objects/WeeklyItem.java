package com.wazorick.longbox2.Objects;

public class WeeklyItem extends Comic {
    private long datePublished;

    public WeeklyItem() {
        super();
        datePublished = -1;
    }

    public long getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(long datePublished) {
        this.datePublished = datePublished;
    }
}
