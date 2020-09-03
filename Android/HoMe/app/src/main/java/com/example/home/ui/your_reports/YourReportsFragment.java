package com.example.home.ui.your_reports;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.home.MainActivity;
import com.example.home.R;
import com.example.home.utility.sql.DBHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A fragment responsible for displaying the reports that the user submitted in a structure format
 */
public class YourReportsFragment extends Fragment {

    private TableLayout reports;

    /**
     * Creating the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.your_reports_fragment, container, false);

        //Ensuring the bottom navigation bar is unchecked and header title is set
        MainActivity main = (MainActivity) getActivity();
        main.getSupportActionBar().setTitle(R.string.title_yourreports);
        main.uncheckNav();

        reports = root.findViewById(R.id.reportTable);

        //fill the rows of the view with entries from the local database
        fillRows(MainActivity.sql.getAllEntries());

        return root;
    }

    /**
     * Populates the rows in the fragment's TableLayout
     * @param output
     */
    private void fillRows(ArrayList<DBHelper.Entry> output) {
        int i=0;
        TableRow row;
        TextView[] columns = new TextView[4];

        //setting up the layout for rows/columns
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        tableLayoutParams.setMargins(2, 0, 0, 2);

        int pad = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int[] rowColours = new int[2];
        rowColours[0] = Color.parseColor("#82c5fa");
        rowColours[1] = Color.WHITE;


        //for each entry we add a row to the table
        for(DBHelper.Entry entry : output) {
            //creating and configuring the row
            row = new TableRow(getContext());
            row.setId(i++);
            row.setPadding(pad,pad,pad,pad);
            row.setLayoutParams(tableLayoutParams);
            row.setBackgroundColor(rowColours[i%2]);

            //for each row we add 4 columns corresponding to the information stored in the database
            for(int j=0; j<4; j++) {
                //creating and configuring the 4 TextViews
                columns[j] = new TextView(getContext());
                columns[j].setLayoutParams(new TableRow.LayoutParams(j));
                columns[j].setPadding(pad, 0, pad, 0);
                columns[j].setTextSize(18);
                columns[j].setGravity(Gravity.CENTER);
            }

            //set the text of each of the columns in the row
            columns[0].setText(getLocation(entry.getLatitude(), entry.getLongitude()));
            columns[1].setText(String.valueOf(entry.getPeople()));
            columns[2].setText(entry.getSheltered());
            columns[3].setText(entry.getDescription());
            columns[3].setGravity(Gravity.START);

            //add each column to the TableRow view
            for(int j=0; j<4; j++) {
                row.addView(columns[j]);
            }

            //add the row to the TableLayout view
            reports.addView(row);
        }
    }

    /**
     * A method which translates geo-coordinates into a county if possible
     * @param latitude
     * @param longitude
     * @return
     */
    private String getLocation(String latitude, String longitude){

        //use geocoder to get extract a county from the coordinates
        String location = "";
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            if (addresses.size() > 0) {
                location = addresses.get(0).getLocality();
                if(location == null) {
                    location = latitude + ",\n" + longitude;
                }

                Log.v("YOUR_REPORTS", latitude+ " " +longitude + " => " + location);
            }
        }
        catch (IOException e) {
            Log.d("YOUR_REPORTS", e.getMessage());
        }

        return location;
    }

    /**
     * Ensure that the navigation is unchecked and header title is changed when fragment is resumed
     */
    public void onResume() {
        ((MainActivity) getActivity()).uncheckNav();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_yourreports);
        super.onResume();
    }

    public void onPause(){
        super.onPause();
    }

}
