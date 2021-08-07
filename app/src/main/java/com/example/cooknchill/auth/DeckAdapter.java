package com.example.cooknchill.auth;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cooknchill.R;

import java.util.ArrayList;

public class DeckAdapter extends BaseAdapter {

   // on below line we have created variables
   // for our array list and context.
   private ArrayList<ProfileCard> profileCards;
   private Context context;

   // on below line we have created constructor for our variables.
   public DeckAdapter(ArrayList<ProfileCard> profileCards, Context context) {
      this.profileCards = profileCards;
      this.context = context;
   }

   @Override
   public int getCount() {
      // in get count method we are returning the size of our array list.
      return profileCards.size();
   }

   @Override
   public Object getItem(int position) {
      // in get item method we are returning the item from our array list.
      return profileCards.get(position);
   }

   @Override
   public long getItemId(int position) {
      // in get item id we are returning the position.
      return position;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      // in get view method we are inflating our layout on below line.
      View v = convertView;
      if (v == null) {
         // on below line we are inflating our layout.
         v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);
      }
      ImageView imageView = v.findViewById(R.id.dishPicture);
      // on below line we are initializing our variables and setting data to our variables.
      ((TextView) v.findViewById(R.id.dishCulture)).setText(profileCards.get(position).getDishCulture());
      ((TextView) v.findViewById(R.id.dishDescription)).setText(profileCards.get(position).getDishDescription());
      profileCards.get(position).setDishPictureUrl(imageView);
      return v;
   }
}