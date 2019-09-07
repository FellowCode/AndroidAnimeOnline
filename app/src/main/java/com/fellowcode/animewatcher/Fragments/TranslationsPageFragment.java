package com.fellowcode.animewatcher.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.fellowcode.animewatcher.Adapters.TranslationAdapter;
import com.fellowcode.animewatcher.Anime.Episode;
import com.fellowcode.animewatcher.Anime.Translation;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.Adapters.TranslationsPage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class TranslationsPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    Context context;

    Api api;

    ListView listView;

    WatchActivity watchActivity;
    ArrayList<Translation> tr = new ArrayList<>();

    View currentSelectItem;

    SharedPreferences transSaver;

    int preselectIndex = -1;
    int animeId;


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

        transSaver = Objects.requireNonNull(getActivity()).getSharedPreferences("translSaver", Context.MODE_PRIVATE);

        if (mPage == TranslationsPage.VOICE) {
            tr = episode.getVoiceRuTranslations();
        } else if (mPage == TranslationsPage.SUB) {
            tr = episode.getSubRuTranslations();
        } else if (mPage == TranslationsPage.RAW) {
            tr = episode.getRawTranslations();
        }
        if (tr != null && tr.size() > 0)
            animeId = tr.get(0).animeId;

        TranslationAdapter adapter = new TranslationAdapter(context, tr, preselectIndex);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                watchActivity.setupVideo(tr.get(position).embedUrl);
                watchActivity.clearSelectTranslation();
                selectTranslationItem(position);

                transSaver.edit().putInt("transPage", mPage)
                        .putInt(animeId+"transPage", mPage)
                        .putString(animeId+"authors", tr.get(position).authorsSummary)
                        .apply();
            }
        });

        return view;
    }

    public void clearSelect(){
        for (int i=0;i<tr.size();i++){
            clearSelectItem(i);
        }
    }

    public void hideList(){
        listView.setVisibility(View.INVISIBLE);
    }

    public void setSelect(){
        String authors = transSaver.getString(animeId+"authors", "");
        Log.d("test", "authors: "+authors);
        for (int i=0; i<tr.size(); i++){
            if (tr.get(i).authorsSummary.equals(authors)) {
                watchActivity.setupVideo(tr.get(i).embedUrl);
                preselectIndex = i;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        selectTranslationItem(preselectIndex);
                    }
                }, 500);

                Log.d("test", "autoSelect"+preselectIndex);
                return;
            }
        }
    }

    void selectTranslationItem(int index){
        View v = listView.getChildAt(index - listView.getFirstVisiblePosition());

        if (v==null)
            return;

        View mainLayout = v.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.translation_select_bg);
    }

    void clearSelectItem(int index){
        View v = listView.getChildAt(index - listView.getFirstVisiblePosition());

        if (v==null)
            return;

        View mainLayout = v.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.translation_bg);
    }
}
