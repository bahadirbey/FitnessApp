package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    VideoView videoView;
    String videoPath;
    String videoNum;

    ListView listView;

    String mainTitle[] = {"There is no active program!"};
    String subTitle[] = {"Make sure you have chosen your program!"};

    String powerTitle[] = {"FirstDay", "Squat", "Bench Press", "Pull up", "Second Day", "Squat", "Overhead Press", "Deadlift", "Dips", "Third Day", "Squat"
    , "Bench Press", "Barbell Row"};
    String powerSubTitle[] = {"", "5x5", "5x5", "5x5", "", "5x5", "5x5", "1x5", "5x5", "", "5x5", "5x5", "5x5"};
    int powerVideoNums[] = {0, R.raw.squat, R.raw.bench_press, R.raw.pull_up, 0, R.raw.squat, R.raw.ohp, R.raw.deadlift, R.raw.dips, 0, R.raw.squat, R.raw.bench_press,
    R.raw.barbell_row};

    String maintainTitle[] = {"First Day", "Bench Press", "Incline Bench Press", "Machine Flies", "Dumbbell Incline Press",
    "Second Day", "Pull up", "Lat Pulldown", "Barbell Row", "Deadlift",
    "Third Day", "Overhead Press", "Dumbbell Shoulder Press", "Lateral Raises", "Front Raises",
    "Fourth Day", "Barbell Biceps Curl", "Dumbbell Curl", "Cable Triceps Extension", "Push up",
    "Fifth Day", "Squat", "Lunges", "Machine Leg Curl", "Machine Calf Raises"};
    String maintainSubTitle[] = {"", "4x12", "4x8", "3x15", "4x10", "", "4xMax", "4x12", "3x15", "3x10", "", "4x10", "3x15", "3x20", "3x20",
            "", "4x20", "3x15", "4x20", "3xMax", "4x8", "3x15", "4x12", "3x20"};

    int maintainVideoNums[] = {0, R.raw.bench_press, R.raw.incline_bench_press, R.raw.machine_fly, R.raw.incline_dumbbell_press,
            0, R.raw.pull_up, R.raw.lat_pulldown, R.raw.barbell_row, R.raw.deadlift,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0};


    String fitTitle[] ={"First Day", "Bench Press", "Barbell Row", "Machine Fly", "Dumbbell Row",
            "Second Day", "Squat", "Machine Leg Curl", "Machine Calf Raises", "Lunges",
            "Third Day", "Triceps Pushdown", "Dumbbell Curls", "Lateral Raises", "Dumbbell Shoulder Press"};
    String fitSubTitle[] = {"", "10x10", "10x10", "3x12", "3x12", "", "10x10", "10x10", "3x10", "3x10", "", "10x10", "10x10", "4x12", "4x12"};

    int fitVideoNums[] = {0, R.raw.bench_press, R.raw.barbell_row, R.raw.machine_fly, R.raw.barbell_row,
    0, R.raw.squat, R.raw.squat, R.raw.squat, R.raw.squat,
    0, 0, 0, 0, 0};

    String myPlanTitle[];
    String myPlanDes[];
    int myPlanVideos[];

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

        //Fragments
        List<Fragment> list = new ArrayList<>();
        list.add(new Workout_PageFragment1());
        list.add(new Workout_PageFragment2());
        list.add(new Workout_PageFragment3());
        list.add(new Workout_PageFragment4());

        pager = findViewById(R.id.pager_workout);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(viewListener);

        //Videos
        videoNum = String.valueOf(R.raw.trailer);
        videoView = findViewById(R.id.video_view);
        videoPath = "android.resource://" + getPackageName() + "/" + videoNum;

        setVideo(videoPath);
        myPlanTitle = mainTitle;
        myPlanDes = subTitle;

        if(InfoActivity.workout_plan.equals("power")){
            myPlanTitle = powerTitle;
            myPlanDes = powerSubTitle;
            myPlanVideos = powerVideoNums;
        }

        if(InfoActivity.workout_plan.equals("fit")){
            myPlanTitle = fitTitle;
            myPlanDes = fitSubTitle;
            myPlanVideos = fitVideoNums;
        }

        if (InfoActivity.workout_plan.equals("maintain")){
            myPlanTitle = mainTitle;
            myPlanDes = maintainSubTitle;
            myPlanVideos = maintainVideoNums;
        }

        listView = findViewById(R.id.list_item);
        ListAdapter adapter = new ListAdapter(this, myPlanTitle, myPlanDes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!myPlanTitle[position].contains("Day")){
                    videoNum = String.valueOf(myPlanVideos[position]);
                    videoPath = "android.resource://" + getPackageName() + "/" + videoNum;
                    setVideo(videoPath);
                }
            }
        });
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
            case R.id.nav_plans:
                Intent intent_plans = new Intent( WorkoutActivity.this, InfoActivity.class);
                startActivity(intent_plans);
                break;
            case R.id.nav_logout:
                Intent intent_logout = new Intent(WorkoutActivity.this, SignInActivity.class);
                startActivity(intent_logout);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    public void setVideo(String videoPath){
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    class ListAdapter extends ArrayAdapter<String>{

        Context context;
        String rTitle[];
        String rDes[];

        ListAdapter(Context context, String title[], String des[]){
            super(context, R.layout.row, R.id.mainText, title);
            this.context = context;
            this.rTitle = title;
            this.rDes = des;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.row, parent, false);
            TextView title = view.findViewById(R.id.mainText);
            TextView des = view.findViewById(R.id.subText);

            title.setText(rTitle[position]);
            des.setText(rDes[position]);

            return view;
        }
    }
}