package com.wazorick.longbox2.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wazorick.longbox2.Objects.Achievement;
import com.wazorick.longbox2.R;
import com.wazorick.longbox2.Utils.DateUtils;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtAchievementName;
        private TextView txtAchievementStatus;
        private TextView txtAchievementDescription;
        private ImageView imgAchievement;
        private LinearLayout layoutAchievementRow;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAchievementName = itemView.findViewById(R.id.txtAchievementName);
            txtAchievementStatus = itemView.findViewById(R.id.txtAchievementStatus);
            txtAchievementDescription = itemView.findViewById(R.id.txtAchievementDescription);
            imgAchievement = itemView.findViewById(R.id.imgAchievement);
            layoutAchievementRow = itemView.findViewById(R.id.layoutAchievementRow);
        }
    }

    private List<Achievement> achievements;
    private Context context;
    private Activity activity;

    public AchievementAdapter(List<Achievement> achievements, Context context, Activity activity) {
        this.achievements = achievements;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public AchievementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View achievementView = inflater.inflate(R.layout.recycler_achievement_row, parent, false);
        return new ViewHolder(achievementView);
    }

    @Override
    public void onBindViewHolder(AchievementAdapter.ViewHolder viewHolder, final int position) {
        final Achievement achievement = achievements.get(position);
        TextView txtAchievementName = viewHolder.txtAchievementName;
        TextView txtAchievementStatus = viewHolder.txtAchievementStatus;
        TextView txtAchivementDescription = viewHolder.txtAchievementDescription;
        ImageView imgAchievement = viewHolder.imgAchievement;
        LinearLayout layoutAchievementRow = viewHolder.layoutAchievementRow;

        txtAchievementName.setText(achievement.getAchievementName());
        txtAchievementStatus.setText(getFormattedStatusString(achievement));
        txtAchivementDescription.setText(achievement.getAchievementDescription());

        int achievementResource = context.getResources().getIdentifier(achievement.getAchievementImage(), "drawable", context.getPackageName());
        imgAchievement.setImageResource(achievementResource);

        if(!achievement.isAchievementUnlocked()) {
            //Still locked
            imgAchievement.setAlpha((float) .1);
        }

        layoutAchievementRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAchievementDetails(achievement);
            }
        });
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    private String getFormattedStatusString(Achievement achievement) {
        String formatted;
        if(achievement.isAchievementUnlocked()) {
            //Unlocked. Give data
            formatted = "Unlocked: " + DateUtils.convertLongToDateString(achievement.getAchievementDateUnlocked());
        } else {
            //Still locked
            formatted = "Locked";
        }
        return formatted;
    }

    private void displayAchievementDetails(Achievement achievement) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Achievement Details");
        LayoutInflater inflater = activity.getLayoutInflater();
        View achievementDialog = inflater.inflate(R.layout.dialog_achievement, null);

        final TextView txtDialogAchievementName = achievementDialog.findViewById(R.id.txtDialogAchievementName);
        final TextView txtDialogAchievementStatus = achievementDialog.findViewById(R.id.txtDialogAchievementStatus);
        final TextView txtDialogAchievementDescription = achievementDialog.findViewById(R.id.txtDialogAchievementDescription);
        final ImageView imgDialogAchievement = achievementDialog.findViewById(R.id.imgDialogAchievement);
        final ProgressBar progAchievementProgress = achievementDialog.findViewById(R.id.progAchievementProgress);

        int achievementResource = context.getResources().getIdentifier(achievement.getAchievementImage(), "drawable", context.getPackageName());
        imgDialogAchievement.setImageResource(achievementResource);
        txtDialogAchievementName.setText(achievement.getAchievementName());
        txtDialogAchievementDescription.setText(achievement.getAchievementDescription());

        String status;
        if(achievement.isAchievementUnlocked()) {
            status = "Unlocked " + DateUtils.convertLongToDateString(achievement.getAchievementDateUnlocked());
        } else {
            status = "Locked";
            imgDialogAchievement.setAlpha((float) .1);
        }
        txtDialogAchievementStatus.setText(status);

        progAchievementProgress.setMax(achievement.getTotal());
        progAchievementProgress.setProgress(achievement.getProgress());

        builder.setView(achievementDialog);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
