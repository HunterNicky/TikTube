package com.aps.tiktube.model;

import org.bson.Document;

public abstract class Entity<T extends Entity<T>> {
    private boolean exist = false;

    public boolean getExist() {
        return this.exist;
    }

    public void setExist(boolean value) {
        this.exist = value;
    }

    private String id = null;

    public String getId() {
        return this.id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public Document save() {
        Access<T> access = new Access<>(getTClass());
        if (this.exist) {
            Document doc = access.update(this);
            access.close();
            return doc;
        } else {
            this.id = access.add(this);
            this.exist = true;
            access.close();
            return this.toDocument();
        }
    }

    public void delete() {
        if (this.exist) {
            Access<T> access = new Access<>(getTClass());
            access.deleteById(this.id);
            this.exist = false;
            access.close();
        }
    }

    public void setFromDocument(Document doc) {
        fromDocument(doc);
        Object iD = doc.get("_id");
        if (iD == null)
            return;
        this.id = iD.toString();
    }

    public abstract Class<T> getTClass();

    protected Document toDocument() {
        Document doc = toDocument();
        if (this.exist)
            doc.append("_id", this.id);
        return doc;
    }

    protected abstract void fromDocument(Document doc);

}
