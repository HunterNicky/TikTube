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

    public void save() {
        Access<T> access = new Access<>(getTClass());
        if (this.exist) {
            access.update(this);
            access.close();
        } else {
            this.id = access.add(this);
            this.exist = true;
            access.close();
            this.toDocument();
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
