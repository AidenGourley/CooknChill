package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cooknchill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpPhase2Fragment extends Fragment {

    EditText course, courseDuration;
    Spinner chooseNationality;
    Button confirm;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    public SignUpPhase2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_phase2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final NavController navController = Navigation.findNavController(view);

        chooseNationality = view.findViewById(R.id.chooseNationality);
        course = view.findViewById(R.id.course);
        courseDuration = view.findViewById(R.id.courseDuration);
        confirm = view.findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String courseText = course.getText().toString().trim();
                final String courseDurationText = courseDuration.getText().toString().trim();
                final String nationalityText = chooseNationality.getSelectedItem().toString().trim();

                if (courseText.isEmpty()) {
                    course.setError("Don't forget to tell us your course!");
                    course.requestFocus();
                }
                else if (courseDurationText.isEmpty()) {
                    courseDuration.setError("Don't forget to tell us your course duration!");
                    courseDuration.requestFocus();
                }
                else if (nationalityText.equals("Select Nationality")) {
                    courseDuration.setError("Don't forget to select your nationality!");
                    courseDuration.requestFocus();
                }
                else{
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
                    dbRef.child("courseDuration").setValue(courseDurationText);
                    dbRef.child("nationality").setValue(nationalityText);
                    dbRef.child("course").setValue(courseText).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                                getActivity().finish();
                                /*
                                navController.popBackStack(); //ATTEMPT TO PREVENT BACK CLICKS
                                navController.navigate(R.id.action_signUpPhase2_to_mainNavigation);

                                 */
                            } else {
                                Toast.makeText(getActivity(), "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
