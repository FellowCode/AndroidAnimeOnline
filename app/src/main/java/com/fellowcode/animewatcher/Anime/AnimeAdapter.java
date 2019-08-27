package com.fellowcode.animewatcher.Anime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fellowcode.animewatcher.R;

import java.util.ArrayList;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {

    ArrayList<Anime> list;
    Context context;

    public AnimeAdapter(ArrayList<Anime> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_anime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount(){
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView type;
        TextView year;
        TextView score;


        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            year = itemView.findViewById(R.id.year);
            score = itemView.findViewById(R.id.score);
        }

        public void bind(Anime anime){
            image.layout(0,0,0,0);
            Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
            Glide.with(context)
                    .load(anime.posterUrlSmall)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(image);
            title.setText(anime.getTitle("ru"));

            if (anime.myAnimeListScore.equals("-1"))
                score.setText("???");
            else
                score.setText(anime.myAnimeListScore);

            year.setText(String.valueOf(anime.year));

            AnimeTypes at = new AnimeTypes();
            type.setText(at.getTitle(anime.type));
            type.setBackgroundResource(at.getSpanBgRes(anime.type));
            year.setBackgroundResource(at.getSpanBgRes(anime.type));
        }
    }
}
