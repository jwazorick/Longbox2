package com.example.longbox2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.longbox2.Objects.Creator;
import com.example.longbox2.R;

import java.util.List;

public class ViewCreatorAdapter extends RecyclerView.Adapter<ViewCreatorAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewComicCreator;
        public TextView txtViewComicJob;

        public ViewHolder(View itemView) {
            super(itemView);
            txtViewComicCreator = itemView.findViewById(R.id.txtViewComicCreator);
            txtViewComicJob = itemView.findViewById(R.id.txtViewComicJob);
        }
    }

    private List<Creator> creatorList;

    public ViewCreatorAdapter(List<Creator> creators) {
        this.creatorList = creators;
    }

    @Override
    public ViewCreatorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View creatorView = inflater.inflate(R.layout.recycler_view_comic_creator_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(creatorView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewCreatorAdapter.ViewHolder holder, int position) {
        Creator creator = creatorList.get(position);
        holder.txtViewComicCreator.setText(creator.getCreatorName());
        holder.txtViewComicJob.setText(creator.getCreatorJob());
    }

    @Override
    public int getItemCount() {
        return creatorList.size();
    }
}
