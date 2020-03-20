package com.example.cooknchill.auth;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cooknchill.R;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    public EditProfileFragment() {
        // Required empty public constructor
    }

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mFirebaseAuth;
    Uri filePath;
    Button btnEditPhoto, btnSubmit, btnCancel, btnDeleteProfile;
    ImageView imageView;
    EditText bioText;
    TextView dishPreferenceText1, dishPreferenceText2, dishPreferenceText3;
    Spinner dishPriority1, dishPriority2, dishPriority3;

    final int PICK_IMAGE_REQUEST = 71;

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(final String userID, Uri filePath) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(userID);
        if (filePath != null) {
            final StorageReference ref = storageReference.child("profilePic");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    System.out.println("Profile Picture Upload Success! URL: " + downloadUrl.toString());
                                    dbRef.child("profilePic").setValue(downloadUrl.toString());
                                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Profile Picture Upload Failed: Couldn't save file to storage");
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            System.out.println("Profile Picture Upload Failed: filePath null");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            uploadImage(mFirebaseAuth.getCurrentUser().getUid(), filePath);
        }
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnDeleteProfile = view.findViewById(R.id.deleteAccount);
        btnEditPhoto = view.findViewById(R.id.editPhoto);
        btnSubmit = view.findViewById(R.id.submit);
        btnCancel = view.findViewById(R.id.cancel);
        imageView = view.findViewById(R.id.imgView);
        bioText = view.findViewById(R.id.bioText);
        dishPriority1 = view.findViewById(R.id.dishPriority1);
        dishPriority2 = view.findViewById(R.id.dishPriority2);
        dishPriority3 = view.findViewById(R.id.dishPriority3);
        dishPreferenceText1 = view.findViewById(R.id.dishPreferenceText1);
        dishPreferenceText2 = view.findViewById(R.id.dishPreferenceText2);
        dishPreferenceText3 = view.findViewById(R.id.dishPreferenceText3);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseAuth.getCurrentUser().getUid());


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All changes ust be tried in his exception handler in case an account deletion
                // triggers the onChange function.
                try {
                    String bio = dataSnapshot.child("bio").getValue().toString();
                    String dp1 = dataSnapshot.child("dishPreference1").getValue().toString();
                    String dp2 = dataSnapshot.child("dishPreference2").getValue().toString();
                    String dp3 = dataSnapshot.child("dishPreference3").getValue().toString();
                    bioText.setText(bio);
                    dishPreferenceText1.setText(dp1);
                    dishPreferenceText2.setText(dp2);
                    dishPreferenceText3.setText(dp3);
                    System.out.println("Profile Picture URL: " + dataSnapshot.child("profilePic").getValue().toString());
                    Glide.with(getActivity()).load(dataSnapshot.child("profilePic").getValue().toString()).into(imageView);

                } catch (NullPointerException e) {
                    System.out.println("Database field couldn't be accessed. Likely user deleted account.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        btnEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child("bio").setValue(bioText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final String dp1 = dishPriority1.getSelectedItem().toString().trim();
                            final String dp2 = dishPriority2.getSelectedItem().toString().trim();
                            final String dp3 = dishPriority3.getSelectedItem().toString().trim();
                            if (!dp1.equals("Select Nationality")) {
                                dbRef.child("dishPreference1").setValue(dp1);
                            }
                            if (!dp2.equals("Select Nationality")) {
                                dbRef.child("dishPreference2").setValue(dp2);
                            }
                            if (!dp3.equals("Select Nationality")) {
                                dbRef.child("dishPreference3").setValue(dp3);
                            }

                            Toast.makeText(getActivity(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_editProfileFragment_to_homeFragment);
            }
        });

        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mFirebaseAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                navController.popBackStack();
                                Intent i = new Intent(getActivity(), AuthenticationActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        });
                    }
                });
            }
        });
    }
}