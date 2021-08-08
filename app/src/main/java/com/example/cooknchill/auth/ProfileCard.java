package com.example.cooknchill.auth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

class ProfileCard {

    private FirebaseAuth mFirebaseAuth;

    private String userId;
    private String dishCulture;
    private String dishDescription;

    public String getDishCulture() {
        return dishCulture;
    }

    public void setDishCulture(String dishCulture) {
        this.dishCulture = dishCulture;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Class to download images form the Picture URL and load them into the ImageView
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    // Get the dish picture URL and call the DownloadImageTask to load them into the ImageView
    public void setDishPictureUrl(ImageView imageView) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // All changes must be tried in this exception handler in case an account deletion
                // triggers the onChange function.
                try {
                    String dishPictureUrl = dataSnapshot.child("dishes").child("dish0").child("dishPic0").getValue().toString();
                    //Glide.with(getActivity()).load(dishPictureUrl).into(imageView);
                    new DownloadImageTask(imageView).execute(dishPictureUrl);
                    System.out.println("Dish Picture URL: " + dishPictureUrl);
                } catch (NullPointerException e) {
                    System.out.println("Database field couldn't be accessed." + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Constructor
    public ProfileCard(String dishCulture, String dishDescription, String userId) {
        setUserId(userId);
        setDishCulture(dishCulture);
        setDishDescription(dishDescription);

    }

}
