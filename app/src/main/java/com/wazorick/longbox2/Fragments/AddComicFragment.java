package com.wazorick.longbox2.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wazorick.longbox2.Adapters.AddCreatorAdapter;
import com.wazorick.longbox2.Database.DBHandler;
import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.Comic;
import com.wazorick.longbox2.Objects.Creator;
import com.wazorick.longbox2.R;
import com.wazorick.longbox2.Utils.EnumUtils;
import com.wazorick.longbox2.Utils.LongboxConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddComicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddComicFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText editAddTitle;
    private EditText editAddVolume;
    private EditText editAddIssue;
    private Spinner spnAddPublisher;
    private EditText editAddCoverPrice;
    private Spinner spnAddCondition;
    private Spinner spnAddFormat;
    private EditText editAddNotes;
    private RecyclerView recyclerAddCreators;

    private ImageView btnAddCreator;
    private ImageView btnAddTakePicture;
    private ImageView btnAddFindImage;

    private ImageView btnAddSave;
    private ImageView btnAddMore;
    private ImageView btnAddCancel;

    private MainActivity mainActivity;
    private DBHandler dbHandler;
    private List<Creator> creatorList;
    private AddCreatorAdapter addCreatorAdapter;
    private String publisher = "";

    private ArrayAdapter<String> pubAdapter;
    private ArrayAdapter<String> conditionAdapter;
    private ArrayAdapter<String> formatAdapter;

    private TextureView.SurfaceTextureListener surfaceTextureListener;

    private AddComicFragmentInteractionListener mListener;

    public AddComicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddComicFragment.
     */
    public static AddComicFragment newInstance(String param1, String param2) {
        AddComicFragment fragment = new AddComicFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_comic, container, false);

        editAddTitle = view.findViewById(R.id.editAddTitle);
        editAddVolume = view.findViewById(R.id.editAddVolume);
        editAddIssue = view.findViewById(R.id.editAddIssue);
        spnAddPublisher = view.findViewById(R.id.spnAddPublisher);
        editAddCoverPrice = view.findViewById(R.id.editAddCoverPrice);
        spnAddCondition = view.findViewById(R.id.spnAddCondition);
        spnAddFormat = view.findViewById(R.id.spnAddFormat);
        editAddNotes = view.findViewById(R.id.editAddNotes);
        recyclerAddCreators = view.findViewById(R.id.recyclerAddCreators);

        btnAddCreator = view.findViewById(R.id.btnAddCreator);
        btnAddTakePicture = view.findViewById(R.id.btnAddTakePicture);
        btnAddFindImage = view.findViewById(R.id.btnAddFindImage);

        btnAddSave = view.findViewById(R.id.btnAddSave);
        btnAddMore = view.findViewById(R.id.btnAddMore);
        btnAddCancel = view.findViewById(R.id.btnAddCancel);

        mainActivity = (MainActivity)getActivity();
        dbHandler = new DBHandler(getContext());
        creatorList = new ArrayList<>();
        //publisher = "";

        btnAddTakePicture.setOnClickListener(this);
        btnAddFindImage.setOnClickListener(this);
        btnAddSave.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnAddCancel.setOnClickListener(this);
        btnAddCreator.setOnClickListener(this);

        loadPublisherSpinner();
        loadConditionSpinner();
        loadFormatSpinner();
        setUpCreatorRecycler();

        spnAddPublisher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnAddPublisher.getSelectedItem().toString().equalsIgnoreCase(LongboxConstants.ADD_PUBLISHER_STRING)) {
                    //Pop up to add a new publisher
                    showAddPublisherDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnAddSave.getId()) {
            //Add comic logic
            if(minimumRequirementsCheck()) {
                Comic comic = createComicObject();
                if(dbHandler.addComic(comic)) {
                    //Success
                    showSuccessToast();
                    mainActivity.loadMainFragment();
                } else {
                    //Failure
                    showFailureToast();
                }
            } else {
                notEnoughInfoDialog();
            }

        } else if(v.getId() == btnAddCancel.getId()) {
            //Cancel logic
            if(checkIfEmpty()) {
                //Nothing inputted. Just load the main fragment
                mainActivity.loadMainFragment();
            } else {
                //Confirm cancel
                cancelDialog();
            }
        } else if(v.getId() == btnAddTakePicture.getId()) {
            //Use camera to take pic of cover
            //ToDO: Camera interaction
        } else if(v.getId() == btnAddFindImage.getId()) {
            //Find a picture stored on the phone
            //ToDO: File navigation
        } else if(v.getId() == btnAddMore.getId()) {
            //Add the comic and refresh the page
            if(minimumRequirementsCheck()) {
                Comic comic = createComicObject();
                if(dbHandler.addComic(comic)) {
                    //Success
                    showSuccessToast();
                    mainActivity.loadAddComicFragment();
                } else {
                    //Failure
                    showFailureToast();
                }
            } else {
                notEnoughInfoDialog();
            }
        } else if(v.getId() == btnAddCreator.getId()) {
            //Add a new row in the recyclerview
            addCreatorRow();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAddComicFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddComicFragmentInteractionListener) {
            mListener = (AddComicFragmentInteractionListener) context;
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

    public interface AddComicFragmentInteractionListener {
        void onAddComicFragmentInteraction(Uri uri);
    }

    private void loadPublisherSpinner() {
        List<String> publishers = dbHandler.getAllPublishers();
        publishers.add(LongboxConstants.ADD_PUBLISHER_STRING);

        pubAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, publishers);
        pubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAddPublisher.setAdapter(pubAdapter);
    }

    private void loadConditionSpinner() {
        List<String> conditions = EnumUtils.getAllConditions();

        conditionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAddCondition.setAdapter(conditionAdapter);
    }

    private void loadFormatSpinner() {
        List<String> formats = EnumUtils.getAllFormats();
        formatAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, formats);
        formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAddFormat.setAdapter(formatAdapter);
    }

    private void setUpCreatorRecycler() {
        Creator blankCreator = new Creator();
        creatorList.add(blankCreator);
        addCreatorAdapter = new AddCreatorAdapter(creatorList);
        recyclerAddCreators.setAdapter(addCreatorAdapter);
        recyclerAddCreators.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private boolean minimumRequirementsCheck() {
        boolean reqsMet = false;

        if(!editAddTitle.getText().toString().equals("")) {
            if(!editAddIssue.getText().toString().equals("")) {
                reqsMet = true;
            }
        }
        return reqsMet;
    }

    private boolean checkIfEmpty() {
        boolean empty = false;

        if(editAddTitle.getText().toString().equals("")) {
            if(editAddVolume.getText().toString().equals("")) {
                if(editAddIssue.getText().toString().equals("")) {
                    if(editAddCoverPrice.getText().toString().equals("")) {
                        if(editAddNotes.getText().toString().equals("")) {
                            empty = true;
                        }
                    }
                }
            }
        }
        return empty;
    }

    private void cancelDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setMessage("Are you sure you want to cancel?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainActivity.loadMainFragment();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing needs to happen
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    private void notEnoughInfoDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setMessage("More information is needed before you can save");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    private Comic createComicObject() {
        Comic comic = new Comic();

        comic.setComicTitle(editAddTitle.getText().toString());
        comic.setComicVolume(editAddVolume.getText().toString());
        comic.setComicIssue(editAddIssue.getText().toString());
        comic.setComicPublisherName(spnAddPublisher.getSelectedItem().toString());
        comic.setComicCoverPrice(editAddCoverPrice.getText().toString());
        comic.setComicConditionText(spnAddCondition.getSelectedItem().toString());

        comic.setComicFormat(EnumUtils.getFormatFromString(spnAddFormat.getSelectedItem().toString()));
        //ToDo: Cover image
        comic.setComicCreators(addCreatorAdapter.getCreatorList());

        comic.setComicNotes(editAddNotes.getText().toString());

        return comic;
    }

    private void showSuccessToast() {
        Toast.makeText(getContext(), "Comic added successfully", Toast.LENGTH_SHORT).show();
    }

    private void showFailureToast() {
        Toast.makeText(getContext(), "Comic Not Added. Please Try Again", Toast.LENGTH_SHORT).show();
    }

    private void addCreatorRow() {
        if(creatorList.size() >= LongboxConstants.MAX_NUM_CREATORS) {
            Toast.makeText(getContext(), "Cannot add any more rows", Toast.LENGTH_SHORT).show();
            return;
        }

        creatorList.add(new Creator());
        int lastPosition = creatorList.size();
        addCreatorAdapter.notifyItemInserted(lastPosition);
        recyclerAddCreators.scrollToPosition(lastPosition);
    }

    private void showAddPublisherDialog() {
        //https://stackoverflow.com/questions/10903754/input-text-dialog-android
        //ToDo: Refactor this into a resusable static call
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final EditText pubName = new EditText(getContext());
        pubName.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(pubName);

        builder.setTitle("Add New Publisher");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                publisher = pubName.getText().toString();

                if(!publisher.isEmpty()) {
                    if(!publisher.equalsIgnoreCase("")) {
                        dbHandler.addPublisher(publisher);
                    }
                }
                loadPublisherSpinner();
                spnAddPublisher.setSelection(pubAdapter.getPosition(publisher));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
