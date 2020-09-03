package com.example.home.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.home.MainActivity;
import com.example.home.R;
import com.example.home.ui.faq.FaqFragment;
import com.example.home.ui.privacy.PrivacyFragment;
import com.example.home.ui.stats.StatsFragment;
import com.example.home.ui.your_reports.YourReportsFragment;

/**
 * Fragment responsible of a secondary menu with buttons leading to fragments which are not required in a bottom navigation
 */
public class MoreFragment extends Fragment implements View.OnClickListener {

    /**
     * Creating the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.more_fragment, container, false);

        //Adding the various buttons leading to different fragments
        //Statistics
        Button btn1 = root.findViewById(R.id.btn_stats);
        btn1.setOnClickListener(this);

        //FAQ
        Button btn2 = root.findViewById(R.id.btn_faq);
        btn2.setOnClickListener(this);

        //Privacy
        Button btn3 = root.findViewById(R.id.btn_privacy);
        btn3.setOnClickListener(this);

        //Contact Us
        Button btn4 = root.findViewById(R.id.btn_contactus);
        btn4.setOnClickListener(this);

        //Your Reports
        Button btn5 = root.findViewById(R.id.btn_yourreports);
        btn5.setOnClickListener(this);

        return root;
    }

    /**
     * Dealing with button clicks by swapping out the relevant fragments in
     * @param v
     */
    @Override
    public void onClick(View v) {
        Fragment f = null;
        int containerId = R.id.nav_host_fragment;

        switch (v.getId()) {
            case R.id.btn_stats:
                f = new StatsFragment();
                break;
            case R.id.btn_privacy:
                f = new PrivacyFragment();
                break;
            case R.id.btn_faq:
                f = new FaqFragment();
                break;
            case R.id.btn_contactus:
                ((MainActivity) getActivity()).showContactInfo();
                break;
            case R.id.btn_yourreports:
                if(MainActivity.sql.hasEntries()) {
                    f = new YourReportsFragment();
                }else{
                    Toast.makeText(getActivity(), "No previous reports detected", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }

        //performing the fragment transaction provided there is a fragment to be swapped in
        if (f != null) {
            getFragmentManager().beginTransaction()
                    .replace(containerId, f)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    /**
     * Ensuring logo is visible rather than a heading in the top bar
     */
    @Override
    public void onResume() {
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onResume();
    }
}