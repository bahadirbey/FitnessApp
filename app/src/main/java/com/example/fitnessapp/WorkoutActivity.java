package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fitnessapp.fragments.workout.Workout_PageFragment1;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment2;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment3;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment4;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    private TextView[] mDots;

    LinearLayout mDotsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        navigationView.setCheckedItem(R.id.nav_workout);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);

        //Fragments
        List<Fragment> list = new ArrayList<>();
        list.add(new Workout_PageFragment1());
        list.add(new Workout_PageFragment2());
        list.add(new Workout_PageFragment3());
        list.add(new Workout_PageFragment4());

        pager = findViewById(R.id.pager_workout);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);

        mDotsLayout = findViewById(R.id.dots_layout_workout);
        addDotsIndicator(0);

        pager.addOnPageChangeListener(viewListener);
    }

    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent_home = new Intent(WorkoutActivity.this, HomeActivity.class);
                startActivity(intent_home);
                break;
            case R.id.nav_workout:
                break;
            case R.id.nav_diet:
                Intent intent_diet = new Intent(WorkoutActivity.this, DietActivity.class);
                startActivity(intent_diet);
                break;
            case R.id.nav_cardio:
                Intent intent_cardio = new Intent(WorkoutActivity.this, CardioActivity.class);
                startActivity(intent_cardio);
                break;
            case R.id.nav_logout:
                Intent intent_logout = new Intent(WorkoutActivity.this, SignInActivity.class);
                startActivity(intent_logout);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[4];
        mDotsLayout.removeAllViews();

        for(int i=0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.purple_200));

            mDotsLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.purple_500));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}