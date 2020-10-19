package com.wazorick.longbox2.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
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
import com.wazorick.longbox2.Utils.CameraHelper;
import com.wazorick.longbox2.Utils.EnumUtils;
import com.wazorick.longbox2.Utils.FileUtils;
import com.wazorick.longbox2.Utils.LongboxConstants;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditComicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditComicFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText editEditTitle;
    private EditText editEditVolume;
    private EditText editEditIssue;
    private EditText editEditCoverPrice;
    private EditText editEditNotes;
    private Spinner spnEditPublisher;
    private Spinner spnEditCondition;
    private Spinner spnEditFormat;
    private RecyclerView recyclerEditCreators;
    private ImageView imgEditCover;
    private ImageView btnEditPicture;
    private ImageView btnEditImage;
    private ImageView btnEditAddRow;
    private ImageView btnEditSave;
    private ImageView btnEditCancel;

    private MainActivity mainActivity;
    private DBHandler dbHandler;
    private Comic comic;
    private List<Creator> creatorList;
    private String publisher = "";
    private File cover = null;
    private Context mContext;

    private ArrayAdapter<String> publisherAdapter;
    private ArrayAdapter<String> conditionAdapter;
    private ArrayAdapter<String> formatAdapter;
    private AddCreatorAdapter addCreatorAdapter;

    private EditComicFragmentInteractionListener mListener;

    public EditComicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditComicFragment.
     */
    public static EditComicFragment newInstance(String param1, String param2) {
        EditComicFragment fragment = new EditComicFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_comic, container, false);

        editEditTitle = view.findViewById(R.id.editEditTitle);
        editEditVolume = view.findViewById(R.id.editEditVolume);
        editEditIssue = view.findViewById(R.id.editEditIssue);
        editEditCoverPrice = view.findViewById(R.id.editEditCoverPrice);
        editEditNotes = view.findViewById(R.id.editEditNotes);
        spnEditPublisher = view.findViewById(R.id.spnEditPublisher);
        spnEditCondition = view.findViewById(R.id.spnEditCondition);
        spnEditFormat = view.findViewById(R.id.spnEditFormat);
        recyclerEditCreators = view.findViewById(R.id.recyclerEditCreators);
        imgEditCover = view.findViewById(R.id.imgEditCover);
        btnEditPicture = view.findViewById(R.id.btnEditPicture);
        btnEditImage = view.findViewById(R.id.btnEditImage);
        btnEditAddRow = view.findViewById(R.id.btnEditAddRow);
        btnEditSave = view.findViewById(R.id.btnEditSave);
        btnEditCancel = view.findViewById(R.id.btnEditCancel);

        dbHandler = new DBHandler(getContext());
        mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;
        comic = dbHandler.getComicById(mainActivity.ISSUE_ID);

        btnEditPicture.setOnClickListener(this);
        btnEditImage.setOnClickListener(this);
        btnEditAddRow.setOnClickListener(this);
        btnEditSave.setOnClickListener(this);
        btnEditCancel.setOnClickListener(this);

        loadPublisherSpinner();
        loadConditionSpinner();
        loadFormatSpinner();
        loadComicInfo();

        spnEditPublisher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnEditPublisher.getSelectedItem().toString().equalsIgnoreCase(LongboxConstants.ADD_PUBLISHER_STRING)) {
                    //Popup for adding a new publisher
                    addNewPublisher();
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
        if(v.getId() == btnEditPicture.getId()) {
            //Take a picture of the cover again
            dispatchCameraIntent();
        } else if(v.getId() == btnEditImage.getId()) {
            //Find the picture on the phone
            //ToDo: This
        } else if(v.getId() == btnEditAddRow.getId()) {
            //Add a row to the recycler
            addCreatorRow();
        } else if(v.getId() == btnEditSave.getId()) {
            //Save the changes
            saveInfo();
        } else if(v.getId() == btnEditCancel.getId()) {
            cancelClicked();
        }
    }

    public void onButtonPressed(Uri uri) {
        if(mListener != null) {
            mListener.onEditComicFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof EditComicFragmentInteractionListener) {
            mListener = (EditComicFragmentInteractionListener) context;
            mContext = context;
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

    public interface EditComicFragmentInteractionListener {
        void onEditComicFragmentInteraction(Uri uri);
    }

    public void loadComicInfo() {
        editEditTitle.setText(comic.getComicTitle());
        editEditVolume.setText(comic.getComicVolume());
        editEditIssue.setText(comic.getComicIssue());
        editEditCoverPrice.setText(comic.getComicCoverPrice());
        editEditNotes.setText(comic.getComicNotes());

        spnEditPublisher.setSelection(publisherAdapter.getPosition(comic.getComicPublisherName()));
        spnEditCondition.setSelection(conditionAdapter.getPosition(comic.getComicConditionText()));
        spnEditFormat.setSelection(formatAdapter.getPosition(comic.getComicFormat().toString()));
        imgEditCover.setImageBitmap(FileUtils.getCoverImage(comic.getComicCoverImage(), getActivity()));

        setupCreatorRecycler();
    }

    public void loadPublisherSpinner() {
        List<String> publishers = dbHandler.getAllPublishers();
        publishers.add(LongboxConstants.ADD_PUBLISHER_STRING);

        publisherAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, publishers);
        publisherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditPublisher.setAdapter(publisherAdapter);
    }

    public void loadConditionSpinner() {
        List<String> conditions = EnumUtils.getAllConditions();

        conditionAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditCondition.setAdapter(conditionAdapter);
    }

    public void loadFormatSpinner() {
        List<String> formats = EnumUtils.getAllFormats();

        formatAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, formats);
        formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditFormat.setAdapter(formatAdapter);
    }

    public void setupCreatorRecycler() {
        creatorList = comic.getComicCreators();
        addCreatorAdapter = new AddCreatorAdapter(creatorList);
        recyclerEditCreators.setAdapter(addCreatorAdapter);
        recyclerEditCreators.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void cancelClicked() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cancel Changes?");
        builder.setTitle("Cancelling with lose any changes that have been made. Are you sure you want to cancel?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Load the main fragment
                mainActivity.loadMainFragment();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing happens
            }
        });
        builder.create();
        builder.show();
    }

    public void addCreatorRow() {
        if(creatorList.size() >= LongboxConstants.MAX_NUM_CREATORS) {
            Toast.makeText(getContext(), "Cannot add any more creators", Toast.LENGTH_SHORT).show();
            return;
        }

        creatorList.add(new Creator());
        addCreatorAdapter.notifyItemInserted(creatorList.size());
        recyclerEditCreators.scrollToPosition(creatorList.size());
    }

    public void saveInfo() {
        //Check if everything needed is there
        if(!minimumRequirements()) {
            Toast.makeText(getContext(), "You need at least a title and issue number", Toast.LENGTH_SHORT).show();
            return;
        }

        updateComic();

        //Send the data to the DB Handler
        if(dbHandler.updateComic(comic)) {
            Toast.makeText(getContext(), "Comic Updated", Toast.LENGTH_SHORT).show();
            mainActivity.loadMainFragment();
        } else {
            Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean minimumRequirements() {
        boolean success = false;

        if(!editEditTitle.getText().toString().equalsIgnoreCase("")) {
            if(!editEditIssue.getText().toString().equalsIgnoreCase("")) {
                success = true;
            }
        }
        return success;
    }

    private void updateComic() {
        comic.setComicPublisherID(-1);
        comic.setComicConditionID(-1);

        comic.setComicTitle(editEditTitle.getText().toString());
        comic.setComicVolume(editEditVolume.getText().toString());
        comic.setComicIssue(editEditIssue.getText().toString());
        comic.setComicPublisherName(spnEditPublisher.getSelectedItem().toString());
        comic.setComicCoverPrice(editEditCoverPrice.getText().toString());
        comic.setComicConditionText(spnEditCondition.getSelectedItem().toString());
        comic.setComicFormat(EnumUtils.getFormatFromString(spnEditFormat.getSelectedItem().toString()));
        comic.setComicNotes(editEditNotes.getText().toString());
        comic.setComicCoverImage(cover.getName());

        comic.setComicCreators(creatorList);
    }

    private void addNewPublisher() {
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
                spnEditPublisher.setSelection(publisherAdapter.getPosition(publisher));
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

    private void dispatchCameraIntent() {
        //Check for camera permission
        mainActivity.checkCameraPermission();
        mainActivity.checkCameraPermission();

        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getContext(), "Longbox does not have permission to access camera", Toast.LENGTH_SHORT).show();
            return;
        }

        if(CameraHelper.checkIfCameraAvailable(mContext)) {
            Intent coverIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(coverIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
                try {
                    cover = CameraHelper.createImage(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(cover != null) {
                    Uri photoUri = FileProvider.getUriForFile(mContext, LongboxConstants.LONGBOX_AUTHORITY, cover);
                    coverIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(coverIntent, LongboxConstants.REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LongboxConstants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                //Compress image
                CameraHelper.compressImage(cover.getAbsolutePath());
                Uri imageUri = Uri.fromFile(new File(cover.getAbsolutePath()));
                Bitmap bitmap = BitmapFactory.decodeFile(cover.getAbsolutePath());
                bitmap = CameraHelper.rotateBitmapIfNecessary(bitmap);
                CameraHelper.saveImage(bitmap, cover.getAbsolutePath());
                imgEditCover.setImageURI(imageUri);
                imgEditCover.refreshDrawableState();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}