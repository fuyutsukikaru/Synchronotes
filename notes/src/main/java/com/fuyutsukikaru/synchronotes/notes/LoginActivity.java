package com.fuyutsukikaru.synchronotes.notes;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

/**
 * Created by soomin on 3/8/14.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }
}
