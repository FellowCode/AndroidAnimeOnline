package com.fellowcode.animewatcher.Anime;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fellowcode.animewatcher.Activities.AnimeActivity;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.User.UserRates;

import java.util.ArrayList;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {

    ArrayList<Anime> list;
    Context context;

    public AnimeAdapter(ArrayList<Anime> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_anime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnimeActivity.class);
                intent.putExtra("anime", list.get(position));
                context.startActivity(intent);
            }
        };
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView rateStatus;
        TextView title;
        TextView type;
        TextView year;
        TextView score;
        CardView cardView;

        View loader;

        private RecyclerView animeRecyclerView;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            rateStatus = itemView.findViewById(R.id.statusRate);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            year = itemView.findViewById(R.id.year);
            score = itemView.findViewById(R.id.score);
            cardView = itemView.findViewById(R.id.card_view);
            loader = itemView.findViewById(R.id.loader);

            animeRecyclerView = itemView.findViewById(R.id.recyclerView);
        }

        public void bind(Anime anime, View.OnClickListener listener) {
            image.layout(0, 0, 0, 0);
            Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
            Glide.with(context)
                    .load(anime.posterUrlSmall)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loader.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .override(Target.SIZE_ORIGINAL)
                    .into(image);
            title.setText(anime.getTitle("ru"));

            if (anime.myAnimeListScore == null || anime.myAnimeListScore.equals("-1"))
                score.setVisibility(View.INVISIBLE);
            else
                score.setText(anime.myAnimeListScore);

            if (anime.rateStatus != null)
                if (UserRates.getIcon(anime.rateStatus) != 0)
                    rateStatus.setImageResource(UserRates.getIcon(anime.rateStatus));
                else {
                    rateStatus.setImageResource(R.drawable.dropped);
                    rateStatus.setVisibility(View.INVISIBLE);
                }
            else {
                rateStatus.setImageResource(R.drawable.dropped);
                rateStatus.setVisibility(View.INVISIBLE);
            }

            year.setText(String.valueOf(anime.year));

            AnimeTypes at = new AnimeTypes();
            type.setText(at.getTitle(anime.type));
            type.setBackgroundResource(at.getSpanBgRes(anime.type));
            year.setBackgroundResource(at.getSpanBgRes(anime.type));


            cardView.setOnClickListener(listener);
        }
    }
}
