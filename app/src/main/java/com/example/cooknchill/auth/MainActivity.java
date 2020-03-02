package com.example.cooknchill.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.cooknchill.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        Fragment mFragment;
        mFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.MainActivityNavHostFrag, mFragment).commit();
        /*setContentView(R.layout.activity_main);
        NavHostFragment.create(R.navigation.nav_graph);
        FragmentManager fm = getSupportFragmentManager();
        HomeFragment fragment = new HomeFragment();
        fm.beginTransaction().replace(R.id.container,fragment).commit();*/
    }
}
