package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cooknchill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp2Activity extends AppCompatActivity {
    EditText course, courseDuration;
    Spinner chooseNationality;

    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        chooseNationality = findViewById(R.id.chooseNationality);
        course = findViewById(R.id.course);
        courseDuration = findViewById(R.id.courseDuration);
        confirm = findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String course = SignUp2Activity.this.course.getText().toString().trim();
                final String courseDuration = SignUp2Activity.this.courseDuration.getText().toString().trim();
                final String nationality = SignUp2Activity.this.chooseNationality.toString().trim();
                if (!(course.isEmpty() && courseDuration.isEmpty())){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
                    dbRef.child("courseDuration").setValue(courseDuration);
                    dbRef.child("nationality").setValue(nationality);
                    dbRef.child("course").setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignUp2Activity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(SignUp2Activity.this, "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    SignUp2Activity.this.course.setError("Please enter a valid course and duration!");
                    SignUp2Activity.this.course.requestFocus();
                }
            }
        });
    }
}