package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.daprlabs.cardstack.SwipeDeck;
import com.example.cooknchill.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class DiscoverFragment extends Fragment {

    private StorageReference storageReference;
    private FirebaseAuth mFirebaseAuth;

    private SwipeDeck cardStack;
    private ArrayList<ProfileCard> profileCards;

    public void populateDeck()
    {
        // To complete
    }

    // Constructor
    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        Button btnLogout, btnEditProfile;
        btnLogout = view.findViewById(R.id.logout);



        // Logout Button Functionality
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), AuthenticationActivity.class);
                getActivity().finish();
                startActivity(i);
            }
        });

    }
}
