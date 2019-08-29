package com.fellowcode.animewatcher.Anime;

import java.io.Serializable;

class Genre implements Serializable {
    int id;
    String title;

    public Genre(String id, String title) {
        this.id = Integer.valueOf(id);
        this.title = title;
    }

    public Genre(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
