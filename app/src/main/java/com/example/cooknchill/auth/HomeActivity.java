package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooknchill.R;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {
    Button btnLogout, btnEditProfile;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnLogout = findViewById(R.id.logout);
        btnEditProfile = findViewById(R.id.editProfile);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToMain);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToEditProfile = new Intent(HomeActivity.this, EditProfileActivity.class);
                startActivity(intToEditProfile);
            }
        });
    }
}
