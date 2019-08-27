package com.fellowcode.animewatcher.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fellowcode.animewatcher.Anime.AnimeGenres;
import com.fellowcode.animewatcher.Anime.AnimeTypes;
import com.fellowcode.animewatcher.Api.Link;
import com.fellowcode.animewatcher.Activities.FilterActivity;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.WrapContentListView;

import java.util.Calendar;
import java.util.Objects;

public class FiltersFragment extends Fragment {

    LinearLayout genresBtn, typesBtn;
    WrapContentListView genresList, typesList;
    boolean genresIsOpen, typesIsOpen;

    AnimeGenres genres = new AnimeGenres();
    AnimeTypes types = new AnimeTypes();

    EditText year_from, year_to, rating_from, rating_to;

    TextView genres_op;
    boolean anyGenre = true;

    FrameLayout ongoingBtn;
    CheckBox ongoing;
    Button applyBtn;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        genresIsOpen = false;
        typesIsOpen = true;

        genresBtn = view.findViewById(R.id.genres);
        typesBtn = view.findViewById(R.id.types);

        genresList = view.findViewById(R.id.genresList);
        typesList = view.findViewById(R.id.typesList);

        genresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genresIsOpen = !genresIsOpen;
                int visibility = genresIsOpen ? View.VISIBLE : View.GONE;
                genresList.setVisibility(visibility);
            }
        });
        typesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typesIsOpen = !typesIsOpen;
                int visibility = typesIsOpen ? View.VISIBLE : View.GONE;
                typesList.setVisibility(visibility);
            }
        });

        ArrayAdapter<String> genresAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                genres.getNames());
        genresList.setAdapter(genresAdapter);

        genres_op = view.findViewById(R.id.genres_op);
        genres_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyGenre = !anyGenre;
                if (anyGenre)
                    genres_op.setText(R.string.any_genres);
                else
                    genres_op.setText(R.string.all_genres);
            }
        });

        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                types.getNames());
        typesList.setAdapter(typesAdapter);

        year_from = view.findViewById(R.id.year_from);
        year_to = view.findViewById(R.id.year_to);
        rating_from = view.findViewById(R.id.rating_from);
        rating_to = view.findViewById(R.id.rating_to);

        ongoingBtn = view.findViewById(R.id.ongoingBtn);
        ongoing = view.findViewById(R.id.ongoing);

        ongoingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ongoing.setChecked(!ongoing.isChecked());
            }
        });

        applyBtn = view.findViewById(R.id.apply);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFilter();
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if (anyGenre)
            genres_op.setText(R.string.any_genres);
        else
            genres_op.setText(R.string.all_genres);
    }

    String getSelectedGenres(){
        StringBuilder selectedGenres = new StringBuilder();
        SparseBooleanArray checked = genresList.getCheckedItemPositions();
        for (int i=0; i<genres.size(); i++){
            if(checked.get(i))
                selectedGenres.append(genres.getId(i)).append(",");
        }
        if (selectedGenres.length()>0) {
            selectedGenres.deleteCharAt(selectedGenres.length() - 1);
            return selectedGenres.toString();
        }
        return null;
    }

    String getSelectedTypes(){
        StringBuilder selectedTypes = new StringBuilder();
        SparseBooleanArray checked = typesList.getCheckedItemPositions();
        for (int i=0; i<types.size(); i++){
            if(checked.get(i))
                selectedTypes.append(types.getType(i)).append(",");
        }
        if (selectedTypes.length()>0) {
            selectedTypes.deleteCharAt(selectedTypes.length() - 1);
            return selectedTypes.toString();
        }
        return null;
    }

    String getYear(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return getDiapason(year_from.getText().toString(), year_to.getText().toString(), 1973, year+1);
    }
    String getRating(){
        return getDiapason(rating_from.getText().toString(), rating_to.getText().toString(), 0, 10);
    }

    String getDiapason(String from, String to, int min, int max){
        if (from.equals(to) && from.length()  > 0)
            return from;
        if (from.length() == 0 && to.length() > 0) {
            if (!to.equals(String.valueOf(min)))
                return min + "-" + to;
            return to;
        }
        if (from.length() > 0 && to.length() == 0) {
            if (!from.equals(String.valueOf(max)))
                return from + "-" + max;
            return from;
        }
        if (from.length() > 0 && to.length() > 0) {
            return from + "-" + to;
        }
        return null;
    }

    public void doFilter(){
        Link link = new Link()
                .addFilterField("yearseason", getYear())
                .addFilterField("rating", getRating())
                .addFilterField(anyGenre ? "genre" : "genre@", getSelectedGenres())
                .addFilterField("type", getSelectedTypes())
                .addField("isAiring", ongoing.isChecked() ? "1" : null);

        ((FilterActivity) Objects.requireNonNull(getActivity())).doFilter(link);
    }
}
