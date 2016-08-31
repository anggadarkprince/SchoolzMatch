package com.sketchproject.schoolzmatch;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sketchproject.schoolzmatch.database.Profile;
import com.sketchproject.schoolzmatch.database.ProfileRepository;
import com.sketchproject.schoolzmatch.modules.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private Validator validator;
    private EditText name;
    private EditText address;
    private RadioButton genderMale;
    private RadioButton genderFemale;
    private EditText school;
    private EditText grade;

    private ProfileRepository profileRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        profileRepository = new ProfileRepository(getApplicationContext());

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
        Map<String, Object> profiles = profileRepository.retrieve();

        if (profiles.containsKey("name")) {
            name.setText(String.valueOf(profiles.get("name")));
        }

        if (profiles.containsKey("address")) {
            address.setText(String.valueOf(profiles.get("address")));
        }

        if (profiles.containsKey("school")) {
            school.setText(String.valueOf(profiles.get("school")));
        }

        if (profiles.containsKey("grade")) {
            grade.setText(String.valueOf(profiles.get("grade")));
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
        for (int i = 0; i < data.size(); i++) {
            profileRepository.store(data.get(i));
        }
        return true;
    }
}
