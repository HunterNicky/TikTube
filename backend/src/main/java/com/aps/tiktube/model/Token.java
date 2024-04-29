package com.aps.tiktube.model;

import org.bson.Document;

public class Token extends Entity<Token> {
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    private String tokenValue;

    public String getTokenValue() {
        return this.tokenValue;
    }

    public void setTokenValue(String value) {
        this.tokenValue = value;
    }

    private Boolean isActive;

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean value) {
        this.isActive = value;
    }

    private Long lastTimeUsed;

    public Long getLastTimeUsed() {
        return this.lastTimeUsed;
    }

    public void setLastTimeUsed(Long value) {
        this.lastTimeUsed = value;
    }

    @Override
    public Class<Token> getTClass() {
        return Token.class;
    }

    @Override
    protected Document toDocument() {
        Document doc = new Document();
        doc.append("UserId", this.userId);
        doc.append("TokenValue", this.tokenValue);
        doc.append("IsActive", this.isActive);
        doc.append("LastTimeUsed", this.lastTimeUsed);
        return doc;
    }

    @Override
    protected void fromDocument(Document doc) {
        Object value;

        value = doc.get("UserId");
        if (value != null)
            this.userId = value.toString();

        value = doc.get("TokenValue");
        if (value != null)
            this.tokenValue = value.toString();

        value = doc.get("IsActive");
        if (value != null)
            this.isActive = Boolean.parseBoolean(value.toString());

        value = doc.get("LastTimeUsed");
        if (value != null)
            this.lastTimeUsed = Long.parseLong(value.toString());
    }

}
