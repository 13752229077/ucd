package com.example.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.home.utility.sql.DBAssetHelper;
import com.example.home.utility.sql.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The MainActivity which contains all our fragments at runtime
 */
public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    public static DBHelper sql;
    public static DBAssetHelper dbAssetHelper;
    Dialog myDialog;
    String phoneNo = "087-241-FAKE"; //note FAKE will be translated using numpad by phone
    String email = "home-rough-sleepers@fake.com";

    /**
     * Creating the view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        sql = new DBHelper(this); //reports database
        dbAssetHelper = new DBAssetHelper(this); //stats and contacts database

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_contacts, R.id.navigation_tips, R.id.navigation_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //setting up a logo in the top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.padded_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        myDialog = new Dialog(this);
    }

    /**
     * Unchecks the currently selected navigation bar item
     */
    public void uncheckNav() {
        int id = navView.getSelectedItemId();
        Menu menu = navView.getMenu();

        menu.setGroupCheckable(0, true, false);
        menu.findItem(id).setChecked(false);
        menu.setGroupCheckable(0, true, true);
    }

    /**
     * Selects the navigation bar item based on current menu
     * @param id
     */
    public void checkNav(int id) {
        Menu menu = navView.getMenu();
        menu.setGroupCheckable(0, true, false);
        menu.findItem(id).setChecked(true);
        menu.setGroupCheckable(0, true, true);
    }

    /**
     * This method displays the contact dialog when Contact Us button is clicked in the More tab.
     *     Buttons in charge of intents are hidden if the intents cannot be handled by the user's device.
     *     Listeners call methods which start the given intents.
     */
    public void showContactInfo() {
        myDialog.setContentView(R.layout.contact_us_popup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent emailIntent = (new Intent(Intent.ACTION_SENDTO)).setData(Uri.parse("mailto:"));
        Intent callIntent = (new Intent(Intent.ACTION_DIAL)).setData(Uri.parse("tel:" + phoneNo));

        if (emailIntent.resolveActivity(getPackageManager()) == null ||
                callIntent.resolveActivity(getPackageManager()) == null) {
            myDialog.getWindow().findViewById(R.id.btn_email).setVisibility(View.GONE);
            myDialog.getWindow().findViewById(R.id.btn_call).setVisibility(View.GONE);
        } else {
            Button emailBtn = myDialog.findViewById(R.id.btn_email);
            emailBtn.setOnClickListener(v -> composeEmail());

            Button callBtn = myDialog.findViewById(R.id.btn_call);
            callBtn.setOnClickListener(v -> dialNumber());
        }

        /*
        Dialog appears to have a fixed width that our xml can't modify.
        Found fix here: https://jimbaca.com/force-dialog-to-take-up-full-screen-width/
         */
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(myDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        myDialog.getWindow().setAttributes(params);


        myDialog.show();
    }

    /**
     * Starts an email intent.
     * This function assumes that the intent can be handled, and since we hide the buttons if it can't
     * be, by definition this method will only be called if they can be handled.
     */
    private void composeEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        startActivity(intent);
    }

    /**
     * Starts a dial intent (User gets to choose whether they would like to call or not).
     *     This function assumes that the intent can be handled, and since we hide the buttons if it can't
     *     be, by definition this method will only be called if they can be handled.
     *
     */
    private void dialNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
        startActivity(intent);
    }

    /**
     * Making the back button return to previous screen
     */
    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}
