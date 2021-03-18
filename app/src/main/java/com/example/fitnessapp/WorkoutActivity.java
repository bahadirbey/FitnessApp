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
    private TextView[] mDots;

    LinearLayout mDotsLayout;

    VideoView videoView;
    String videoPath;
    String videoNum;
    Button btnVideoSetter;
    String plan;

    ListView listView;
    String mainTitle[] = {"FirstDay", "1", "2", "3", "4", "5", "6", "7", "8"};
    String subTitle[] = {"", "1", "2", "3", "4", "5", "6", "7", "8"};

    String myPlanTitle[];
    String myPlanDes[];

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

        videoNum = "2131689473";
        videoView = findViewById(R.id.video_view);
        videoPath = "android.resource://" + getPackageName() + "/" + videoNum;

        setVideo(videoPath);

        myPlanTitle = mainTitle;
        myPlanDes = subTitle;

        if(InfoActivity.workoutPlan.equals("power")){

        }

        //Toast.makeText(WorkoutActivity.this,  "R.raw.video : " + R.raw.barbell_row, Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.list_item);
        ListAdapter adapter = new ListAdapter(this, myPlanTitle, myPlanDes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!myPlanTitle[position].contains("Day")){
                    getDatabaseReference(myPlanTitle[position]);
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

    public void setVideo(String videoPath){
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    public void getDatabaseReference(String child){
        String mail = InfoActivity.email.replace("@","").replace(".","");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mail).child("workout");
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