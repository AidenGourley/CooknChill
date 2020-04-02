package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private long mLastClickTime = 0;


    public LoginFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final NavController navController = Navigation.findNavController(view);

        final Button btnSignIn, signUp, forgottenPassword;
        final EditText emailId, password;
        emailId = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        btnSignIn = view.findViewById(R.id.register);
        signUp = view.findViewById(R.id.signUp);
        forgottenPassword = view.findViewById(R.id.forgottenPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Don't forget to enter your email address!");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Don't forget to enter your password!");
                    password.requestFocus();
                }
                else{
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                getActivity().finish();
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(),"Sorry, that's not quite right... Give it another go!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                navController.navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });

        forgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                navController.navigate(R.id.action_loginFragment_to_resetPassword);
            }
        });

    }
}
