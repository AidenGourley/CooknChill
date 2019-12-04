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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                final String email = SignUpActivity.this.email.getText().toString().trim();
                final String password = SignUpActivity.this.password.getText().toString().trim();
                final String university = SignUpActivity.this.university.getText().toString().trim();
                final String firstName = SignUpActivity.this.firstName.getText().toString().trim();
                final String surname = SignUpActivity.this.surname.getText().toString().trim();
                if(email.isEmpty()){
                    SignUpActivity.this.email.setError("Don't forget to enter your email address!");
                    SignUpActivity.this.email.requestFocus();
                }
                if(firstName.equals("") || surname.equals("")){
                    SignUpActivity.this.firstName.setError("Don't forget to enter your name!");
                    SignUpActivity.this.firstName.requestFocus();
                }
                if(university.equals("")){
                    SignUpActivity.this.email.setError("Don't forget to enter your university!");
                    SignUpActivity.this.email.requestFocus();
                }
                else if(!email.contains(".ac.uk")){
                    SignUpActivity.this.email.setError("Your email must be a valid academic email address!");
                    SignUpActivity.this.email.requestFocus();
                }
                else  if(!password.isEmpty()){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
                                User user = new User(uid, university, firstName, surname);

                                dbRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(SignUpActivity.this, SignUp2Activity.class));
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(SignUpActivity.this, "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
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
