package com.wazorick.longbox2.Objects;

import com.wazorick.longbox2.Enums.PublicationFormat;

import java.util.ArrayList;
import java.util.List;

public class Comic {
    private int comicID;
    private String comicTitle;
    private String comicIssue;
    private String comicVolume;
    private String comicPublisherName;
    private int comicPublisherID;
    private String comicCoverPrice;
    private String comicCoverImage;
    private String comicConditionText;
    private int comicConditionID;
    private String comicNotes;
    private PublicationFormat comicFormat;
    private List<Creator> comicCreators;

    public Comic() {
        comicID = -1;
        comicTitle = "";
        comicIssue = "";
        comicVolume = "";
        comicPublisherName = "";
        comicPublisherID = -1;
        comicCoverPrice = "";
        comicCoverImage = "";
        comicConditionText = "";
        comicConditionID = -1;
        comicNotes = "";
        comicCreators = new ArrayList<>();
        comicFormat = PublicationFormat.UNKNOWN;
    }

    public Comic(int comicID, String comicTitle, String comicIssue, String comicVolume, String comicPublisherName, int comicPublisherID, String comicCoverPrice, String comicCoverImage, String comicConditionText, int comicConditionID, String comicNotes, PublicationFormat comicFormat, List<Creator> comicCreators) {
        this.comicID = comicID;
        this.comicTitle = comicTitle;
        this.comicIssue = comicIssue;
        this.comicVolume = comicVolume;
        this.comicPublisherName = comicPublisherName;
        this.comicPublisherID = comicPublisherID;
        this.comicCoverPrice = comicCoverPrice;
        this.comicCoverImage = comicCoverImage;
        this.comicConditionText = comicConditionText;
        this.comicConditionID = comicConditionID;
        this.comicNotes = comicNotes;
        this.comicFormat = comicFormat;
        this.comicCreators = comicCreators;
    }

    public int getComicID() {
        return comicID;
    }

    public void setComicID(int comicID) {
        this.comicID = comicID;
    }

    public String getComicTitle() {
        return comicTitle;
    }

    public void setComicTitle(String comicTitle) {
        this.comicTitle = comicTitle;
    }

    public String getComicIssue() {
        return comicIssue;
    }

    public void setComicIssue(String comicIssue) {
        this.comicIssue = comicIssue;
    }

    public String getComicVolume() {
        return comicVolume;
    }

    public void setComicVolume(String comicVolume) {
        this.comicVolume = comicVolume;
    }

    public String getComicPublisherName() {
        return comicPublisherName;
    }

    public void setComicPublisherName(String comicPublisherName) {
        this.comicPublisherName = comicPublisherName;
    }

    public int getComicPublisherID() {
        return comicPublisherID;
    }

    public void setComicPublisherID(int comicPublisherID) {
        this.comicPublisherID = comicPublisherID;
    }

    public String getComicCoverPrice() {
        return comicCoverPrice;
    }

    public void setComicCoverPrice(String comicCoverPrice) {
        this.comicCoverPrice = comicCoverPrice;
    }

    public String getComicCoverImage() {
        return comicCoverImage;
    }

    public void setComicCoverImage(String comicCoverImage) {
        this.comicCoverImage = comicCoverImage;
    }

    public String getComicConditionText() {
        return comicConditionText;
    }

    public void setComicConditionText(String comicConditionText) {
        this.comicConditionText = comicConditionText;
    }

    public int getComicConditionID() {
        return comicConditionID;
    }

    public void setComicConditionID(int comicConditionID) {
        this.comicConditionID = comicConditionID;
    }

    public String getComicNotes() {
        return comicNotes;
    }

    public void setComicNotes(String comicNotes) {
        this.comicNotes = comicNotes;
    }

    public PublicationFormat getComicFormat() {
        return comicFormat;
    }

    public void setComicFormat(PublicationFormat comicFormat) {
        this.comicFormat = comicFormat;
    }

    public List<Creator> getComicCreators() {
        return comicCreators;
    }

    public void setComicCreators(List<Creator> comicCreators) {
        this.comicCreators = comicCreators;
    }

    public void addComicCreator(int creatorID, String creatorName, String creatorJob) {
        Creator creator = new Creator(creatorName, creatorJob, creatorID);
        comicCreators.add(creator);
    }

    public void addComicCreator(String creatorName, String creatorJob) {
        Creator creator = new Creator();
        creator.setCreatorName(creatorName);
        creator.setCreatorJob(creatorJob);
        comicCreators.add(creator);
    }

    public int getNumberOfCreators() {
        return comicCreators.size();
    }
}
