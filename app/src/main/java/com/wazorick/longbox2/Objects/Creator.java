package com.example.longbox2.Objects;

public class Creator {
    private String creatorName;
    private String creatorJob;
    private int creatorID;
    private int creatorJobID;

    public Creator() {
        creatorName = "";
        creatorJob = "";
        creatorID = -1;
        creatorJobID = -1;
    }

    public Creator(String creatorName, String creatorJob, int creatorID) {
        this.creatorName = creatorName;
        this.creatorJob = creatorJob;
        this.creatorID = creatorID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorJob() {
        return creatorJob;
    }

    public void setCreatorJob(String creatorJob) {
        this.creatorJob = creatorJob;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public int getCreatorJobID() {
        return creatorJobID;
    }

    public void setCreatorJobID(int creatorJobID) {
        this.creatorJobID = creatorJobID;
    }
}
