package com.fellowcode.animewatcher.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fellowcode.animewatcher.Activities.MainActivity;
import com.fellowcode.animewatcher.Activities.WatchActivity;
import com.fellowcode.animewatcher.Anime.AnimeAdvanced;
import com.fellowcode.animewatcher.Anime.AnimeItemDecoration;
import com.fellowcode.animewatcher.Anime.AnimeList;
import com.fellowcode.animewatcher.Anime.Episode;
import com.fellowcode.animewatcher.Anime.Translation;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.Page;
import com.fellowcode.animewatcher.Utils.TranslationsPage;

import java.util.ArrayList;
import java.util.Objects;

public class TranslationsPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    Context context;

    Api api;

    ListView listView;

    ArrayList<Translation> tr = new ArrayList<>();

    public static TranslationsPageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TranslationsPageFragment fragment = new TranslationsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }

        api = ((WatchActivity) Objects.requireNonNull(getActivity())).api;

    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_translations, container, false);
        listView = view.findViewById(R.id.listView);

        final WatchActivity watchActivity = (WatchActivity) Objects.requireNonNull(getActivity());

        Episode episode = watchActivity.currentEpisode;

        if (mPage == TranslationsPage.VOICE) {
            tr = episode.getVoiceRuTranslations();
        } else if (mPage == TranslationsPage.SUB) {
            tr = episode.getSubRuTranslations();
        } else if (mPage == TranslationsPage.RAW) {
            tr = episode.getRawTranslations();
        }

        ArrayList<String> trNames = new ArrayList<>();
        for (int i=0;i<tr.size();i++){
            trNames.add(tr.get(i).getSummary());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, trNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                watchActivity.setupVideo(tr.get(position).embedUrl);
            }
        });

        return view;
    }

}
