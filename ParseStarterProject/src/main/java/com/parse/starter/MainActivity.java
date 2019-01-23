/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    EditText usernameText;
    EditText passwordText;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

  public void signUpClicked(View view) {
      usernameText = (EditText) findViewById(R.id.usernameText);
      passwordText = (EditText) findViewById(R.id.passwordText);

      // throw error when signup goes wrong, otherwise save the user in database
      if (usernameText.getText().toString().matches("") || passwordText.getText().toString().matches("")) {
          Toast.makeText(this, "Username and password required.", Toast.LENGTH_SHORT).show();
      } else {
          ParseUser user = new ParseUser();
          user.setUsername(usernameText.getText().toString());
          user.setPassword(passwordText.getText().toString());

          user.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                  if (e == null) {
                      showUserList();
                  } else {
                      Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              }
          });
      }
  }

  public void loginClicked(View view) {
      usernameText = (EditText) findViewById(R.id.usernameText);
      passwordText = (EditText) findViewById(R.id.passwordText);

      // throw toast if error occures on login, else go to user list
      if (usernameText.getText().toString().matches("") || passwordText.getText().toString().matches("")) {
          Toast.makeText(this, "Username and password required.", Toast.LENGTH_SHORT).show();
      } else {
          ParseUser.logInInBackground(usernameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {
                  if (e == null) {
                      showUserList();
                  } else {
                      Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              }
          });
      }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram");

    usernameText = (EditText) findViewById(R.id.usernameText);
    passwordText = (EditText) findViewById(R.id.passwordText);
    ImageView logo = (ImageView) findViewById(R.id.imageView);
    RelativeLayout backgroudLayout = (RelativeLayout) findViewById(R.id.backgroudLayout);

    logo.setOnClickListener(this);
    backgroudLayout.setOnClickListener(this);

    passwordText.setOnKeyListener(this);

    // go to user list if we have logged in user
    if (ParseUser.getCurrentUser() != null) {
        showUserList();
    }
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        // when pressed enter in password field, apply login
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            loginClicked(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        // ensuring moving keyboard from the screen when clicked on logo or background
        if (view.getId() == R.id.imageView || view.getId() == R.id.backgroudLayout) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}