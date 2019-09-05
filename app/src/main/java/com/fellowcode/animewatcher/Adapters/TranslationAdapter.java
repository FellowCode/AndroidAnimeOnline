package com.fellowcode.animewatcher.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fellowcode.animewatcher.Anime.Translation;
import com.fellowcode.animewatcher.R;

import java.util.ArrayList;
import java.util.List;

public class TranslationAdapter extends ArrayAdapter<Translation> {

    private Context context;
    private List<Translation> translationList = new ArrayList<>();

    public TranslationAdapter(@NonNull Context context, ArrayList<Translation> list){
        super(context, 0, list);
        this.context = context;
        translationList = list;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_translation,parent,false);

        Translation currTrans = translationList.get(position);

        TextView authors = listItem.findViewById(R.id.authors);
        TextView qualityType = listItem.findViewById(R.id.qualityType);
        TextView height = listItem.findViewById(R.id.height);

        if (currTrans.authorsSummary.equals(""))
            authors.setText(R.string.no_name);
        else
            authors.setText(currTrans.authorsSummary);

        if (currTrans.qualityType.equals("tv")){
            qualityType.setBackgroundResource(R.drawable.card_span_ova);
        } else if (currTrans.qualityType.equals("bd"))
            qualityType.setBackgroundResource(R.drawable.card_span_tv);
        qualityType.setText(currTrans.qualityType.toUpperCase());

        if (currTrans.height > 900){
            height.setBackgroundResource(R.drawable.card_span_special);
        } else if (currTrans.height > 600){
            height.setBackgroundResource(R.drawable.card_span_ona);
        } else if (currTrans.height > 420){
            height.setBackgroundResource(R.drawable.card_span_ova);
        } else {
            height.setBackgroundResource(R.drawable.card_span_movie);
        }

        height.setText(String.format("%dp", currTrans.height));

        if (currTrans.height == 0)
            height.setVisibility(View.GONE);

        return listItem;
    }
}
