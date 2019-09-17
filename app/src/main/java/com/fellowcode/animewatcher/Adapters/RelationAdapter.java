package com.fellowcode.animewatcher.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.fellowcode.animewatcher.Activities.AnimeActivity;
import com.fellowcode.animewatcher.Anime.Relation;
import com.fellowcode.animewatcher.R;

import java.util.ArrayList;

public class RelationAdapter extends RecyclerView.Adapter<RelationAdapter.RelationViewHolder> {

    ArrayList<Relation> list;
    Context context;

    public RelationAdapter(ArrayList<Relation> list) {
        this.list = list;
    }

    @Override
    public RelationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_relation, parent, false);
        return new RelationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RelationViewHolder holder, final int position) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnimeActivity.class);
                intent.putExtra("anime", list.get(position).anime);
                context.startActivity(intent);
            }
        };
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class RelationViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView relation;
        View loader;
        View cardView;

        public RelationViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            relation = itemView.findViewById(R.id.relation);
            loader = itemView.findViewById(R.id.loader);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(Relation relation, View.OnClickListener listener) {
            image.layout(0, 0, 0, 0);
            Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
            Glide.with(context)
                    .load(relation.anime.posterUrlSmall)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .override(Target.SIZE_ORIGINAL)
                    .into(image);
            name.setText(relation.anime.russian);
            if (relation.rel_eng.equals("Alternative setting"))
                this.relation.setText(R.string.alt_setting);
            else if (relation.rel_eng.equals("Alternative version"))
                this.relation.setText(R.string.alt_version);
            else
                this.relation.setText(relation.rel_rus);

            cardView.setOnClickListener(listener);
        }
    }
}