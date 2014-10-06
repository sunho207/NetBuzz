package edu.gatech.sunho207.netbuzz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sunho207 on 10/1/14.
 */
public class Home extends Fragment {

    private ParseUser user = ParseUser.getCurrentUser();
    private TextView nameView;
    private TextView dateView;
    private String firstName;
    private String lastName;
    private String date;
    private View view;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        nameView = (TextView) view.findViewById(R.id.home_nameTextView);
        dateView = (TextView) view.findViewById(R.id.home_dateTextView);
        updateAll();
        return view;
    }

    public void updateAll() {
        setName();
        setDate();
        setPicture();
    }

    public void setName() {
        firstName = user.getString("firstName").toString();
        lastName = user.getString("lastName").toString();
        nameView.setText(firstName + " " + lastName);
    }

    public void setDate() {
        String weekDay;
        String month;
        int day;
        Calendar c = Calendar.getInstance();
        weekDay = new SimpleDateFormat("EEEE", Locale.US).format(c.getTime());
        month = new SimpleDateFormat("MMMM", Locale.US).format(c.getTime());
        day = c.get(Calendar.DAY_OF_MONTH);
        date = weekDay + ", " + month + " " + day;
        dateView.setText(date);
    }

    public void setPicture() {
        ParseFile image = user.getParseFile("profilePicture");
        final ParseImageView pictureImage = (ParseImageView) view.findViewById(R.id.home_propic);
        pictureImage.setParseFile(image);
        pictureImage.loadInBackground(new GetDataCallback() {
            @Override
                public void done(byte[] bytes, com.parse.ParseException e) {
            }
        });
    }
}