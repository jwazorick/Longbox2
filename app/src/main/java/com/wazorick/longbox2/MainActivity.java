package com.wazorick.longbox2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.wazorick.longbox2.Fragments.WishlistFragment;
import com.wazorick.longbox2.R;
import com.wazorick.longbox2.Fragments.AddComicFragment;
import com.wazorick.longbox2.Fragments.EditComicFragment;
import com.wazorick.longbox2.Fragments.MainFragment;
import com.wazorick.longbox2.Fragments.SearchFragment;
import com.wazorick.longbox2.Fragments.ViewCollectionFragment;
import com.wazorick.longbox2.Fragments.ViewComicFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentInteractionListener, AddComicFragment.AddComicFragmentInteractionListener, ViewCollectionFragment.ViewCollectionFragmentInteractionListener,
        ViewComicFragment.ViewComicFragmentInteractionListener, EditComicFragment.EditComicFragmentInteractionListener, SearchFragment.SearchFragmentInteractionListener, WishlistFragment.WishlistFragmentInteractionListener {

    public int ISSUE_ID = -1;
    public int VIEW_COLLECTION_SCROLL_TO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null){
            fragment = new MainFragment();
            manager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    //Buttons created here: https://www.clickminded.com/button-generator/
    //Roboto font, Size 26, 250 width, Fully curved sides, Pyramid gradient, Top and bottom color #15d798, Center color #073763

    public void checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 1);
        }
    }

    //******************************************************************************************************
    public void loadMainFragment() {
        MainFragment mainFragment = new MainFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mainFragment).addToBackStack(null).commit();
    }

    public void loadAddComicFragment() {
        AddComicFragment addComicFragment = new AddComicFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, addComicFragment).addToBackStack(null).commit();
    }

    public void loadViewCollectionFragment() {
        ViewCollectionFragment viewCollectionFragment = new ViewCollectionFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, viewCollectionFragment).addToBackStack(null).commit();
    }

    public void loadViewComicFragment() {
        ViewComicFragment viewComicFragment = new ViewComicFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, viewComicFragment).addToBackStack(null).commit();
    }

    public void loadEditComicFragment() {
        EditComicFragment editComicFragment = new EditComicFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, editComicFragment).addToBackStack(null).commit();
    }

    public void loadSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, searchFragment).addToBackStack(null).commit();
    }

    public void loadWishlistFragment() {
        WishlistFragment wishlistFragment = new WishlistFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, wishlistFragment).addToBackStack(null).commit();
    }

    //******************************************************************************************************
    @Override
    public void onMainFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAddComicFragmentInteraction(Uri uri) {

    }

    @Override
    public void onViewCollectionFragmentInteraction(Uri uri) {

    }

    @Override
    public void onViewComicFragmentInteraction(Uri uri) {

    }

    @Override
    public void onEditComicFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSearchFragmentInteraction(Uri uri) {

    }

    @Override
    public void onWishlistFragmentInteraction(Uri uri) {

    }
}
