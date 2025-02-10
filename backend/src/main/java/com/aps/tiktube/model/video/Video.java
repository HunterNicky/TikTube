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

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public Class<Video> getTClass() {
        return Video.class;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();

        doc.append("video_name", this.videoName);
        doc.append("description", this.description);
        if (this.publishDate != null) {
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            outputFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            doc.append("publish_date", outputFormat.format(this.publishDate));
        }
        doc.append("video_id", this.videoId);
        doc.append("user_id", this.userId);
        doc.append("thumbnail_id", this.thumbnailId);

        return doc;
    }

    @Override
    protected void fromDocument(Document doc) {
        Object value;

        value = doc.get("video_name");
        if (value != null)
            this.videoName = value.toString();

        value = doc.get("description");
        if (value != null)
            this.description = value.toString();

        value = doc.get("video_id");
        if (value != null)
            this.videoId = value.toString();

        value = doc.get("user_id");
        if (value != null)
            this.userId = value.toString();

        value = doc.get("thumbnail_id");
        if (value != null)
            this.thumbnailId = value.toString();

        value = doc.get("publish_date");
        if (value != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                inputFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

                this.publishDate = inputFormat.parse(value.toString());
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }
    }
}
