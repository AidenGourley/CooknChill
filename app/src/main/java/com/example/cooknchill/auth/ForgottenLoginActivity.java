package com.example.cooknchill.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cooknchill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenLoginActivity extends AppCompatActivity {
    EditText email;
    Button btnResetPassword, btnSignIn, btnSignUp;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.signup);
        btnSignIn = findViewById(R.id.signin);
        email = findViewById(R.id.email);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            String emailText = email.getText().toString();
            @Override
            public void onClick(View v) {
                if(emailText.isEmpty()){
                    email.setError("Don't forget to enter your email address!");
                    email.requestFocus();
                }
                else{
                    mFirebaseAuth.sendPasswordResetEmail(emailText)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("Email Sent");
                                    }
                                }
                            });
                }
            }
        });
    }
}