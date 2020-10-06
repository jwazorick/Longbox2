package com.wazorick.longbox2.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.wazorick.longbox2.Objects.Creator;
import com.wazorick.longbox2.R;

import java.util.List;

public class AddCreatorAdapter extends RecyclerView.Adapter<AddCreatorAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText addCreatorName;
        public EditText addCreatorJob;

        public ViewHolder(View itemView) {
            super((itemView));

            addCreatorName = itemView.findViewById(R.id.editCardAddCreatorName);
            addCreatorJob = itemView.findViewById(R.id.editCardAddCreatorJob);

            addCreatorName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    creatorList.get(getAdapterPosition()).setCreatorName(addCreatorName.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            addCreatorJob.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    creatorList.get(getAdapterPosition()).setCreatorJob(addCreatorJob.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private List<Creator> creatorList;

    public AddCreatorAdapter(List<Creator> creators) {
        creatorList = creators;
    }

    @Override
    public AddCreatorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View creatorView = inflater.inflate(R.layout.recycler_add_creators_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(creatorView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddCreatorAdapter.ViewHolder holder, int position) {
        Creator creator = creatorList.get(position);

        EditText creatorName = holder.addCreatorName;
        creatorName.setText(creator.getCreatorName());
        EditText creatorJob = holder.addCreatorJob;
        creatorJob.setText(creator.getCreatorJob());

    }

    @Override
    public int getItemCount() {
        return creatorList.size();
    }

    public List<Creator> getCreatorList() {
        return creatorList;
    }
}
