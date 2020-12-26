package com.wazorick.longbox2.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView btnMainAdd;
    private ImageView btnMainView;
    private ImageView btnMainSearch;
    private ImageView btnMainWishlist;
    private ImageView btnMainWeekly;
    private ImageView btnMainAchievements;
    private ImageView btnMainSettings;

    private MainActivity mainActivity;

    private MainFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnMainAdd = view.findViewById(R.id.btnMainAdd);
        btnMainView = view.findViewById(R.id.btnMainView);
        btnMainSearch = view.findViewById(R.id.btnMainSearch);
        btnMainWishlist = view.findViewById(R.id.btnMainWishlist);
        btnMainWeekly = view.findViewById(R.id.btnMainWeekly);
        btnMainAchievements = view.findViewById(R.id.btnMainAchievements);
        btnMainSettings = view.findViewById(R.id.btnMainSettings);

        mainActivity = (MainActivity) getActivity();

        btnMainAdd.setOnClickListener(this);
        btnMainView.setOnClickListener(this);
        btnMainSearch.setOnClickListener(this);
        btnMainWishlist.setOnClickListener(this);
        btnMainWeekly.setOnClickListener(this);
        btnMainAchievements.setOnClickListener(this);
        btnMainSettings.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnMainAdd.getId()) {
            //Go to the add fragment
            mainActivity.loadAddComicFragment();
        } else if(v.getId() == btnMainView.getId()) {
            //Go to the view collection fragment
            mainActivity.loadViewCollectionFragment();
        } else if(v.getId() == btnMainSearch.getId()) {
            //Go to the search fragment
            mainActivity.loadSearchFragment();
        } else if(v.getId() == btnMainWishlist.getId()) {
            //Go to wishlist fragment
            mainActivity.loadWishlistFragment();
        } else if(v.getId() == btnMainWeekly.getId()) {
            //Go to weekly list fragment
            mainActivity.loadWeeklyListFragment();
        } else if(v.getId() == btnMainAchievements.getId()) {
            //Go to achievement fragment
            mainActivity.loadAchievementFragment();
        } else if(v.getId() == btnMainSettings.getId()) {
            //Go to setting fragment
        }
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentInteractionListener) {
            mListener = (MainFragmentInteractionListener) context;
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

    public interface MainFragmentInteractionListener {
        void onMainFragmentInteraction(Uri uri);
    }
}
