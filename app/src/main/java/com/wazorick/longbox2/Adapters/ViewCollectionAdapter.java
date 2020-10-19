package com.wazorick.longbox2.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.Comic;
import com.wazorick.longbox2.R;
import com.wazorick.longbox2.Utils.FileUtils;

import java.util.List;

public class ViewCollectionAdapter extends RecyclerView.Adapter<ViewCollectionAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCollectionCover;
        private TextView txtCollectionTitle;
        private TextView txtCollectionVolume;
        private TextView txtCollectionIssue;
        private LinearLayout layoutCollectionMaster;

        public ViewHolder(View itemView) {
            super(itemView);
            imgCollectionCover = itemView.findViewById(R.id.imgCollectionCover);
            txtCollectionTitle = itemView.findViewById(R.id.txtCollectionTitle);
            txtCollectionVolume = itemView.findViewById(R.id.txtCollectionVolume);
            txtCollectionIssue = itemView.findViewById(R.id.txtCollectionIssue);
            layoutCollectionMaster = itemView.findViewById(R.id.layoutCollectionMaster);
        }
    }

    private List<Comic> comicList;
    private MainActivity mainActivity;
    private Context context;

    public ViewCollectionAdapter(List<Comic> comics, Context context, MainActivity activity) {
        this.comicList = comics;
        this.context = context;
        this.mainActivity = activity;
    }

    @Override
    public ViewCollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View collectionView = inflater.inflate(R.layout.recycler_view_collection_row, parent, false);
        return new ViewHolder(collectionView);
    }

    @Override
    public void onBindViewHolder(ViewCollectionAdapter.ViewHolder viewHolder, final int position) {
        final Comic comic = comicList.get(position);
        ImageView imgCollectionCover = viewHolder.imgCollectionCover;
        TextView txtCollectionTitle = viewHolder.txtCollectionTitle;
        TextView txtCollectionVolume = viewHolder.txtCollectionVolume;
        TextView txtCollectionIssue = viewHolder.txtCollectionIssue;
        LinearLayout layoutCollectionMaster = viewHolder.layoutCollectionMaster;

        if(!comic.getComicCoverImage().equalsIgnoreCase("")) {
            Bitmap cover = BitmapFactory.decodeFile(comic.getComicCoverImage());
            imgCollectionCover.setImageBitmap(cover);
        }
        imgCollectionCover.setImageBitmap(FileUtils.getCoverImage(comic.getComicCoverImage(), mainActivity));
        txtCollectionTitle.setText(comic.getComicTitle());
        txtCollectionVolume.setText(context.getString(R.string.volume_string, comic.getComicVolume()));
        txtCollectionIssue.setText(context.getString(R.string.issue_string, comic.getComicIssue()));

        layoutCollectionMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.ISSUE_ID = comic.getComicID();
                mainActivity.VIEW_COLLECTION_SCROLL_TO = position;
                mainActivity.loadViewComicFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }
}
