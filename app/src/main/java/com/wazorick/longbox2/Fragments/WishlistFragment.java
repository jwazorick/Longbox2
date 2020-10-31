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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wazorick.longbox2.Adapters.ViewWishlistAdapter;
import com.wazorick.longbox2.Database.DBHandler;
import com.wazorick.longbox2.MainActivity;
import com.wazorick.longbox2.Objects.WishlistItem;
import com.wazorick.longbox2.R;
import com.wazorick.longbox2.Utils.ScreenUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private WishlistFragmentInteractionListener mListener;

    private TextView txtWishlistItems;
    private ImageView btnWishlistAdd;
    private ImageView btnWishlistTransfer;
    private ImageView btnWishlistEdit;
    private ImageView btnWishlistDelete;
    private ImageView btnWishlistHome;
    private ImageView btnWishlistClear;
    private RecyclerView recyclerWishlist;

    private List<WishlistItem> wishlistItemList;
    private DBHandler dbHandler;
    private Context mContext;
    private MainActivity mainActivity;
    private ViewWishlistAdapter viewWishlistAdapter;

    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishlistFragment newInstance(String param1, String param2) {
        WishlistFragment fragment = new WishlistFragment();
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
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        txtWishlistItems = view.findViewById(R.id.txtWishlistNumber);
        btnWishlistAdd = view.findViewById(R.id.btnWishlistAdd);
        btnWishlistTransfer = view.findViewById(R.id.btnWishlistTransfer);
        btnWishlistEdit = view.findViewById(R.id.btnWishlistEdit);
        btnWishlistDelete = view.findViewById(R.id.btnWishlistDelete);
        btnWishlistHome = view.findViewById(R.id.btnWishlistHome);
        btnWishlistClear = view.findViewById(R.id.btnWishlistClear);
        recyclerWishlist = view.findViewById(R.id.recyclerWishlist);

        btnWishlistAdd.setOnClickListener(this);
        btnWishlistTransfer.setOnClickListener(this);
        btnWishlistEdit.setOnClickListener(this);
        btnWishlistDelete.setOnClickListener(this);
        btnWishlistHome.setOnClickListener(this);
        btnWishlistClear.setOnClickListener(this);

        dbHandler = new DBHandler(mContext);
        wishlistItemList = null;
        mainActivity = (MainActivity)getActivity();

        //Load wishlist items
        loadWishlist();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnWishlistHome.getId()) {
            //Load the home fragment
            mainActivity.loadMainFragment();
        } else if(v.getId() == btnWishlistTransfer.getId()) {
            //Transfer the selected wishlist item(s) to the collection
            transferWishlistItem();
        } else if(v.getId() == btnWishlistEdit.getId()) {
            //Edit the selected item(s)
            editWishlistItem();
        } else if(v.getId() == btnWishlistDelete.getId()) {
            //Delete the selected item(s)
            deleteWishlistItem();
        } else if(v.getId() == btnWishlistClear.getId()) {
            //Clear any selections
            clearSelectedItems();
        } else if(v.getId() == btnWishlistAdd.getId()) {
            //Add a new item
            addWishlistItem();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onWishlistFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof WishlistFragmentInteractionListener) {
            mListener = (WishlistFragmentInteractionListener) context;
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

    public interface WishlistFragmentInteractionListener {
        void onWishlistFragmentInteraction(Uri uri);
    }

    public void loadWishlist() {
        wishlistItemList = dbHandler.getAllWishlistItems();

        //Update the number of items displayed in the text view
        txtWishlistItems.setText(mContext.getString(R.string.wishlist_items, Integer.toString(wishlistItemList.size())));

        //Load into adapter
        viewWishlistAdapter = new ViewWishlistAdapter(wishlistItemList, mContext);
        recyclerWishlist.setAdapter(viewWishlistAdapter);
        recyclerWishlist.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void addWishlistItem() {
        //Pop up the dialog
        //https://stackoverflow.com/questions/27755767/adding-multiple-edit-texts-to-an-alert-dialog/27755802
        //ToDo: add the ability to add a publisher from the dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View wishlistDialog = inflater.inflate(R.layout.dialog_add_wishlist, null);

        final EditText editWishlistDialogTitle = wishlistDialog.findViewById(R.id.editWishlistDialogTitle);
        final EditText editWishlistDialogIssue = wishlistDialog.findViewById(R.id.editWishlistDialogIssue);
        final Spinner spnWishlistDialogPublisher = wishlistDialog.findViewById(R.id.spnWishlistDialogPublisher);
        final Spinner spnWishlistDialogPriority = wishlistDialog.findViewById(R.id.spnWishlistDialogPriority);
        List<String> publishers = dbHandler.getAllPublishers();
        ArrayAdapter<String> pubAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, publishers);
        pubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWishlistDialogPublisher.setAdapter(pubAdapter);

        builder.setTitle("Add Wishlist Item");
        builder.setMessage("Please enter in the following information");
        builder.setView(wishlistDialog);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Add the new data
                if(!editWishlistDialogTitle.getText().toString().equalsIgnoreCase("") && !editWishlistDialogIssue.getText().toString().equalsIgnoreCase("")) {
                    //Looks like we're good
                    WishlistItem item = new WishlistItem();
                    item.setComicTitle(editWishlistDialogTitle.getText().toString());
                    item.setComicIssue(editWishlistDialogIssue.getText().toString());
                    item.setComicPublisherName(spnWishlistDialogPublisher.getSelectedItem().toString());
                    item.setWishlistPriority(spnWishlistDialogPriority.getSelectedItem().toString());

                    if(dbHandler.addWishlistItem(item)) {
                        //Success
                        ScreenUtils.showAddSuccessToast(mContext);
                        loadWishlist();
                    } else {
                        //failure
                        ScreenUtils.showAddFailureToast(mContext);
                    }
                } else {
                    //Not everything filled in
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
        builder.create().show();
    }

    public void editWishlistItem() {
        //Get the id of any selected items
        List<WishlistItem> editList = viewWishlistAdapter.getAllSelectedItems();
        if(editList.size() == 0) {
            ScreenUtils.showNoItemSelectedToast(mContext);
            return;
        }

        //In a loop, pop a dialog with the data for the user to update them
        for (final WishlistItem item: editList) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View wishlistDialog = inflater.inflate(R.layout.dialog_add_wishlist, null);

            final EditText editWishlistDialogTitle = wishlistDialog.findViewById(R.id.editWishlistDialogTitle);
            final EditText editWishlistDialogIssue = wishlistDialog.findViewById(R.id.editWishlistDialogIssue);
            final Spinner spnWishlistDialogPublisher = wishlistDialog.findViewById(R.id.spnWishlistDialogPublisher);
            final Spinner spnWishlistDialogPriority = wishlistDialog.findViewById(R.id.spnWishlistDialogPriority);
            List<String> publishers = dbHandler.getAllPublishers();
            ArrayAdapter<String> pubAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, publishers);
            pubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnWishlistDialogPublisher.setAdapter(pubAdapter);

            editWishlistDialogTitle.setText(item.getComicTitle());
            editWishlistDialogIssue.setText(item.getComicIssue());
            spnWishlistDialogPublisher.setSelection(pubAdapter.getPosition(item.getComicPublisherName()));

            builder.setTitle("Update Wishlist Item");
            builder.setView(wishlistDialog);

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Get new info
                    item.setComicTitle(editWishlistDialogTitle.getText().toString());
                    item.setComicIssue(editWishlistDialogIssue.getText().toString());
                    item.setWishlistPriority(spnWishlistDialogPriority.getSelectedItem().toString());
                    item.setComicPublisherName(spnWishlistDialogPublisher.getSelectedItem().toString());

                    //Update database
                    if(dbHandler.updateWishlistItem(item)) {
                        ScreenUtils.showEditSuccessToast(mContext);
                    } else {
                        ScreenUtils.showEditFailureToast(mContext);
                    }
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

        //After the edits are done, update the adapter
        viewWishlistAdapter.clearAllCheckboxes();
        loadWishlist();
    }

    public void deleteWishlistItem() {
        //Get the ids of any selected items
        final List<WishlistItem> deleteList = viewWishlistAdapter.getAllSelectedItems();
        if(deleteList.size() == 0) {
            ScreenUtils.showNoItemSelectedToast(mContext);
            return;
        }

        //Warn the user these items will be deleted
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete items?");
        builder.setMessage("Are you sure you want to delete these " + deleteList.size() + " item(s)? This cannot be undone");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dbHandler.deleteWishlistItems(deleteList)) {
                    Toast.makeText(mContext, "Items deleted successfully", Toast.LENGTH_SHORT).show();
                    loadWishlist();
                } else {
                    Toast.makeText(mContext, "Some items not deleted. Please try again", Toast.LENGTH_SHORT).show();
                }
                loadWishlist();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewWishlistAdapter.clearAllCheckboxes();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void transferWishlistItem() {
        //Get the ids of any selected items
        final List<WishlistItem> transferList = viewWishlistAdapter.getAllSelectedItems();
        if(transferList.size() == 0) {
            ScreenUtils.showNoItemSelectedToast(mContext);
        }

        //Warn the user it will move the items from the wishlist to their regular collection
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Transfer to your collection?");
        builder.setMessage("Warning: This will move the following " + transferList.size() + " items from your wishlist to your collection. Proceed?");
        builder.setPositiveButton("Transfer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Call the transfer method
                if(dbHandler.transferWishlistItems(transferList)) {
                    ScreenUtils.showTransferSuccessToast(mContext);
                } else {
                    ScreenUtils.showTransferFailureToast(mContext);
                }

                //Update the adapter
                loadWishlist();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewWishlistAdapter.clearAllCheckboxes();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void clearSelectedItems() {
        //Clear selected items from the recycler
        viewWishlistAdapter.clearAllCheckboxes();
        loadWishlist();
    }
}