package com.example.cooknchill.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.cooknchill.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ConfigureDishProfileFragment extends Fragment {

    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference storageReference;
    private FirebaseAuth mFirebaseAuth;
    private ImageView dishImage0, dishImage1;
    private EditText descText;
    private Spinner cultureChoice;
    private StorageReference ref;
    private DatabaseReference dbRef;
    private int PIC_NUM = 0;
    private final List<String> nationalities = Arrays.asList("Select Nationality", "United Kingdom", "Bangladesh", "Pakistan",
            "Italy", "Uganda");
    private long mLastClickTime;


    public ConfigureDishProfileFragment() {
        // Required empty public constructor
    }


    private void chooseImage(final int picNum) {
        PIC_NUM = picNum;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(final String userID, Uri filePath) {
                /*
                @picNum 0 for the left hand side image, 1 for the right hand side image
                 */
        storageReference = FirebaseStorage.getInstance().getReference().child(userID);
        // users -> dishes -> dishID
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("dishes");
        if (PIC_NUM == 0) {
            // dishes -> dishID -> dishPic0
            ref = storageReference.child("dishes").child("dish0").child("dishPic0");
            dbRef = dbRef.child("dish0").child("dishPic0");
        } else {
            ref = storageReference.child("dishes").child("dish0").child("dishPic1");
            dbRef = dbRef.child("dish0").child("dishPic1");
        }

        if (filePath != null) {
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    System.out.println("Dish: " + PIC_NUM + " Picture Upload Success! URL: " + downloadUrl.toString());
                                    dbRef.setValue(downloadUrl.toString());
                                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Dish: " + PIC_NUM + " Picture Upload Failed: Couldn't save file to storage");
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            System.out.println("Dish: " + PIC_NUM + " Picture Upload Failed: filePath null");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configure_dish_profile, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            uploadImage(mFirebaseAuth.getCurrentUser().getUid(), filePath);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();

        Button btnDishPic0, btnDishPic1, btnSubmit;
        btnDishPic0 = view.findViewById(R.id.btnDishPic0);
        btnDishPic1 = view.findViewById(R.id.btnDishPic1);
        btnSubmit = view.findViewById(R.id.submit);
        dishImage0 = view.findViewById(R.id.dishImage0);
        dishImage1 = view.findViewById(R.id.dishImage1);

        descText = view.findViewById(R.id.dishDesc);
        cultureChoice = view.findViewById(R.id.nationalityChoice);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseAuth.getCurrentUser().getUid());

        dbRef.child("dishes").child("dish0").child("dishPic0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All changes ust be tried in his exception handler in case an account deletion
                // triggers the onChange function.
                try {
                    String dishPicUrl = dataSnapshot.getValue().toString();
                    Glide.with(getActivity()).load(dishPicUrl).into(dishImage0);
                } catch (NullPointerException e) {
                    System.out.println("Database field couldn't be accessed." + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dbRef.child("dishes").child("dish0").child("dishPic1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All changes ust be tried in his exception handler in case an account deletion
                // triggers the onChange function.
                try {
                    String dishPicUrl = dataSnapshot.getValue().toString();
                    Glide.with(getActivity()).load(dishPicUrl).into(dishImage1);
                } catch (NullPointerException e) {
                    System.out.println("Database field couldn't be accessed.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All changes ust be tried in his exception handler in case an account deletion
                // triggers the onChange function.
                try {
                    String description = dataSnapshot.child("dishes").child("dish0").child("dishDescription").getValue().toString();
                    String dishCulture = dataSnapshot.child("dishes").child("dish0").child("dishCulture").getValue().toString();
                    cultureChoice.setSelection(nationalities.indexOf(dishCulture));
                    descText.setText(description);


                } catch (NullPointerException e) {
                    System.out.println("Database field couldn't be accessed.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        btnDishPic0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                chooseImage(0);
            }
        });


        btnDishPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                chooseImage(1);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                dbRef.child("dishes").child("dish0").child("dishDescription").setValue(descText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final String dishCulture = cultureChoice.getSelectedItem().toString().trim();
                            if (!dishCulture.equals("Select Nationality")) {
                                dbRef.child("dishes").child("dish0").child("dishCulture").setValue(dishCulture);
                            }
                            Toast.makeText(getActivity(), "Thanks, we've updated that for you!", Toast.LENGTH_SHORT).show();
                            //navController.navigate(R.id.action_configureDishProfileFragment_to_discoverFragment);
                        } else {
                            Toast.makeText(getActivity(), "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}