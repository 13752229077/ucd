package com.example.home.ui.stats;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.home.MainActivity;
import com.example.home.R;
import com.example.home.utility.sql.DBAssetHelper;

/**
 * Fragment displaying the map of Ireland which upon click of a county displays the number of homeless people
 * recorded over 3 months which is stored in a local database
 */
public class StatsFragment extends Fragment implements View.OnTouchListener {

    private TableRow row;
    private ImageView imageView;
    private DBAssetHelper dbAssetHelper;

    /**
     * Creating the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAssetHelper = MainActivity.dbAssetHelper;
        View root = inflater.inflate(R.layout.stats_fragment, container, false);
        ImageView imageView = root.findViewById(R.id.coloured_map);
        imageView.setOnTouchListener(this); //add listener to the coloured map hidden under the normal map

        row = root.findViewById(R.id.countyStats);

        //Unchecking bottom navigation bar and replacing logo with a header title
        MainActivity main = (MainActivity) getActivity();
        main.getSupportActionBar().setTitle(R.string.title_stats);
        main.uncheckNav();

        return root;
    }


    /**
     * Dealing with tapping of the map
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        imageView = v.findViewById(R.id.coloured_map);
        boolean handled = false;
        //Only select a county when the action is a tap
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            float[] eventXY = new float[] {event.getX() , event.getY()};

            //Inverting the coordinates with a Matrix
            Matrix invert = new Matrix();
            imageView.getImageMatrix().invert(invert);
            invert.mapPoints(eventXY);

            setCountyStatistics((int) eventXY[0], (int) eventXY[1]);
            handled = true;
        }
        return handled;
    }

    /**
     * Given a set of coordinates this method will get the colour of the pixel clicked on the coloured map so as to request the
     * correct information from our database for a given county
     * @param x
     * @param y
     */
    private void setCountyStatistics(int x, int y) {
        if (imageView != null ) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();

            //the bitmap scales with height so we need to modify x to get the correct coordinates on the bitmap to avoid issues on different devices
            x = (int) (x * (bh / (imageView.getHeight() * 1.0)));
            y = (int) (y * (bh / (imageView.getHeight() * 1.0)));

            //if coordinates are within the bitmap we get the colour of the pixel at those coordinates and get the statistics up
            if(0 <= x && x < bw && 0 <= y && y < bh) {
                int pixelValue = bitmap.getPixel(x, y);
                String hexColor = String.format("#%06X", (0xFFFFFF & pixelValue));
                setStatistics(hexColor.substring(1).toLowerCase());
            }
        }
    }

    /**
     * Given a hexadecimal colour in string format the method will request the relevant data from the database
     * and populate the TableRow within the stats_fragment.xml
     * @param hexColour
     */
    private void setStatistics(String hexColour){
        Cursor c = dbAssetHelper.getCountyFigures(dbAssetHelper.getCountyByColour(hexColour));
        if(c.getCount()>0) {
            //Gather the info from the database
            String[] data = new String[] {
                    c.getString(c.getColumnIndex("County")),
                    c.getString(c.getColumnIndex("June_19")),
                    c.getString(c.getColumnIndex("July_19")),
                    c.getString(c.getColumnIndex("August_19"))
            };

            //populate the rows within our TableRow
            TextView t;
            for(int i=0; i<4; i++) {
                t = (TextView) row.getVirtualChildAt(i);
                t.setText(data[i]);
            }
        }
    }
}
