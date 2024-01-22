package com.sp.mallreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MallReview extends AppCompatActivity {

    private BottomNavigationView navView;
    private MallList Mall_list_Fragment;
    private MallReviwer Mall_Reviwer_Fragment;
    private int bottomSelectedMenu = R.id.review;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        navView = findViewById(R.id.BottomNavigationView);
        navView.setOnItemSelectedListener(menuSelected);
        Mall_list_Fragment = new MallList();
        Mall_Reviwer_Fragment = new MallReviwer();
    }
    @Override
    protected void onStart() {
        invalidateOptionsMenu();
        super.onStart();
    }

    //Logic case for BottomNavBar to switch between fragments
    NavigationBarView.OnItemSelectedListener menuSelected = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            invalidateOptionsMenu();
            if (id == R.id.review) {
                fragmentManager.beginTransaction()
                        .replace(R.id.MallFragmentContainer, Mall_Reviwer_Fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                return true;
            } else if (id == R.id.past_reviews) {
                fragmentManager.beginTransaction()
                        .replace(R.id.MallFragmentContainer, Mall_list_Fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Cases for Options in Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.about) {
            //pulls up activity window for about page
            intent = new Intent(MallReview.this, About.class);
            startActivity(intent);
        }  else if (item.getItemId() == R.id.exit) {
            //Exits the app
            System.exit(0);
        } else if (item.getItemId() == R.id.add) {
            //opens an empty review page for new entries
            navView.setSelectedItemId(R.id.review);
            Bundle bundle = new Bundle();
            bundle.putString("id",null);
            Mall_Reviwer_Fragment.getParentFragmentManager().setFragmentResult("listToDetailKey", bundle);
        } else { System.exit(0);}
        return super.onOptionsItemSelected(item);
    }

    }
