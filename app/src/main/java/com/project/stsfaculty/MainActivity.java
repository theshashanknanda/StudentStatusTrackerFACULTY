package com.project.stsfaculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.project.stsfaculty.Fragments.Fragment1;
import com.project.stsfaculty.Fragments.Fragment2;

public class MainActivity extends AppCompatActivity {
    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottomNavigationView_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment1()).commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.documents_id:

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment1()).commit();

                        break;

                    case R.id.add_id:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment2()).commit();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_profile_id:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.menu_logout_id:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}