package com.example.cooknchill.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.daprlabs.cardstack.SwipeDeck;
import com.example.cooknchill.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DiscoverFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;

    private SwipeDeck cardStack;
    private ArrayList<ProfileCard> profileCards;

    public void populateDeck()
    {
        // To complete
        mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = mFirebaseAuth.getCurrentUser().getUid();
        final DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // All changes must be tried in this exception handler in case an account deletion
                // triggers the onChange function.
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.child("uid").getValue().toString();
                        System.out.println("userId: " + userId);
                        String dishDescription = snapshot.child("dishes").child("dish0").child("dishDescription").getValue().toString();
                        System.out.println("dishDescription: " + dishDescription);
                        String dishCulture = snapshot.child("dishes").child("dish0").child("dishCulture").getValue().toString();
                        System.out.println("dishCulture: " + dishCulture);

                        // Ensure the current card is not one that the user has been matched to previously
                        dbRef.child(currentUserId + "/matches").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.hasChildren()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String matchedUserId = snapshot.getKey().toString();
                                            System.out.println("MatchedUserId" + matchedUserId);
                                            // Ensure the user's own card is not added to the deck.
                                            if (!(currentUserId.equals(userId)) && !(userId.equals(matchedUserId))) {
                                                profileCards.add(new ProfileCard(dishCulture, dishDescription, userId));
                                            }
                                        }
                                    }
                                    // If the user has no matches
                                    else
                                    {
                                        if (!currentUserId.equals(userId)) {
                                            profileCards.add(new ProfileCard(dishCulture, dishDescription, userId));
                                        }
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println("Database field couldn't be accessed." + e);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } catch (NullPointerException e) {
                    System.out.println("Database field couldn't be accessed." + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // For an unknown reason, the profileCards array requires one addition of a ProfileCard object outside of the 'for loop' in order to
        // display on the app properly.
        profileCards.add(new ProfileCard("dishCulture", "dishDescription", "BUTTcG89hGXo87aqYERku0to2Pr2") );

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

        // Logout Button Functionality
        ImageButton btnLogout;
        btnLogout = view.findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), AuthenticationActivity.class);
                getActivity().finish();
                startActivity(i);
            }
        });

        // Add data to the profileCards array list
        profileCards = new ArrayList<>();
        cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);
        TextView noProfilesMessage = (TextView) view.findViewById(R.id.noProfilesMessage);

        // Populate the profiles card deck
        populateDeck();

        if (profileCards.isEmpty())
        {
            noProfilesMessage.setVisibility(View.VISIBLE);
        }

        // on below line we are creating a variable for our adapter class and passing array list to it.
        final DeckAdapter adapter = new DeckAdapter(profileCards, getContext());

        // on below line we are setting adapter to our card stack.
        cardStack.setAdapter(adapter);

        // on below line we are setting event callback to our card stack.
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                // on card swiped to left
                System.out.println("Card Swiped Left.");
            }

            @Override
            public void cardSwipedRight(int position) {
                // on card swiped to right
                String userId = profileCards.get(position).getUserId();

                final DatabaseReference dbRef;
                dbRef = FirebaseDatabase.getInstance().getReference("users/"+userId);

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // All changes must be tried in this exception handler in case an account deletion
                        // triggers the onChange function.
                        try {
                            String firstName = dataSnapshot.child("firstName").getValue().toString();
                            dbRef.child("matches").child(userId).setValue(firstName);

                        } catch (NullPointerException e) {
                            System.out.println("Database field couldn't be accessed." + e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                System.out.println("Card Swiped Right.");
            }

            @Override
            public void cardsDepleted() {
                // this method is called when no card is present
                System.out.println("No more profiles available.");
                TextView noProfilesMessage = (TextView) view.findViewById(R.id.noProfilesMessage);
                noProfilesMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void cardActionDown() {
                // this method is called when card is swiped down.
                System.out.println("Card Moved Down.");
            }

            @Override
            public void cardActionUp() {
                // this method is called when card is moved up.
                System.out.println("Card Moved Up.");
            }
        });

    }
}
