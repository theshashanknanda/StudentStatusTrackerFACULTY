package com.project.stsfaculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.project.stsfaculty.Model.FacultyModel;

public class ProfileActivity extends AppCompatActivity {

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    public Button branchTextView;
    public Button nameButton, emailButton, passwordButton;

    public String hodemail;
    public String branch, name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        branchTextView = findViewById(R.id.branch_id);
        nameButton = findViewById(R.id.name_id);
        emailButton = findViewById(R.id.email_id);
        passwordButton = findViewById(R.id.password_id);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        getSupportActionBar().hide();

        // getting HOD Email
        database.getReference().child("HODOFFACULTY").child(user.getEmail().replace(".",""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        {
                            hodemail = snapshot.getValue(String.class);

                            // Getting faculty model object
                            database.getReference().child("FACULTYAccounts"+hodemail.replace(".",""))
                                    .child(user.getEmail().replace(".",""))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.hasChildren())
                                            {
                                                FacultyModel model = new FacultyModel();
                                                model = snapshot.getValue(FacultyModel.class);

                                                // Setting the retrieved data
                                                branchTextView.setText("Faculty:  " + model.getBranch());
                                                nameButton.setText("Name:  " + model.getName());
                                                passwordButton.setText("Password:  " + model.getPassword());
                                                emailButton.setText("Email:  " + model.getEmail());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
