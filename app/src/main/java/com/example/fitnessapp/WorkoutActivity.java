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
import android.content.res.Resources;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.fitnessapp.fragments.workout.Workout_PageFragment1;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment2;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment3;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment4;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity{

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    private TextView[] mDots;

    LinearLayout mDotsLayout;

    VideoView videoView;
    String videoPath;
    String videoNum;
    Button btnVideoSetter;

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

        //Videos

        btnVideoSetter = findViewById(R.id.btn_videosetter);

        videoNum = "2131689472";
        videoView = findViewById(R.id.video_view);
        videoPath = "android.resource://" + getPackageName() + "/" + videoNum;

        setVideo(videoPath);

        //Toast.makeText(WorkoutActivity.this,  "R.raw.video : " + R.raw.video_two, Toast.LENGTH_SHORT).show();
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

    public void setVideoPath(View view){
        switch (view.getId()){
            case R.id.btn_videosetter:
                getDatabasebReference("1");
                videoPath = "android.resource://" + getPackageName() + "/" + videoNum;
                setVideo(videoPath);
                break;
        }
    }

    public void setVideo(String videoPath){
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    public void getDatabasebReference(String child){
        String mail = InfoActivity.email.replace("@","").replace(".","");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mail).child("plan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoNum = snapshot.child(child).getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}