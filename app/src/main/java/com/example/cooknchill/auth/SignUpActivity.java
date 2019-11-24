package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cooknchill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText email, password, university, firstName, surname;
    Button register, signIn;
    TextView signIn2;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        university = findViewById(R.id.university);
        firstName = findViewById(R.id.firstName);
        surname = findViewById(R.id.surname);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        signIn2 = findViewById(R.id.signin2);
        signIn = findViewById(R.id.signin);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = SignUpActivity.this.email.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    SignUpActivity.this.email.setError("Don't forget to enter your email address!");
                    SignUpActivity.this.email.requestFocus();
                }
                if(firstName.getText().toString().equals("") || surname.getText().toString().equals("")){
                    SignUpActivity.this.firstName.setError("Don't forget to enter your name!");
                    SignUpActivity.this.firstName.requestFocus();
                }
                if(university.getText().toString().equals("")){
                    SignUpActivity.this.email.setError("Don't forget to enter your university!");
                    SignUpActivity.this.email.requestFocus();
                }
                else if(!email.contains(".ac.uk")){
                    SignUpActivity.this.email.setError("Your email must be a valid academic email address!");
                    SignUpActivity.this.email.requestFocus();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"Sorry! Your sign up has been unsuccessful, please give it another go!",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Oops, something's gone wrong... Please try again!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        signIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
