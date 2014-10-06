package edu.gatech.sunho207.netbuzz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Login extends Activity {

    private ImageButton loginButton;
    private Button signupButton;
    private EditText loginField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "ZMi35hiVUUUhRODPypgIY77cfCwMSNddNmXsoYj8", "FGxLRb7KfKReVZOQR6pEd9YuJJlSct5F9mA4swOP");
        ParseUser currentUser = ParseUser.getCurrentUser();
        setContentView(R.layout.activity_login);
        if (currentUser != null) {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();
        }
        getActionBar().setTitle("NetBuzz");
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        loginField = (EditText) findViewById(R.id.login);
        passwordField = (EditText) findViewById(R.id.password);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                ParseUser.logInInBackground(loginField.getText().toString(), passwordField.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent = new Intent(context, Main.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(e.getCode() + " Error");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            switch (e.getCode()) {
                                case 100:
                                    builder.setMessage("Connection failed. Please try again later.");
                                    break;
                                case 101:
                                    builder.setMessage("The username or password you have entered is incorrect. Please try again.");
                                    break;

                            }
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Signup.class);
                startActivity(intent);
            }
        });
    }
}
