package com.wazorick.longbox2.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wazorick.longbox2.Adapters.AchievementAdapter;
import com.wazorick.longbox2.Database.DBHandler;
import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.Achievement;
import com.wazorick.longbox2.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchievementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchievementFragment extends Fragment implements View.OnClickListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView btnAchievementHome;
    private RecyclerView recyclerAchievements;

    private AchievementFragmentInteractionListener mListener;
    private Context mContext;
    private MainActivity mainActivity;
    private DBHandler dbHandler;

    public AchievementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementFragment.
     */
    public static AchievementFragment newInstance(String param1, String param2) {
        AchievementFragment fragment = new AchievementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        btnAchievementHome = view.findViewById(R.id.btnAchievementHome);
        recyclerAchievements = view.findViewById(R.id.recyclerAchievements);

        dbHandler = new DBHandler(mContext);
        mainActivity = (MainActivity) requireActivity();

        btnAchievementHome.setOnClickListener(this);

        //Load recycler
        loadAchievements();

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAchievementHome) {
            mainActivity.loadMainFragment();
        }
    }

    public void onButtonPressed(Uri uri) {
        if(mListener != null) {
            mListener.onAchievementFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof AchievementFragmentInteractionListener) {
            mListener = (AchievementFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AchievementFragmentInteractionListener {
        void onAchievementFragmentInteraction(Uri uri);
    }

    private void loadAchievements() {
        List<Achievement> achievements = dbHandler.getAllOrderedAchievements();
        AchievementAdapter achievementAdapter = new AchievementAdapter(achievements, mContext, requireActivity());
        recyclerAchievements.setAdapter(achievementAdapter);
        recyclerAchievements.setLayoutManager(new LinearLayoutManager(mContext));
    }
}