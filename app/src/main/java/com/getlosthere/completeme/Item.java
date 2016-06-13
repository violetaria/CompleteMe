package com.getlosthere.completeme;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by violetaria on 6/12/16.
 */
@Table(name = "Items")
public class Item extends Model {
    @Column(name = "RemoteId")
    public long remoteId;

    @Column(name = "Text")
    public String text;

    @Column(name = "Completed")
    public boolean completed;

    public Item() {
        super();
    }

    public Item(long remoteId, String text) {
        super();
        this.text = text;
        this.remoteId = remoteId;
        this.completed = false;
    }

}

