package com.aps.tiktube.model.video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bson.Document;

import com.aps.tiktube.model.Entity;

public class Video extends Entity<Video> {

    private String videoName;

    private String description;
    
    private String userId;
    
    private String videoId;
    
    private String thumbnailId;
    
    private Date publishDate;

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getDescripton() {
        return description;
    }

    public void setDescripton(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public Class<Video> getTClass() {
        return Video.class;
    }

    @Override
    protected Document toDocument() {
        Document doc = new Document();

        doc.append("VideoName", this.videoName);
        doc.append("Description", this.description);
        doc.append("PublishDate", this.publishDate);
        doc.append("VideoId", this.videoId);
        doc.append("UserId", this.userId);
        doc.append("ThumbnailId", this.thumbnailId);

        return doc;
    }

    @Override
    protected void fromDocument(Document doc) {
        Object value;

        value = doc.get("VideoName");
        if (value != null)
            this.videoName = value.toString();

        value = doc.get("Description");
        if (value != null)
            this.description = value.toString();

        value = doc.get("VideoId");
        if (value != null)
            this.videoId = value.toString();

        value = doc.get("UserId");
        if (value != null)
            this.userId = value.toString();

        value = doc.get("ThumbnailId");
        if (value != null)
            this.thumbnailId = value.toString();

        value = doc.get("PublishDate");
        if (value != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date parsedDate = inputFormat.parse(value.toString());

                this.publishDate = parsedDate;
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }
    }
}
