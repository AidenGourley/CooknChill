package com.example.cooknchill.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cooknchill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;



public class EditProfileActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(user.getUid());
    Uri filePath;
    Button btnEditPhoto, btnSubmit;
    ImageView imageView;
    EditText bioText;
    TextView dishPreferenceText1, dishPreferenceText2, dishPreferenceText3;
    Spinner dishPriority1, dishPriority2, dishPriority3;
    final int PICK_IMAGE_REQUEST = 71;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(user.getUid() + "/profilePic");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            dbRef.child(user.getUid()).child("profilePic").setValue(ref.getDownloadUrl().toString());
                            Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (java.io.IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        btnEditPhoto = findViewById(R.id.editPhoto);
        btnSubmit = findViewById(R.id.submit);
        imageView = findViewById(R.id.imgView);
        bioText = findViewById(R.id.bioText);
        dishPriority1 = findViewById(R.id.dishPriority1);
        dishPriority2 = findViewById(R.id.dishPriority2);
        dishPriority3 = findViewById(R.id.dishPriority3);
        dishPreferenceText1 = findViewById(R.id.dishPreferenceText1);
        dishPreferenceText2 = findViewById(R.id.dishPreferenceText2);
        dishPreferenceText3 = findViewById(R.id.dishPreferenceText3);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bio = dataSnapshot.child("bio").getValue().toString();
                String dp1 = dataSnapshot.child("dishPreference1").getValue().toString();
                String dp2 = dataSnapshot.child("dishPreference2").getValue().toString();
                String dp3 = dataSnapshot.child("dishPreference3").getValue().toString();
                bioText.setText(bio);
                dishPreferenceText1.setText(dp1);
                dishPreferenceText2.setText(dp2);
                dishPreferenceText3.setText(dp3);
                System.out.println("Profile Picture URL: " + dataSnapshot.child("profilePic").getValue().toString());
                Glide.with(EditProfileActivity.this).load(dataSnapshot.child("profilePic").getValue().toString()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                uploadImage();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dbRef.child("bio").setValue(bioText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final String dp1 = dishPriority1.getSelectedItem().toString().trim();
                            final String dp2 = dishPriority2.getSelectedItem().toString().trim();
                            final String dp3 = dishPriority3.getSelectedItem().toString().trim();
                            if (!dp1.equals("Select Nationality")){
                                dbRef.child("dishPreference1").setValue(dp1);
                            }
                            if (!dp2.equals("Select Nationality")){
                                dbRef.child("dishPreference2").setValue(dp2);
                            }
                            if (!dp3.equals("Select Nationality")){
                                dbRef.child("dishPreference3").setValue(dp3);
                            }
                            startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Oops, something's gone wrong... Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
