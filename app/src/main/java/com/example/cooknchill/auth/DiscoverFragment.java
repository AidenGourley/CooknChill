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
    private DeckAdapter adapter;
    private String supportAccountID = "unLh8Ui7xgXE576BVyCr11cgN7f1";

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
                profileCards.clear();

                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.child("uid").getValue().toString();
                        String dishDescription = snapshot.child("dishes/dish0/dishDescription").getValue().toString();
                        String dishCulture = snapshot.child("dishes/dish0/dishCulture").getValue().toString();

                        // Ensure the current card is not one that the user has been matched to previously
                        dbRef.child(currentUserId + "/matches").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    boolean addCard = true;
                                    if (dataSnapshot.hasChildren()) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String matchedUserId = snapshot.getKey().toString();
                                            System.out.println("MatchedUserId" + matchedUserId);
                                            // Ensure the card is not a matched user.
                                            if ((userId.equals(matchedUserId))) {
                                                addCard = false;
                                            }
                                        }
                                    }
                                    // Ensure the card is not the support account or the current user
                                    if (currentUserId.equals(userId) &&
                                            (userId.equals(supportAccountID)))
                                    {
                                        addCard = false;
                                    }

                                    // If the card is OK to add
                                    if (addCard)
                                    {
                                        profileCards.add(new ProfileCard(dishCulture, dishDescription, userId));
                                        System.out.println("CARD ADDED");
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
        profileCards.add(new ProfileCard("Discover new dishes!",
                "Swipe right to show you're interested! \n " +
                        "Swipe left to politely decline!", "unLh8Ui7xgXE576BVyCr11cgN7f1") );
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

        // Populate the profiles card deck
        FirebaseDatabase.getInstance().getReference("users").get();


        populateDeck();
        // on below line we are creating a variable for our adapter class and passing array list to it.
        adapter = new DeckAdapter(profileCards, getContext());

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
                String matchedUserId = profileCards.get(position).getUserId();
                String currentUserId = mFirebaseAuth.getCurrentUser().getUid();

                final DatabaseReference dbRef;
                dbRef = FirebaseDatabase.getInstance().getReference("users/"+currentUserId);

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // All changes must be tried in this exception handler in case an account deletion
                        // triggers the onChange function.
                        try {
                            String firstName = dataSnapshot.child("firstName").getValue().toString();
                            if (!(matchedUserId.equals(supportAccountID))) {
                                dbRef.child("matches").child(matchedUserId).setValue(matchedUserId);
                            }
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
