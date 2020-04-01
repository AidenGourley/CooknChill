package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.NavController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cooknchill.R;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {

    private Button btnLogout, btnEditProfile;
    FirebaseAuth mFirebaseAuth;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final NavController navController = Navigation.findNavController(view);
        btnLogout = view.findViewById(R.id.logout);
        btnEditProfile = view.findViewById(R.id.editProfile);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                navController.popBackStack();
                Intent i = new Intent(getActivity(), AuthenticationActivity.class);
                getActivity().finish();
                startActivity(i);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_editProfileFragment);
            }
        });
    }
}
