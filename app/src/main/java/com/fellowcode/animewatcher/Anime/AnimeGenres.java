package com.fellowcode.animewatcher.Anime;

import java.util.ArrayList;

public class AnimeGenres {

    private ArrayList<Genre> genres = new ArrayList<>();
    
    public AnimeGenres(){
        genres.add(new Genre("5", "Безумие"));
        genres.add(new Genre("17", "Боевые искусства"));
        genres.add(new Genre("32", "Вампиры"));
        genres.add(new Genre("38", "Военное"));
        genres.add(new Genre("35", "Гарем"));
        genres.add(new Genre("6", "Демоны"));
        genres.add(new Genre("7", "Детектив"));
        genres.add(new Genre("15", "Детское"));
        genres.add(new Genre("43", "Дзёсей"));
        genres.add(new Genre("8", "Драма"));
        genres.add(new Genre("11", "Игры"));
        genres.add(new Genre("13", "Исторический"));
        genres.add(new Genre("4", "Комедия"));
        genres.add(new Genre("29", "Космос"));
        genres.add(new Genre("16", "Магия"));
        genres.add(new Genre("3", "Машины"));
        genres.add(new Genre("18", "Меха"));
        genres.add(new Genre("19", "Музыка"));
        genres.add(new Genre("20", "Пародия"));
        genres.add(new Genre("36", "Повседневность"));
        genres.add(new Genre("39", "Полиция"));
        genres.add(new Genre("2", "Приключения"));
        genres.add(new Genre("40", "Психологическое"));
        genres.add(new Genre("22", "Романтика"));
        genres.add(new Genre("37", "Сверхестественное"));
        genres.add(new Genre("42", "Сейнен"));
        genres.add(new Genre("30", "Спорт"));
        genres.add(new Genre("31", "Супер сила"));
        genres.add(new Genre("25", "Сёдзе"));
        genres.add(new Genre("26", "Сёдзе-Ай"));
        genres.add(new Genre("27", "Сёнен"));
        genres.add(new Genre("41", "Триллер"));
        genres.add(new Genre("14", "Ужасы"));
        genres.add(new Genre("24", "Фантастика"));
        genres.add(new Genre("10", "Фэнтези"));
        genres.add(new Genre("12", "Хентай"));
        genres.add(new Genre("23", "Школа"));
        genres.add(new Genre("1", "Экшен"));
        genres.add(new Genre("9", "Этти"));
        genres.add(new Genre("34", "Юри"));
        genres.add(new Genre("33", "Яой"));
    }

    public String getName(int pos){
        return genres.get(pos).title;
    }

    public String getId(int pos){
        return String.valueOf(genres.get(pos).id);
    }

    public Genre get(int pos){
        return genres.get(pos);
    }

    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();
        for (int i=0; i<genres.size(); i++)
            names.add(getName(i));
        return names;
    }
    public int size(){
        return genres.size();
    }
}
