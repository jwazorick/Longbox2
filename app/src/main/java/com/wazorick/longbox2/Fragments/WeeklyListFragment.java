package com.wazorick.longbox2.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.wazorick.longbox2.Adapters.ViewWeeklyListAdapter;
import com.wazorick.longbox2.Database.DBHandler;
import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.WeeklyItem;
import com.wazorick.longbox2.R;
import com.wazorick.longbox2.Utils.DateUtils;
import com.wazorick.longbox2.Utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeeklyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyListFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private WeeklyListFragmentInteractionListener mListener;
    private Context mContext;
    private MainActivity mainActivity;

    private ImageView btnWeeklyAdd;
    private ImageView btnWeeklyTransfer;
    private ImageView btnWeeklyEdit;
    private ImageView btnWeeklyHome;
    private ImageView btnWeeklyClear;
    private ImageView btnWeeklyDelete;
    private RecyclerView recyclerWeeklyList;

    private List<WeeklyItem> weeklyItems;
    private DBHandler dbHandler;
    private ViewWeeklyListAdapter viewWeeklyListAdapter;

    public WeeklyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyListFragment.
     */
    public static WeeklyListFragment newInstance(String param1, String param2) {
        WeeklyListFragment fragment = new WeeklyListFragment();
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
        View view = inflater.inflate(R.layout.fragment_weekly_list, container, false);

        btnWeeklyAdd = view.findViewById(R.id.btnWeeklyAdd);
        btnWeeklyTransfer = view.findViewById(R.id.btnWeeklyTransfer);
        btnWeeklyEdit = view.findViewById(R.id.btnWeeklyEdit);
        btnWeeklyHome = view.findViewById(R.id.btnWeeklyHome);
        btnWeeklyClear = view.findViewById(R.id.btnWeeklyClear);
        btnWeeklyDelete = view.findViewById(R.id.btnWeeklyDelete);
        recyclerWeeklyList = view.findViewById(R.id.recyclerWeeklyList);

        btnWeeklyAdd.setOnClickListener(this);
        btnWeeklyTransfer.setOnClickListener(this);
        btnWeeklyEdit.setOnClickListener(this);
        btnWeeklyHome.setOnClickListener(this);
        btnWeeklyClear.setOnClickListener(this);
        btnWeeklyDelete.setOnClickListener(this);

        mainActivity = (MainActivity)requireActivity();
        weeklyItems = new ArrayList<>();
        dbHandler = new DBHandler(mContext);

        //Load the weekly list
        loadWeeklyList();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnWeeklyAdd.getId()) {
            //Add an item to your weekly list
            addNewWeeklyItem();
        } else if(v.getId() == btnWeeklyEdit.getId()) {
            //Edit selected items
            editWeeklyItems();
        } else if(v.getId() == btnWeeklyTransfer.getId()) {
            //Transfer selected items to your collection
            transferSelectedItems();
        } else if(v.getId() == btnWeeklyHome.getId()) {
            //Load the main fragment
            mainActivity.loadMainFragment();
        } else if(v.getId() == btnWeeklyClear.getId()) {
            //Clear any selections
            viewWeeklyListAdapter.clearAllCheckboxes();
            loadWeeklyList();
        } else if(v.getId() == btnWeeklyDelete.getId()) {
            //Delete selected items
            deleteSelectedItems();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onWeeklyListFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof WeeklyListFragmentInteractionListener) {
            mListener = (WeeklyListFragmentInteractionListener) context;
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

    public interface WeeklyListFragmentInteractionListener {
        void onWeeklyListFragmentInteraction(Uri uri);
    }

    private void loadWeeklyList() {
        weeklyItems = dbHandler.getAllWeeklyListItems();
        viewWeeklyListAdapter = new ViewWeeklyListAdapter(weeklyItems, mContext);
        recyclerWeeklyList.setAdapter(viewWeeklyListAdapter);
        recyclerWeeklyList.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void addNewWeeklyItem() {
        //Pop up the dialog
        //ToDo: Add the ability to add a publisher from here
        //ToDo: Refactor this to be better

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View weeklyListDialog = inflater.inflate(R.layout.dialog_add_weekly_list, null);

        final EditText editWeeklyDialogTitle = weeklyListDialog.findViewById(R.id.editWeeklyDialogTitle);
        final EditText editWeeklyDialogIssue = weeklyListDialog.findViewById(R.id.editWeeklyDialogIssue);
        final Spinner spnWeeklyDialogPublisher = weeklyListDialog.findViewById(R.id.spnWeeklyDialogPublisher);
        final EditText editWeeklyDialogDate = weeklyListDialog.findViewById(R.id.editWeeklyDialogDate);
        List<String> publishers = dbHandler.getAllPublishers();
        ArrayAdapter<String> pubAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, publishers);
        pubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWeeklyDialogPublisher.setAdapter(pubAdapter);

        //Now for the date display
        //https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editWeeklyDialogDate, myCalendar);
            }
        };

        editWeeklyDialogDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        builder.setTitle("Add Weekly Item");
        builder.setMessage("Please enter the following data");
        builder.setView(weeklyListDialog);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Add the new data
                if(!editWeeklyDialogTitle.getText().toString().equalsIgnoreCase("") && !editWeeklyDialogIssue.getText().toString().equalsIgnoreCase("") &&
                        !editWeeklyDialogDate.getText().toString().equalsIgnoreCase("")) {
                    WeeklyItem item = new WeeklyItem();
                    item.setComicTitle(editWeeklyDialogTitle.getText().toString());
                    item.setComicIssue(editWeeklyDialogIssue.getText().toString());
                    item.setComicPublisherName(spnWeeklyDialogPublisher.getSelectedItem().toString());
                    item.setDatePublished(myCalendar.getTimeInMillis());

                    if(dbHandler.addWeeklyItem(item)) {
                        ScreenUtils.showAddSuccessToast(mContext);
                        loadWeeklyList();
                    } else {
                        ScreenUtils.showAddFailureToast(mContext);
                    }
                } else {
                    ScreenUtils.showNotEnoughInfoToast(mContext);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void updateLabel(EditText editText, Calendar myCalendar) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    private void editWeeklyItems() {
        List<WeeklyItem> selectedItems = viewWeeklyListAdapter.getAllSelectedItems();

        if(selectedItems.size() == 0) {
            ScreenUtils.showNoItemSelectedToast(mContext);
            return;
        }

        for(final WeeklyItem item: selectedItems) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View weeklyListDialog = inflater.inflate(R.layout.dialog_add_weekly_list, null);

            final EditText editWeeklyDialogTitle = weeklyListDialog.findViewById(R.id.editWeeklyDialogTitle);
            final EditText editWeeklyDialogIssue = weeklyListDialog.findViewById(R.id.editWeeklyDialogIssue);
            final Spinner spnWeeklyDialogPublisher = weeklyListDialog.findViewById(R.id.spnWeeklyDialogPublisher);
            final EditText editWeeklyDialogDate = weeklyListDialog.findViewById(R.id.editWeeklyDialogDate);
            List<String> publishers = dbHandler.getAllPublishers();
            ArrayAdapter<String> pubAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, publishers);
            pubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnWeeklyDialogPublisher.setAdapter(pubAdapter);

            editWeeklyDialogTitle.setText(item.getComicTitle());
            editWeeklyDialogIssue.setText(item.getComicIssue());
            editWeeklyDialogDate.setText(DateUtils.convertLongToDateString(item.getDatePublished()));
            spnWeeklyDialogPublisher.setSelection(pubAdapter.getPosition(item.getComicPublisherName()));

            final Calendar myCalendar = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(editWeeklyDialogDate, myCalendar);
                }
            };

            editWeeklyDialogDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            builder.setTitle("Edit Weekly Item");
            builder.setView(weeklyListDialog);

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!editWeeklyDialogTitle.getText().toString().equalsIgnoreCase("") && !editWeeklyDialogIssue.getText().toString().equalsIgnoreCase("") &&
                            !editWeeklyDialogDate.getText().toString().equalsIgnoreCase("")) {
                        WeeklyItem update = new WeeklyItem();
                        update.setComicTitle(editWeeklyDialogTitle.getText().toString());
                        update.setComicIssue(editWeeklyDialogIssue.getText().toString());
                        update.setComicPublisherName(spnWeeklyDialogPublisher.getSelectedItem().toString());
                        update.setDatePublished(myCalendar.getTimeInMillis());
                        update.setWeeklyId(item.getWeeklyId());

                        //update
                        if(dbHandler.updateWeeklyItem(update)) {
                            ScreenUtils.showEditSuccessToast(mContext);
                        } else {
                            ScreenUtils.showEditFailureToast(mContext);
                        }
                    } else {
                        ScreenUtils.showNotEnoughInfoToast(mContext);
                    }
                    loadWeeklyList();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private void deleteSelectedItems() {
        final List<WeeklyItem> selectedItems = viewWeeklyListAdapter.getAllSelectedItems();

        if(selectedItems.size() == 0) {
            ScreenUtils.showNoItemSelectedToast(mContext);
            return;
        }

        //Show the confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete Selected Items?");
        builder.setMessage("Are you sure you want to delete ALL items? This cannot be undone");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dbHandler.deleteWeeklyListItems(selectedItems)) {
                    ScreenUtils.showDeletedSuccessToast(mContext);
                } else {
                    ScreenUtils.showDeleteFailureToast(mContext);
                }
                loadWeeklyList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void transferSelectedItems() {
        final List<WeeklyItem> selectedItems = viewWeeklyListAdapter.getAllSelectedItems();

        if(selectedItems.size() == 0) {
            ScreenUtils.showNoItemSelectedToast(mContext);
            return;
        }

        //Confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Transfer Selected Items?");
        builder.setMessage("Do you want to transfer ALL selected items to your collection?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dbHandler.transferWeeklyItems(selectedItems)) {
                    ScreenUtils.showTransferSuccessToast(mContext);
                } else {
                    ScreenUtils.showTransferFailureToast(mContext);
                }
                loadWeeklyList();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}