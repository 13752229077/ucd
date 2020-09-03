package com.example.home.ui.privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.home.MainActivity;
import com.example.home.R;

import java.util.Objects;

/**
 * Fragment to display some privacy information about how the data is stored etc.
 */
public class PrivacyFragment extends Fragment {

    /**
     * Creating the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.privacy_fragment, container, false);
        final ListView listView = root.findViewById(R.id.privacy);
        String[] privacy = getResources().getStringArray(R.array.privacy);

        //using an array adapter to fill the listView within the fragment with string array from strings.xml
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_1,
                privacy
        );

        listView.setAdapter(arrayAdapter);

        //Ensuring bottom navigation bar is unchecked and replacing logo with a Header title
        MainActivity main = (MainActivity) getActivity();
        main.getSupportActionBar().setTitle(R.string.title_privacy);
        main.uncheckNav();

        return root;
    }

}
