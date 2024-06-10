package com.aps.tiktube.model.video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bson.Document;

import com.aps.tiktube.model.Entity;

public class Comments extends Entity<Comments> {

    private String comment;

    private String userId;

    private String videoId;

    private Date data;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    @Override
    public Class<Comments> getTClass() {
        return Comments.class;
    }

    @Override
    protected Document toDocument() {
        Document doc = new Document();

        doc.append("comment", comment);
        doc.append("user_id", userId);
        doc.append("video_id", videoId);
        doc.append("data", data);
        
        return doc;
    }

    @Override
    protected void fromDocument(Document doc) {
        Object value;

        value = doc.get("comment");
        if (value instanceof String) {
            this.comment = (String) value;
        }

        value = doc.get("user_id");
        if (value instanceof String) {
            this.userId = (String) value;
        }

        value = doc.get("video_id");
        if (value instanceof String) {
            this.videoId = (String) value;
        }

        value = doc.get("data");
        if (value != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date parsedDate = inputFormat.parse(value.toString());

                this.data = parsedDate;
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }
    }
    
}
