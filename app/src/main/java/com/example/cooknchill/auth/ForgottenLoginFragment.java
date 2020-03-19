package com.example.cooknchill.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cooknchill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgottenLoginFragment extends Fragment {

    private EditText email;
    private Button btnSendEmail, btnSignIn, btnSignUp;
    private FirebaseAuth mFirebaseAuth;


    public ForgottenLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgotten_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final NavController navController = Navigation.findNavController(view);

        btnSignUp = view.findViewById(R.id.signUp);
        btnSignIn = view.findViewById(R.id.signIn);
        email = view.findViewById(R.id.email);
        btnSendEmail= view.findViewById(R.id.sendemail);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                if(emailText.isEmpty()){
                    email.setError("Don't forget to enter your email address!");
                    email.requestFocus();
                }
                else if(!emailText.contains(".ac.uk")){
                    email.setError("Your email must be a valid academic email address!");
                    email.requestFocus();
                }
                else{
                    mFirebaseAuth.sendPasswordResetEmail(emailText)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("Email Sent");
                                        navController.navigate(R.id.action_resetPassword_to_resetPasswordPhase2);
                                    }
                                    else{
                                        System.out.println("Email failure");
                                        Toast.makeText(getActivity(),"Sorry, something went wrong... Give it another go!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_resetPassword_to_loginFragment);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                navController.navigate(R.id.action_resetPassword_to_signUpFragment);
            }
        });

    }
}
