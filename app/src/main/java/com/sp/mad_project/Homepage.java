package com.sp.mad_project;

import static com.sp.mad_project.LocalDBHelper.fh;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;

public class Homepage extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(navSelected);

        fh.setRecipetype("*");
        fh.setCalorieamt("");
        fh.setPreptime("");
        fh.setPreptimemoreorless("=");
        fh.setCaloriemoreorless("=");
        // Find Recipes Button
        Button findRecipesButton = findViewById(R.id.findRecipesButton);
        Button cookingTimerButton = findViewById(R.id.cookingTimerButton);
        findRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, RecipeList.class);
                startActivity(intent);
            }
        });

        cookingTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, CookingTimer.class);
                startActivity(intent);
            }
        });
    }

    NavigationView.OnNavigationItemSelectedListener navSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.sidenav_exit) {
                finish();
            } else if (id == R.id.sidenav_about) {
                Intent intent = new Intent(Homepage.this, about.class);
                startActivity(intent);
                // Handle about
            } else if (id == R.id.sidenav_help) {
                Intent intent = new Intent(Homepage.this, HelpActivity.class);
                startActivity(intent);
            } else if (id == R.id.sidenav_login) {
                Intent intent = new Intent(Homepage.this, Loginpage.class);
                startActivity(intent);
            }
            return true;
        }
    };

    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

}
