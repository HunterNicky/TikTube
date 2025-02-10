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

    private String email;

    public String getEmail() {
        return this.email;
    }

    private Integer age;

    private Date birthDate;

    private String password;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    private String profilePictureId;

    private Boolean isAdmin = false;

    @Override
    public Class<User> getTClass() {
        return User.class;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();

        doc.append("user_name", this.userName);
        doc.append("email", this.email);
        if (this.birthDate != null) {
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            outputFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            doc.append("birth_date", outputFormat.format(this.birthDate));
        }
        doc.append("profile_picture_id", this.profilePictureId);
        doc.append("password", this.password);
        doc.append("is_admin", this.isAdmin);

        return doc;
    }

    @Override
    protected void fromDocument(Document doc) {
        Object value;

        value = doc.get("user_name");
        if (value != null)
            this.userName = value.toString();

        value = doc.get("email");
        if (value != null)
            this.email = value.toString();

        value = doc.get("birth_date");
        if (value != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

                this.birthDate = inputFormat.parse(value.toString());
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }

        value = doc.get("password");
        if (value != null)
            this.password = value.toString();

        value = doc.get("profile_picture_id");
        if (value != null)
            this.profilePictureId = value.toString();

        value = doc.get("is_admin");
        if (value != null)
            this.isAdmin = Boolean.parseBoolean(value.toString());
    }
}
