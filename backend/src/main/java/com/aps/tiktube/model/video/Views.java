package com.aps.tiktube.model.video;

import org.bson.Document;

import com.aps.tiktube.model.Entity;

public class Views extends Entity<Views> {
    private String videoId;
    private String userId;

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Class<Views> getTClass() {
        return Views.class;
    }

    @Override
    protected Document toDocument() {
        Document doc = new Document();

        doc.append("video_id", videoId);
        doc.append("user_id", userId);

        return doc;
    }

    @Override
    protected void fromDocument(Document doc) {
        Object value;

        value = doc.get("video_id");
        if (value instanceof String) {
            this.videoId = (String) value;
        }

        value = doc.get("user_id");
        if (value instanceof String) {
            this.userId = (String) value;
        }
    }
    
}
