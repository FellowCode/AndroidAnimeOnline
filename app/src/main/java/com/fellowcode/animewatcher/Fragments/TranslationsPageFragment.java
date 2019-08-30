package com.fellowcode.animewatcher.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fellowcode.animewatcher.Activities.WatchActivity;
import com.fellowcode.animewatcher.Anime.Anime;
import com.fellowcode.animewatcher.Anime.Episode;
import com.fellowcode.animewatcher.Anime.Translation;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Utils.TranslationsPage;

import java.util.ArrayList;
import java.util.Objects;

public class TranslationsPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    Context context;

    Api api;

    ListView listView;

    WatchActivity watchActivity;
    ArrayList<Translation> tr = new ArrayList<>();

    View currentSelectItem;


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
        watchActivity = (WatchActivity) Objects.requireNonNull(getActivity());
        watchActivity.tabs.add(this);

        api = watchActivity.api;

    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_translations, container, false);
        listView = view.findViewById(R.id.listView);

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
                watchActivity.clearSelectTranslation();
                currentSelectItem = listView.getChildAt(position);
                selectItem();
            }
        });
        selectItem();
        Log.d("test", "onCreateView"+mPage);
        Log.d("test", String.valueOf(listView.getChildCount()));


        return view;
    }

    public void clearSelect(){
        /*if (currentSelectItem != null)
            currentSelectItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));*/
    }

    void selectItem(){
        /*if (currentSelectItem != null)
            currentSelectItem.setBackgroundColor(getResources().getColor(R.color.selectItem));*/
    }

    public void hideList(){
        listView.setVisibility(View.INVISIBLE);
    }

}
