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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wazorick.longbox2.Adapters.ViewCreatorAdapter;
import com.wazorick.longbox2.Database.DBHandler;
import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.Comic;
import com.wazorick.longbox2.Objects.Creator;
import com.wazorick.longbox2.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewComicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewComicFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView txtViewTitle;
    private TextView txtViewVolume;
    private TextView txtViewIssue;
    private TextView txtViewPublisher;
    private TextView txtViewCoverPrice;
    private TextView txtViewCondition;
    private TextView txtViewFormat;
    private TextView txtViewNotes;
    private ImageView imgViewCover;
    private ImageView btnViewEdit;
    private ImageView btnViewDelete;
    private ImageView btnViewHome;
    private RecyclerView recyclerViewCreators;

    private DBHandler dbHandler;
    private MainActivity mainActivity;
    private Comic comic;
    private ViewCreatorAdapter viewCreatorAdapter;

    private ViewComicFragmentInteractionListener mListener;

    public ViewComicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewComicFragment.
     */
    public static ViewComicFragment newInstance(String param1, String param2) {
        ViewComicFragment fragment = new ViewComicFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_comic, container, false);

        txtViewTitle = view.findViewById(R.id.txtViewTitle);
        txtViewVolume = view.findViewById(R.id.txtViewVolume);
        txtViewIssue = view.findViewById(R.id.txtViewIssue);
        txtViewPublisher = view.findViewById(R.id.txtViewPublisher);
        txtViewCoverPrice = view.findViewById(R.id.txtViewCoverPrice);
        txtViewCondition = view.findViewById(R.id.txtViewCondition);
        txtViewFormat = view.findViewById(R.id.txtViewFormat);
        txtViewNotes = view.findViewById(R.id.txtViewNotes);
        imgViewCover = view.findViewById(R.id.imgViewCover);
        btnViewEdit = view.findViewById(R.id.btnViewEdit);
        btnViewDelete = view.findViewById(R.id.btnViewDelete);
        btnViewHome = view.findViewById(R.id.btnViewHome);
        recyclerViewCreators = view.findViewById(R.id.recyclerViewCreators);

        btnViewEdit.setOnClickListener(this);
        btnViewDelete.setOnClickListener(this);
        btnViewHome.setOnClickListener(this);

        mainActivity = (MainActivity)getActivity();
        dbHandler = new DBHandler(getContext());

        //Load comic data
        loadComicData();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnViewHome.getId()) {
            mainActivity.loadMainFragment();
        } else if(v.getId() == btnViewEdit.getId()) {
            //Go to the edit fragment
            editComic();
        } else if(v.getId() == btnViewDelete.getId()) {
            //Delete the comic
            deleteComic();
        }
    }

    public void onButtonPressed(Uri uri) {
        if(mListener != null) {
            mListener.onViewComicFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ViewComicFragmentInteractionListener) {
            mListener = (ViewComicFragmentInteractionListener) context;
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

    public interface ViewComicFragmentInteractionListener {
        void onViewComicFragmentInteraction(Uri uri);
    }

    private void loadComicData() {
        //Get comic id from main activity
        int comicID = mainActivity.ISSUE_ID;
        issueCheck(comicID);

        //Set main activity comic id to -1
        mainActivity.ISSUE_ID = -1;

        //Query db
        comic = dbHandler.getComicById(comicID);

        //Load text views
        txtViewTitle.setText(comic.getComicTitle());
        txtViewVolume.setText("Volume: " + comic.getComicVolume());
        txtViewIssue.setText("Issue: " + comic.getComicIssue());
        txtViewPublisher.setText(comic.getComicPublisherName());
        txtViewCoverPrice.setText(comic.getComicCoverPrice());
        txtViewCondition.setText(comic.getComicConditionText());
        txtViewFormat.setText(comic.getComicFormat().toString());
        txtViewNotes.setText(comic.getComicNotes());

        //ToDo: Cover image

        loadRecycler(comic.getComicCreators());

    }

    private void issueCheck(int id) {
        if(id < 0) {
            //Bad data or no id
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Something Bad Happened");
            builder.setMessage("Could not retrieve comic data properly. Please try again");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mainActivity.loadViewCollectionFragment();
                }
            });
            builder.create();
            builder.show();
        }
    }

    private void deleteComic() {
        //Prompt user for confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Comic?");
        builder.setMessage("Are you sure you want to delete this comic? This cannot be undone");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dbHandler.deleteComic(comic.getComicID())) {
                    //Success
                    Toast.makeText(getContext(), "Comic successfully deleted", Toast.LENGTH_SHORT).show();
                    mainActivity.loadViewCollectionFragment();
                } else {
                    //Failure
                    Toast.makeText(getContext(), "Comic not deleted successfully. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing needs to happen here
            }
        });
    }

    private void editComic() {
        //Put the comic ID back in the main activity holder
        mainActivity.ISSUE_ID = comic.getComicID();

        //Load the edit fragment
        mainActivity.loadEditComicFragment();
    }

    private void loadRecycler(List<Creator> creators) {
        viewCreatorAdapter = new ViewCreatorAdapter(creators);
        recyclerViewCreators.setAdapter(viewCreatorAdapter);
        recyclerViewCreators.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}