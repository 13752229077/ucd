package com.example.home.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.home.MainActivity;
import com.example.home.MyApplication;
import com.example.home.R;
import com.example.home.ui.location.AddressManager;
import com.example.home.utility.sql.DBAssetHelper;

import java.util.ArrayList;

/** This Fragment displays a list of homeless service contact details.
 *
 * A county is either selected from the dropdown list in spinner
 * or set via the device location if this has already been set in MyApplication.java.
 *
 * The database helper class DBAssetHelper returns the services in the selected county/counties.
 * The details are passed to ContactsAdapter.java to create a View containing all the services
 * which is displayed in a ListView below the spinner.
 *
 * **/

public class ContactsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private DBAssetHelper dbAssetHelper = MainActivity.dbAssetHelper;
    private ContactsAdapter contactsAdapter;
    private static String county = "";
    private ListView listView;

    /**
     * Creates the ContactsFragment view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // create contacts fragment
        View root = inflater.inflate(R.layout.contacts_fragment, container, false);
        // check for previously set location
        setAddress();
        // create spinner, set item selected listener, instantiate dropdown list and set adapter
        spinner = root.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.counties_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // instantiate listView
        listView = root.findViewById(R.id.contacts_list);

        if(!county.equals("")) {
            String item = county;
            int index=0;
            // find corresponding county in counties_array
            String [] counties = getContext().getResources().getStringArray(R.array.counties_array);
            while(index<counties.length){
                if(county.equals(counties[index])) {
                    // set spinner to that county
                    spinner.setSelection(index);
                    index=counties.length;
                }
                index++;
            }
            contactsAdapter = new ContactsAdapter(getActivity(), setServices(item));
            listView.setAdapter(contactsAdapter);
        }

        return root;
    }


    /** Sets the contact details for a user selected county or location based
     * @param adapterView The AdapterView where the selection happened - this
     * @param view The view within the AdapterView that was clicked - spinner
     * @param i The position of the spinner
     * **/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // if county not set in report fragment
        String item = adapterView.getItemAtPosition(i).toString();
        // if 1 of Dublin sub regions
        if(item.contains("¬"))
            item = item.substring(1);

        // Create a for each service returned by setServices()

        // Instantiate custom adapter to make TextViews using a list of ServiceDetails for item (county)
        contactsAdapter = new ContactsAdapter(getActivity(), setServices(item));
        listView.setAdapter(contactsAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onResume();
    }

    /** Creates a list of ServiceDetails each containing homeless service details by extracting
     * columns for each example returned from dbAssetHelper.getColumns().
     * @param item county to get the table of in dbAssetHelper
     * @return A List of service details **/
    private ArrayList<ServiceDetails> setServices(String item) {

        ArrayList<ServiceDetails> services2 = new ArrayList<>();

        Cursor cursor = dbAssetHelper.getColumns(item);

        do{
            ServiceDetails serviceDetails2 = new ServiceDetails(
                    cursor.getString(cursor.getColumnIndex("Name")).trim(),
                    cursor.getString(cursor.getColumnIndex("PhoneNum")).trim(),
                    cursor.getString(cursor.getColumnIndex("Website")).trim(),
                    cursor.getString(cursor.getColumnIndex("county_name")).trim()
            );
            services2.add(serviceDetails2);

        }while (cursor.moveToNext());
        return services2;
    }
    /** This method checks if the location has been set in the class MyApplication, if so,
     * the class AddressManager is used to find the county name from the longitude and latitude **/
    private void setAddress(){
        if(((MyApplication) getActivity().getApplication()).hasLocation()) {
            AddressManager addressManager = new AddressManager();
            double longitude = ((MyApplication) getActivity().getApplication()).getLongitude();
            double latitude = ((MyApplication) getActivity().getApplication()).getLatitude();
            county = addressManager.getAddress(getContext(), longitude, latitude);
        }
    }
}

