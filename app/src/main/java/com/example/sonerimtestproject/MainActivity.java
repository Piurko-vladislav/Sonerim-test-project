package com.example.sonerimtestproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.sonerimtestproject.view.MapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import static androidx.core.util.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = new MapFragment();

        addFragmentToActivity(getSupportFragmentManager(),
                mapFragment, R.id.base_fragment);
    }

    @SuppressLint("RestrictedApi")
    private void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                        @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

}