package com.example.home.ui.contacts;

import android.app.Activity;

import android.text.util.Linkify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.home.R;

import java.util.List;

/** A custom adapter class to create and store a view containing all the
 * different TextViews that make up a service **/
public class ContactsAdapter extends ArrayAdapter<ServiceDetails> {

    /** inner-class that holds the TextViews of each service **/
    private static class ViewHolder {
        TextView titleText;
        TextView urlText;
        TextView phoneText;
        TextView countyText;
    }

    /**
     * Calls the super class
     * @param context
     * @param details
     */
    ContactsAdapter(Activity context, List<ServiceDetails> details) {
        super(context, R.layout.contacts_service, details);
    }

    /** This method instantiates the TextViews with their layout in contact_services.xml
     * then populates each with the details stored in ServiceDetails for the current position.
     * Linkify allows easy creation of clickable links for email, websites and phone numbers **/
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ServiceDetails sd = getItem(position);
        ViewHolder viewHolder;


        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view=inflater.inflate(R.layout.contacts_service, null,true);
            viewHolder.titleText = view.findViewById(R.id.service_name);
            viewHolder.urlText = view.findViewById(R.id.service_web);
            viewHolder.phoneText = view.findViewById(R.id.service_phone);
            viewHolder.countyText = view.findViewById(R.id.service_county);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.titleText.setText(sd.name);
        viewHolder.phoneText.setText(sd.phoneNumber);
        viewHolder.urlText.setText(sd.url);
        viewHolder.countyText.setText(sd.county);
        Linkify.addLinks(viewHolder.phoneText,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(viewHolder.urlText, Linkify.ALL);

        return view;
    }

}

