package com.fellowcode.animewatcher.Adapters;

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
import com.bumptech.glide.request.target.Target;
import com.fellowcode.animewatcher.Anime.AnimeCharacter;
import com.fellowcode.animewatcher.R;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    ArrayList<AnimeCharacter> list;
    Context context;

    public CharacterAdapter(ArrayList<AnimeCharacter> list) {
        this.list = list;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, final int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class CharacterViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        View loader;

        public CharacterViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            loader = itemView.findViewById(R.id.loader);
        }

        public void bind(AnimeCharacter animeCharacter) {
            image.layout(0, 0, 0, 0);
            Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
            Glide.with(context)
                    .load(animeCharacter.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .override(Target.SIZE_ORIGINAL)
                    .into(image);
            name.setText(animeCharacter.russian);
        }
    }
}
