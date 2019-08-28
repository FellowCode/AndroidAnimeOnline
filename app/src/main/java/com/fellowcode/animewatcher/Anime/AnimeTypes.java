package com.fellowcode.animewatcher.Anime;

import com.fellowcode.animewatcher.R;

import java.util.ArrayList;
import java.util.Objects;

public class AnimeTypes {
    public class Type {
        String type;
        String title;
        int span_bg_res;

        Type(String type, String title, int span_bg_res){
            this.type = type;
            this.title = title;
            this.span_bg_res = span_bg_res;
        }
    }
    private ArrayList<Type> types = new ArrayList<>();
    public AnimeTypes(){
        types.add( new Type("tv","ТВ сериал", R.drawable.card_span_tv));
        types.add(new Type("ova", "OVA", R.drawable.card_span_ova));
        types.add( new Type("ona","ONA", R.drawable.card_span_ona));
        types.add(new Type("movie", "Фильм", R.drawable.card_span_movie));
        types.add( new Type("special","Special", R.drawable.card_span_special));
        types.add( new Type("music","Music", R.drawable.card_span_special));
    }

    Type get(String key){
        for (int i=0; i<types.size(); i++) {
            if (types.get(i).type.equals(key))
                return types.get(i);
        }
        return null;
    }
    public Type get(int pos){
        return types.get(pos);
    }

    public String getTitle(String key){
        return Objects.requireNonNull(get(key)).title;
    }

    public String getType(int pos){
        return types.get(pos).type;
    }

    public int getSpanBgRes(String key){
        return Objects.requireNonNull(get(key)).span_bg_res;
    }

    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();
        for (int i=0; i<types.size(); i++)
            names.add(types.get(i).title);
        return names;
    }

    public int size(){
        return types.size();
    }
}
