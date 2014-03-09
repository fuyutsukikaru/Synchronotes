package com.fuyutsukikaru.synchronotes.notes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by soomin on 3/8/14.
 */
public class LoginActivity extends Activity {
    public static final String PREFS_NAME = "MyPrefs";

    static String firebaseurl = "https://syncnotes.firebaseio.com/";
    Firebase fb = new Firebase(firebaseurl);

    // UI elements
    EditText username;
    EditText password;
    Button login;
    Button signup;

    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

        login.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login(username.getText().toString(), password.getText().toString());
                }
            }
        );

        final SignUpFragment sigfrag = SignUpFragment.newInstance();
        /*FragmentManager fragman = getFragmentManager();
        fragman.beginTransaction().hide(sigfrag).commit();*/

        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.loginlayout, sigfrag)
                .hide(sigfrag)
                .setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down)
                .addToBackStack(null)
                .commit();

        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTitle = "Sign Up";
                        restoreActionBar();
                        fragmentManager.beginTransaction().show(sigfrag).commit();
                        //setContentView(R.layout.fragment_signup);
                    }
                }
        );
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    protected void login(String username, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Username and Password cannot be blank.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            alert.show();
            return;
        }

        /*final boolean[] correct = {false};
        final Object here = this;

        Firebase userRef = fb.child("users").child(username);
        System.out.println("Username is " + username);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                System.out.println("Here I am!");
                if (value == null) {
                    System.out.println("Here 1");
                    correct[0] = false;
                }
                else {
                    correct[0] = true;
                    System.out.println("Username correct? " + correct[0]);
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        System.out.println("Username correct? " + correct[0]);
        if (!correct[0]) {
            System.out.println("Here 3");
            AlertDialog.Builder userError = new AlertDialog.Builder(this);
            userError.setMessage("Username is incorrect.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog passAlert = userError.create();
            passAlert.show();
            return;
        }*/

        final String passcopy = password;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = prefs.edit();

        // Close the activity
        final Intent i = new Intent(this, MainActivity.class);

        System.out.println("Username is: " + username + ". Password is: " + password);
        Firebase passRef = fb.child("users/" + username + "/password");
        passRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Password is " + snapshot.getValue());
                if (passcopy.equals(snapshot.getValue())) {
                    System.out.println("Here 4");
                    editor.putBoolean("loggedIn", true);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        System.out.println("Here 5");
        AlertDialog.Builder passError = new AlertDialog.Builder(this);
        System.out.println("Password was wrong!");
        passError.setMessage("Username or Password is incorrect.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog passAlert = passError.create();
        passAlert.show();
        return;
    }

    public static class SignUpFragment extends Fragment {
        /**
         * Returns a new instance of this fragment
         */
        /*private LayoutInflater mInflate;
        private ViewGroup mViews;*/

        public static SignUpFragment newInstance() {
            SignUpFragment fragment = new SignUpFragment();
            return fragment;
        }

        public SignUpFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            /*mInflate = inflater;
            mViews = container;*/
            View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
            //rootView.setVisibility(View.VISIBLE);
            return rootView;
        }

        /*@Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
            rootView.setVisibility(View.VISIBLE);
        }*/
        /*@Override
        public void onActivityCreated(Bundle savedInstanceState) {
            View rootView = mInflate.inflate(R.layout.fragment_signup, mViews, false);
            rootView.setVisibility(View.VISIBLE);
        }*/
    }

    protected void signup(String username, String password, String pass2) {
        if (!password.equals(pass2)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Passwords do not match.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        final String usercpy = username;
        final String passcpy = password;
        final boolean correct[] = {false};

        Firebase userRef = fb.child("users").child(username);
        System.out.println("Username is " + username);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                System.out.println("Here I am!");
                if (value == null) {
                    System.out.println("Here 1");
                    Firebase userRef = fb.child("users/" + usercpy);
                    userRef.child("password").setValue(passcpy);
                }
                else {
                    correct[0] = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        if (correct[0]) {
            System.out.println("Here 3");
            AlertDialog.Builder userError = new AlertDialog.Builder(this);
            userError.setMessage("Username is already taken.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog passAlert = userError.create();
            passAlert.show();
            return;
        }
    }
}
