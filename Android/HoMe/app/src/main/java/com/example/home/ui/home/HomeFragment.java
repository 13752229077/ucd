package com.example.home.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.home.MainActivity;
import com.example.home.R;
import com.example.home.ui.report.ReportFragment;

/**
 * HomeFragment is the home screen which the app opens upon start displaying information about the app as well as the report button
 * keeping the main function of the app in the app's primary screen
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private MainActivity main;

    /**
     * Creating the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        //Using a webview to display the home screen paragraph so as to allow for styling it using justification
        WebView webView = root.findViewById(R.id.home_web_view);
        webView.setVerticalScrollBarEnabled(false);
        webView.loadData(getString(R.string.home_msg), "text/html; charset=utf-8", "utf-8");

        //adding the report button and its listener
        Button reportBtn = root.findViewById(R.id.btn_report);
        reportBtn.setOnClickListener(this);

        return root;
    }

    /**
     * When this fragment is resumed the navigation should be set to Home again and the Logo should be visible in the Top bar
     * instead of a heading
     */
    public void onResume() {
        main.checkNav(R.id.navigation_home);
        main.getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onResume();
    }

    /**
     * Implementing the fragment swap for when Report button is clicked
     * It will replace the current fragment with the Report Fragment
     * @param v
     */
    public void onClick(View v) {
        int containerId = R.id.nav_host_fragment;

        if (v.getId() == R.id.btn_report) {
            getFragmentManager().beginTransaction()
                    .replace(containerId, new ReportFragment())
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }
}