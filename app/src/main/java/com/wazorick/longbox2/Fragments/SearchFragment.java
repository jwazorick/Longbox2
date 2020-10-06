package com.wazorick.longbox2.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wazorick.longbox2.Adapters.ViewCollectionAdapter;
import com.wazorick.longbox2.Database.DBHandler;
import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.Comic;
import com.wazorick.longbox2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText editSearchCriteria;
    private RadioButton rdoSearchTitle;
    private RadioButton rdoSearchPublisher;
    private RadioButton rdoSearchFormat;
    private RadioButton rdoSearchCreator;
    private ImageView btnSearchSearch;
    private ImageView btnSearchHome;
    private TextView txtSearchNumResults;

    private RecyclerView recyclerSearchResults;
    private ViewCollectionAdapter viewCollectionAdapter;

    private MainActivity mainActivity;
    private DBHandler dbHandler;
    private boolean priorSearch; //ToDo: Determine if I need this
    private List<Comic> comicList;

    private SearchFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editSearchCriteria = view.findViewById(R.id.editSearchCriteria);
        rdoSearchTitle = view.findViewById(R.id.rdoSearchTitle);
        rdoSearchPublisher = view.findViewById(R.id.rdoSearchPublisher);
        rdoSearchFormat = view.findViewById(R.id.rdoSearchFormat);
        rdoSearchCreator = view.findViewById(R.id.rdoSearchCreator);
        btnSearchSearch = view.findViewById(R.id.btnSearchSearch);
        btnSearchHome = view.findViewById(R.id.btnSearchHome);
        recyclerSearchResults = view.findViewById(R.id.recyclerSearchResults);
        txtSearchNumResults = view.findViewById(R.id.txtSearchNumResults);

        btnSearchHome.setOnClickListener(this);
        btnSearchSearch.setOnClickListener(this);

        mainActivity = (MainActivity)getActivity();
        dbHandler = new DBHandler(getContext());
        priorSearch = false;
        comicList = new ArrayList<>();

        loadRecycler();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnSearchHome.getId()) {
            //Back to the main screen
            mainActivity.loadMainFragment();
        } else if(v.getId() == btnSearchSearch.getId()) {
            performSearch();
        }
    }

    public void onButtonPressed(Uri uri) {
        if(mListener != null) {
            mListener.onSearchFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SearchFragmentInteractionListener) {
            mListener = (SearchFragmentInteractionListener) context;
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

    public interface SearchFragmentInteractionListener {
        void onSearchFragmentInteraction(Uri uri);
    }

    private void performSearch() {
        //Check if there's something in the search box
        if(editSearchCriteria.getText().toString().equalsIgnoreCase("")) {
            displayCriteriaError();
            return;
        }

        //If yes, perform search
        if(rdoSearchTitle.isChecked()) {
            //Title search
            comicList = dbHandler.searchByTitle(editSearchCriteria.getText().toString());
        } else if(rdoSearchPublisher.isChecked()) {
            //Publisher search
            comicList = dbHandler.searchByPublisher(editSearchCriteria.getText().toString());
        } else if(rdoSearchFormat.isChecked()) {
            //Format search
            comicList = dbHandler.searchByFormat(editSearchCriteria.getText().toString());
        } else {
            //Creator search
            comicList = dbHandler.searchByCreator(editSearchCriteria.getText().toString());
        }

        if(comicList.size() == 0) {
            txtSearchNumResults.setText(getResources().getString(R.string.search_no_results));
        } else {
            txtSearchNumResults.setText(R.string.search_num_results + comicList.size());
        }

        loadRecycler();
    }

    private void displayCriteriaError() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Search Criteria");
        builder.setMessage("No search criteria added. Please enter something to search for");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void loadRecycler() {
        viewCollectionAdapter = new ViewCollectionAdapter(comicList, getContext(), mainActivity);
        recyclerSearchResults.setAdapter(viewCollectionAdapter);
        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}