package com.fellowcode.animewatcher.Anime;

import android.content.Context;

import com.fellowcode.animewatcher.Utils.Serialize;

import java.util.ArrayList;

public class Favorites {
    Context context;

    ArrayList<AnimeAdvanced> favorites;

    public Favorites(Context context) {
        this.context = context;
        favorites = Serialize.read(context,"favorites");
        if (favorites == null)
            favorites = new ArrayList<>();
    }

    public void updateItem(AnimeAdvanced anime) {
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).id == anime.id || favorites.get(i).shikiId == anime.shikiId) {
                favorites.set(i, anime);
                save();
                return;
            }
        }
    }

    public void updateItem(Anime anime) {
        for (int i = 0; i < favorites.size(); i++) {
            if ((favorites.get(i).id == anime.id && anime.id != 0) || favorites.get(i).shikiId == anime.shikiId) {
                favorites.get(i).updateAnimeValues(anime);
                return;
            }
        }
    }

    public void updateItems(ArrayList<Anime> animes){
        for (int i=0; i<animes.size(); i++)
            updateItem(animes.get(i));
        save();
    }

    public boolean checkIn(Anime anime){
        for (int i = 0; i < favorites.size(); i++) {
            if ((favorites.get(i).id == anime.id && anime.id != 0) || favorites.get(i).shikiId == anime.shikiId)
                return true;
        }
        return false;
    }

    int getPosition(AnimeAdvanced anime){
        for (int i = 0; i < favorites.size(); i++) {
            if ((favorites.get(i).id == anime.id && anime.id != 0) || favorites.get(i).shikiId == anime.shikiId)
                return i;
        }
        return -1;
    }

    public void add(AnimeAdvanced anime){
        favorites.add(anime);
        save();
    }

    public void remove(AnimeAdvanced anime){
        int pos = getPosition(anime);
        if (pos != -1) {
            favorites.remove(pos);
            save();
        }
    }

    public ArrayList<Integer> getShikiIdsList(){
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i=0; i<favorites.size(); i++)
            ids.add(favorites.get(i).shikiId);
        return ids;
    }

    private void save() {
        Serialize.write(context, "favorites", favorites);
    }
}
