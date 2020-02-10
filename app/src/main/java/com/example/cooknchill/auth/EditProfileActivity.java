package com.example.cooknchill.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooknchill.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class EditProfileActivity extends AppCompatActivity {
    Button btnChangePicture;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        btnChangePicture = findViewById(R.id.changePicture);

        btnChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                //.setPhotoUri(Uri.fromFile()).build();
                //user.updateProfile()
            }
        });
    }
}
