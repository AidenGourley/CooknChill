package com.example.cooknchill.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cooknchill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpFragment extends Fragment {

    EditText email, password, firstName, surname;
    Spinner uniChoice;
    Button register, signIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final NavController navController = Navigation.findNavController(view);

        //Shouldn't need this check//

        /*mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    navController.popBackStack(); //ATTEMPT TO PREVENT BACK CLICKS
                    navController.navigate(R.id.action_signUpFragment_m);
                }
                else{
                    Toast.makeText(getActivity(),"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };*/

        email = view.findViewById(R.id.email);
        uniChoice = view.findViewById(R.id.university);
        firstName = view.findViewById(R.id.firstName);
        surname = view.findViewById(R.id.surname);
        password = view.findViewById(R.id.password);
        register = view.findViewById(R.id.register);
        signIn = view.findViewById(R.id.signIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentActivity context = getActivity();
                final String emailText = email.getText().toString().trim();
                final String passwordText =password.getText().toString().trim();
                final String universityText = uniChoice.getSelectedItem().toString().trim();
                final String firstNameText = firstName.getText().toString().trim();
                final String surnameText = surname.getText().toString().trim();
                if(universityText.contentEquals("Select University")){
                    email.setError("Don't forget to enter your university!");
                    uniChoice.requestFocus();
                }
                else if(emailText.isEmpty()){
                    email.setError("Don't forget to enter your email address!");
                    email.requestFocus();
                }
                else if(firstNameText.equals("") || surnameText.equals("")){
                    firstName.setError("Don't forget to enter your name!");
                    firstName.requestFocus();
                }
                else if(!emailText.contains(".ac.uk")){
                    email.setError("Your email must be a valid academic email address!");
                    email.requestFocus();
                }
                else{
                    mFirebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
                                User user = new User(uid, universityText, firstNameText, surnameText);

                                dbRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            navController.popBackStack(); //ATTEMPT TO PREVENT BACK CLICKS
                                            navController.navigate(R.id.action_signUpFragment_to_signUpPhase2);
                                        } else {
                                            Toast.makeText(context, "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(context, "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpFragment_to_loginFragment);
            }
        });

    }
    //SHOULDN'T NEED THIS//
    /*
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
     */
}