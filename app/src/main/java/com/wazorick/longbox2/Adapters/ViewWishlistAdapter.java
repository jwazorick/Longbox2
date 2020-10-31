package com.wazorick.longbox2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wazorick.longbox2.Objects.WishlistItem;
import com.wazorick.longbox2.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewWishlistAdapter extends RecyclerView.Adapter<ViewWishlistAdapter.ViewHolder> {
    //https://stackoverflow.com/questions/33434626/get-list-of-checked-checkboxes-from-recyclerview-android
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox chkWishlistSelect;
        private TextView txtWishlistTitle;
        private TextView txtWishlistIssue;
        private TextView txtWishlistPriority;

        public ViewHolder(View itemView) {
            super(itemView);
            chkWishlistSelect = itemView.findViewById(R.id.chkWishlistSelect);
            txtWishlistTitle = itemView.findViewById(R.id.txtWishlistTitle);
            txtWishlistIssue = itemView.findViewById(R.id.txtWishlistIssue);
            txtWishlistPriority = itemView.findViewById(R.id.txtWishlistPriority);
        }
    }

    private List<WishlistItem> wishlistItems;
    private Context context;
    private boolean[] checkStatus;
    private List<WishlistItem> selectedItems;

    public ViewWishlistAdapter(List<WishlistItem> wishlistItems, Context context) {
        this.wishlistItems = wishlistItems;
        this.context = context;
        checkStatus = new boolean[wishlistItems.size()];
        selectedItems = new ArrayList<>();

        clearAllCheckboxes();
    }

    @Override
    public ViewWishlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View wishlistView = inflater.inflate(R.layout.recycler_view_wishlist_row, parent, false);
        return new ViewHolder(wishlistView);
    }

    @Override
    public void onBindViewHolder(ViewWishlistAdapter.ViewHolder viewHolder, final int position) {
        final WishlistItem wishlistItem = wishlistItems.get(position);
        final CheckBox chkWishlistItem = viewHolder.chkWishlistSelect;
        TextView txtWishlistTitle = viewHolder.txtWishlistTitle;
        TextView txtWishlistIssue = viewHolder.txtWishlistIssue;
        TextView txtWishlistPriority = viewHolder.txtWishlistPriority;

        txtWishlistTitle.setText(wishlistItem.getComicTitle());
        txtWishlistIssue.setText(wishlistItem.getComicIssue());
        txtWishlistPriority.setText(wishlistItem.getWishlistPriority());

        if(checkStatus[position]) {
            chkWishlistItem.setChecked(true);
        }

        chkWishlistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chkWishlistItem.setChecked(!chkWishlistItem.isChecked());
                checkStatus[position] = chkWishlistItem.isChecked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public List<WishlistItem> getAllSelectedItems() {
        for(int i = 0; i < wishlistItems.size(); i++) {
            if(checkStatus[i]) {
                selectedItems.add(wishlistItems.get(i));
            }
        }
        return selectedItems;
    }

    public void clearAllCheckboxes() {
        Arrays.fill(checkStatus, false);
    }
}
