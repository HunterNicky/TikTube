package com.aps.tiktube.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bson.Document;

public class User extends Entity<User> {
    private String userName;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    private Integer age;

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer value) {
        this.age = value;
    }

    private Date birthDate;

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date value) {
        this.birthDate = value;
    }

    private String password;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    private String profilePictureId;

    public String getProfilePictureId() {
        return this.profilePictureId;
    }

    public void setProfilePictureId(String value) {
        this.profilePictureId = value;
    }

    private Boolean isAdmin = false;

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public Class<User> getTClass() {
        return User.class;
    }

    @Override
    protected Document toDocument() {
        Document doc = new Document();

        doc.append("UserName", this.userName);
        doc.append("Email", this.email);
        doc.append("BirthDate", this.birthDate);
        doc.append("Password", this.password);
        doc.append("ProfilePictureId", this.profilePictureId);
        doc.append("IsAdmin", this.isAdmin);

        return doc;
    }

    @Override
    protected void fromDocument(Document doc) {
        Object value;

        value = doc.get("UserName");
        if (value != null)
            this.userName = value.toString();

        value = doc.get("Email");
        if (value != null)
            this.email = value.toString();

        value = doc.get("BirthDate");
        if (value != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date parsedDate = inputFormat.parse(value.toString());

                this.birthDate = parsedDate;
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }

        value = doc.get("Password");
        if (value != null)
            this.password = value.toString();

        value = doc.get("ProfilePictureId");
        if (value != null)
            this.profilePictureId = value.toString();

        value = doc.get("IsAdmin");
        if (value != null)
            this.isAdmin = Boolean.parseBoolean(value.toString());
    }
}
