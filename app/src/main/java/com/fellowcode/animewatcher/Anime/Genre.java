package com.fellowcode.animewatcher.Anime;

class Genre {
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
