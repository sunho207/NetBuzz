package edu.gatech.sunho207.netbuzz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.Date;


public class Signup extends Activity {

    private EditText username;
    private EditText password;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private Spinner gender;
    private Spinner year;
    private DatePicker birthday;
    private Spinner major;
    private Button submit;
    private Button upload;
    private static final int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText) findViewById(R.id.signup_username);
        password = (EditText) findViewById(R.id.signup_password);
        firstName = (EditText) findViewById(R.id.signup_firstName);
        lastName = (EditText) findViewById(R.id.signup_lastName);
        email = (EditText) findViewById(R.id.signup_email);
        submit = (Button) findViewById(R.id.signup_submit);
        upload = (Button) findViewById(R.id.signup_upload);
        gender = (Spinner) findViewById(R.id.signup_gender);
        year = (Spinner) findViewById(R.id.signup_year);
        major = (Spinner) findViewById(R.id.signup_major);
        birthday = (DatePicker) findViewById(R.id.signup_birthday);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final Context context = view.getContext();
                ParseUser user = new ParseUser();
                if (canSet()) {
                    user.setUsername(username.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setEmail(email.getText().toString() + "@gatech.edu");
                    user.put("firstName", firstName.getText().toString());
                    user.put("lastName", lastName.getText().toString());
                    if (gender.getSelectedItemPosition() == 0) {
                        user.put("gender", true);
                    } else {
                        user.put("gender", false);
                    }
                    user.put("year", year.getSelectedItemPosition() + 1);
                    user.put("major", String.valueOf(major.getSelectedItem()));
                    user.put("birthday", new Date(birthday.getYear() - 1900, birthday.getMonth(), birthday.getDayOfMonth()));

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Congratulations");
                                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                builder.setMessage("Congratulations! Your account has been created! Please verify your email to continue.");
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Problem with signing up");
                                builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                switch (e.getCode()) {
                                    case 202:
                                        builder.setMessage("Username has already been taken");
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        break;
                                    case 203:
                                        builder.setMessage("Email has already been taken.");
                                        alertDialog = builder.create();
                                        alertDialog.show();
                                        break;
                                }
                            }
                        }
                    });
                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public Boolean canSet() {
        Boolean result = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Problem with signing up");
        builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        if (username.getText().toString().length() < 6) {
            builder.setMessage("Please enter a valid username that is at least 6 characters");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            result = false;
        } else if (password.getText().toString().length() < 6) {
                builder.setMessage("Please enter a valid password that is at least 6 characters.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                result = false;
        } else if (firstName.getText().toString().length() == 0
                || lastName.getText().toString().length() == 0
                || email.getText().toString().length() == 0) {
            builder.setMessage("Please complete all fields.");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            result = false;
        }
        return result;
    }
}
