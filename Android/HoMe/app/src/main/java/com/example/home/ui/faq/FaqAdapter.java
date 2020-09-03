package com.example.home.ui.faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.home.R;

import java.util.List;

/**
 * Custom adapter for a list view to display the FAQ questions and answers in a neat way
 * Questions are set to be in bold while answers are set to normal style
 * The following example was followed to implement the adapter: https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
 */
public class FaqAdapter extends ArrayAdapter<QA> {

    /**
     * A ViewHolder class which will store our view to avoid unnecessary overheads
     */
    private static class ViewHolder {
        TextView question;
        TextView answer;
    }

    /**
     * Constructor which simply calls the superclass
     * @param faq
     * @param context
     */
    FaqAdapter(List<QA> faq, Context context) {
        super(context, R.layout.faq_row_item, faq);
    }

    /**
     * Overriden method which populates the questions and answers using the list passed into constructor earlier
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        QA qa = getItem(position);
        ViewHolder viewHolder;

        final View result;

        //if view is not assigned create a new view holder to store the view
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.faq_row_item, parent, false);
            viewHolder.question = convertView.findViewById(R.id.question);
            viewHolder.answer = convertView.findViewById(R.id.answer);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else { //otherwise retrieve a previously stored view
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        //set the text for question answer of current viewHolder
        assert qa != null;
        viewHolder.question.setText(qa.question);
        viewHolder.answer.setText(qa.answer);

        return result;
    }
}

/**
 * Question-Answer class to simplify the work in the above class
 */
class QA {
    String question;
    String answer;

    QA(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}