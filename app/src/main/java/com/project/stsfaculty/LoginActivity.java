package com.project.stsfaculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.stsfaculty.Model.FacultyModel;

public class LoginActivity extends AppCompatActivity {

    public EditText emailEditText, passwordEditText, HODEmailEditText;
    public Button loginButton;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText_id);
        passwordEditText = findViewById(R.id.passwordEditText_id);
        HODEmailEditText = findViewById(R.id.hodEmailEditText_id);
        loginButton = findViewById(R.id.loginButton_id);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            // creating account
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String hodEmail = HODEmailEditText.getText().toString();

            Log.i("mytag", "At button click");

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Logging you in");
            dialog.show();

            if(!email.isEmpty() && !password.isEmpty()){
                database.getReference().child("FACULTYAccounts"+hodEmail.replace(".",""))
                        .child(email.replace(".",""))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    Log.i("mytag", "At database check");

                                    FacultyModel model = new FacultyModel();
                                    model = snapshot.getValue(FacultyModel.class);

                                    if(model.getEmail().equals(email)
                                    &&
                                    model.getPassword().equals(password))
                                    {

                                        // Data matched

                                        Log.i("mytag", "data matched");

                                        auth.createUserWithEmailAndPassword(email, password)
                                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                Toast.makeText(LoginActivity.this, "Login Succesfully", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                dialog.dismiss();
                                                finish();
                                            }})
                                            .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                login(email, password);
                                                Log.i("mytag", e.getMessage());
                                            }});
                                        }
                                    }
                                }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            else{
                Toast.makeText(LoginActivity.this, "Fields Empty", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "login Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                        Log.i("mytag", e.getMessage());
                        dialog.dismiss();
                    }
                });
    }
}
