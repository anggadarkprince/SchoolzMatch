package com.sketchproject.schoolzmatch;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sketchproject.schoolzmatch.database.DBHelper;
import com.sketchproject.schoolzmatch.database.Profile;
import com.sketchproject.schoolzmatch.modules.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private Validator validator;
    private EditText name;
    private EditText address;
    private RadioButton genderMale;
    private RadioButton genderFemale;
    private EditText school;
    private EditText grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        validator = new Validator();

        name = (EditText) findViewById(R.id.editName);
        address = (EditText) findViewById(R.id.editAddress);
        genderMale = (RadioButton) findViewById(R.id.radioMale);
        genderFemale = (RadioButton) findViewById(R.id.radioFemale);
        school = (EditText) findViewById(R.id.editSchool);
        grade = (EditText) findViewById(R.id.editGrade);

        loadProfile();

        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        assert buttonSave != null;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<Profile> data = new ArrayList<>();
                data.add(new Profile("name", name.getText()));
                data.add(new Profile("address", address.getText()));
                data.add(new Profile("gender", genderMale.isChecked() ? "male" : genderFemale.isChecked() ? "female" : ""));
                data.add(new Profile("school", school.getText()));
                data.add(new Profile("grade", grade.getText()));

                if (checkValidation(data)) {
                    saveProfile(view, data);
                } else {
                    final Snackbar snackbar = Snackbar.make(view, "Please complete the form!", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorLight));
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundResource(R.color.colorAccent);
                    snackbar.show();
                }
            }
        });
    }

    /**
     * Load profile from database
     */
    private void loadProfile() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase mDatabase = dbHelper.getReadableDatabase();

        String[] projection = {Profile.KEY, Profile.VALUE};

        Cursor cursor = mDatabase.query(Profile.TABLE, projection, null, null, null, null, null);

        HashMap<String, String> profiles = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                profiles.put(cursor.getString(cursor.getColumnIndexOrThrow(Profile.KEY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Profile.VALUE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        if (profiles.containsKey("name")) {
            name.setText(profiles.get("name"));
        }

        if (profiles.containsKey("address")) {
            address.setText(profiles.get("address"));
        }

        if (profiles.containsKey("school")) {
            school.setText(profiles.get("school"));
        }

        if (profiles.containsKey("grade")) {
            grade.setText(profiles.get("grade"));
        }

        if (profiles.containsKey("gender")) {
            if (profiles.get("gender").equals("male")) {
                genderMale.setChecked(true);
                genderFemale.setChecked(false);
            } else {
                genderFemale.setChecked(true);
                genderMale.setChecked(false);
            }
        }
    }

    /**
     * All input must be not empty.
     *
     * @param data profile key-value collection
     * @return empty check status
     */
    private boolean checkValidation(List<Profile> data) {
        for (int i = 0; i < data.size(); i++) {
            Profile profile = data.get(i);
            if (validator.isEmpty(String.valueOf(profile.getValue()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Save status check.
     *
     * @param view button
     * @param data profile key-value collection
     */
    private void saveProfile(View view, final List<Profile> data) {
        Snackbar snackbar;
        View snackbarView;
        if (save(data)) {
            snackbar = Snackbar.make(view, "Profile data has been updated", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorLight));
            snackbar.setAction("BACK TO MAIN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            snackbarView = snackbar.getView();
            snackbarView.setBackgroundResource(R.color.colorSuccess);
        } else {
            snackbar = Snackbar.make(view, "Saving profile failed!", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorLight));
            snackbar.setAction("TRY AGAIN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveProfile(view, data);
                }
            });
            snackbarView = snackbar.getView();
            snackbarView.setBackgroundResource(R.color.colorAccent);
        }
        snackbar.show();
    }

    /**
     * Save data into database
     *
     * @param data collection
     * @return save status
     */
    private boolean save(List<Profile> data) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase mDatabase = null;

        String[] projection = {Profile.KEY};

        for (int i = 0; i < data.size(); i++) {
            Profile profile = data.get(i);

            mDatabase = dbHelper.getReadableDatabase();
            String selection = Profile.KEY + " = ?";
            String[] selectionArgs = {String.valueOf(profile.getKey())};
            Cursor cursor = mDatabase.query(Profile.TABLE, projection, selection, selectionArgs, null, null, null);

            mDatabase = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Profile.KEY, profile.getKey());
            values.put(Profile.VALUE, String.valueOf(profile.getValue()));

            if (cursor.moveToFirst()) {
                // update
                mDatabase.update(Profile.TABLE, values, selection, selectionArgs);
            } else {
                // insert
                mDatabase.insert(Profile.TABLE, null, values);
            }
            cursor.close();
        }

        if (mDatabase != null) {
            mDatabase.close();
        }

        return true;
    }
}
