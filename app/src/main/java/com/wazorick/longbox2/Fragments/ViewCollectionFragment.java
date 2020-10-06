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

import com.wazorick.longbox2.Adapters.ViewCollectionAdapter;
import com.wazorick.longbox2.Database.DBHandler;
import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.Comic;
import com.wazorick.longbox2.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewCollectionFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewCollection;
    private ImageView btnCollectionBack;
    private MainActivity mainActivity;
    private ViewCollectionAdapter viewCollectionAdapter;

    private ViewCollectionFragmentInteractionListener mListener;

    public ViewCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewCollectionFragment.
     */
    public static ViewCollectionFragment newInstance(String param1, String param2) {
        ViewCollectionFragment fragment = new ViewCollectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_collection, container, false);

        recyclerViewCollection = view.findViewById(R.id.recyclerViewCollection);
        btnCollectionBack = view.findViewById(R.id.btnCollectionBack);

        mainActivity = (MainActivity) getActivity();

        btnCollectionBack.setOnClickListener(this);
        loadCollection();

        recyclerViewCollection.scrollToPosition(mainActivity.VIEW_COLLECTION_SCROLL_TO);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnCollectionBack.getId()) {
            //Go back to main fragment
            mainActivity.loadMainFragment();
        }
    }

    public void onButtonPressed(Uri uri) {
        if(mListener != null) {
            mListener.onViewCollectionFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewCollectionFragmentInteractionListener) {
            mListener = (ViewCollectionFragmentInteractionListener) context;
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

    public interface ViewCollectionFragmentInteractionListener {
        void onViewCollectionFragmentInteraction(Uri uri);
    }

    private void loadCollection() {
        DBHandler dbHandler = new DBHandler(getContext());
        List<Comic> comicList = dbHandler.getAllComicsForGallery();

        viewCollectionAdapter = new ViewCollectionAdapter(comicList, getContext(), mainActivity);
        recyclerViewCollection.setAdapter(viewCollectionAdapter);
        recyclerViewCollection.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}