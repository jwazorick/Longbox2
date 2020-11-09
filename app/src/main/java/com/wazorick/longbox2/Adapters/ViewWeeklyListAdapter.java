package com.wazorick.longbox2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wazorick.longbox2.Objects.WeeklyItem;
import com.wazorick.longbox2.R;
import com.wazorick.longbox2.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewWeeklyListAdapter extends RecyclerView.Adapter<ViewWeeklyListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox chkWeeklySelected;
        private TextView txtWeeklyTitle;
        private TextView txtWeeklyIssue;
        private TextView txtWeeklyDate;

        public ViewHolder(View itemView) {
            super(itemView);
            chkWeeklySelected = itemView.findViewById(R.id.chkWeeklySelected);
            txtWeeklyTitle = itemView.findViewById(R.id.txtWeeklyTitle);
            txtWeeklyIssue = itemView.findViewById(R.id.txtWeeklyIssue);
            txtWeeklyDate = itemView.findViewById(R.id.txtWeeklyDate);
        }
    }

    private List<WeeklyItem> weeklyItemList;
    private Context context;
    private boolean[] chkStatus;
    private List<WeeklyItem> selectedItems;

    public ViewWeeklyListAdapter(List<WeeklyItem> weeklyItems, Context context) {
        this.weeklyItemList = weeklyItems;
        this.context = context;
        chkStatus = new boolean[weeklyItemList.size()];
        selectedItems = new ArrayList<>();
        clearAllCheckboxes();
    }

    @Override
    public ViewWeeklyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View weeklyListView = inflater.inflate(R.layout.recycler_view_weekly_row, parent, false);
        return new ViewHolder(weeklyListView);
    }

    @Override
    public void onBindViewHolder(ViewWeeklyListAdapter.ViewHolder viewHolder, final int position) {
        final WeeklyItem weeklyItem = weeklyItemList.get(position);
        final CheckBox chkWeeklySelected = viewHolder.chkWeeklySelected;
        TextView txtWeeklyTitle = viewHolder.txtWeeklyTitle;
        TextView txtWeeklyIssue = viewHolder.txtWeeklyIssue;
        TextView txtWeeklyDate = viewHolder.txtWeeklyDate;

        txtWeeklyTitle.setText(context.getString(R.string.weekly_row_title, weeklyItem.getComicTitle()));
        txtWeeklyIssue.setText(context.getString(R.string.weekly_row_issue, weeklyItem.getComicIssue()));
        txtWeeklyDate.setText(context.getString(R.string.weekly_row_date, DateUtils.convertLongToDateString(weeklyItem.getDatePublished())));

        if(chkStatus[position]) {
            chkWeeklySelected.setChecked(true);
        }

        chkWeeklySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkStatus[position] = chkWeeklySelected.isChecked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return weeklyItemList.size();
    }

    public List<WeeklyItem> getAllSelectedItems() {
        selectedItems = new ArrayList<>();
        for(int i = 0; i < weeklyItemList.size(); i++) {
            if(chkStatus[i]) {
                selectedItems.add(weeklyItemList.get(i));
            }
        }
        return selectedItems;
    }

    public void clearAllCheckboxes() {
        Arrays.fill(chkStatus, false);
    }
}
