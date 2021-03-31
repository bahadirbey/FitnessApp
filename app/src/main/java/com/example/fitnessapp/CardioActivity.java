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
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.fitnessapp.fragments.cardio.Cardio_PageFragment1;
import com.example.fitnessapp.fragments.cardio.Cardio_PageFragment2;
import com.example.fitnessapp.fragments.cardio.Cardio_PageFragment3;
import com.example.fitnessapp.fragments.cardio.Cardio_PageFragment4;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class CardioActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    VideoView videoView;
    String videoPath;
    String videoNum;

    ListView listView;

    String cardioTitle[] = {"Kilo Vermek İsteyenler İçin", "Spora Yeni Başlayanlar İçin", "HIIT Kardiyo", "Evde Kardiyo İdmanı", "Evde Karın İdmanı"};
    String cardioSubTitle[] = {"Evde Kardiyo Egzersizleri", "Ev Antrenmanı", "Ev İdmanı", "Fazlalık Kilolarından Kurtul", "5 Dakikada Karın Kaslarınızı Güçlendirin"};
    int videoPlan[] = {R.raw.cardio_one, R.raw.cardio_two, R.raw.cardio_three, R.raw.cardio_four, R.raw.cardio_five};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        navigationView.setCheckedItem(R.id.nav_cardio);

        //Fragments
        List<Fragment> list = new ArrayList<>();
        list.add(new Cardio_PageFragment1());
        list.add(new Cardio_PageFragment2());
        list.add(new Cardio_PageFragment3());
        list.add(new Cardio_PageFragment4());

        pager = findViewById(R.id.pager_cardio);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(viewListener);

        videoNum = String.valueOf(R.raw.cardio_trailer);
        videoView = findViewById(R.id.video_view_cardio);
        videoPath = "android.resource://" + getPackageName() + "/" + videoNum;
        setVideo(videoPath);

        listView = findViewById(R.id.list_cardio_item);
        ListAdapter adapter = new ListAdapter(this, cardioTitle, cardioSubTitle);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    videoNum = String.valueOf(videoPlan[position]);
                    videoPath = "android.resource://" + getPackageName() + "/" + videoNum;
                    setVideo(videoPath);
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
                Intent intent_home = new Intent(CardioActivity.this, HomeActivity.class);
                startActivity(intent_home);
                break;
            case R.id.nav_workout:
                Intent intent_workout = new Intent(CardioActivity.this, WorkoutActivity.class);
                startActivity(intent_workout);
                break;
            case R.id.nav_diet:
                Intent intent_diet = new Intent(CardioActivity.this, DietActivity.class);
                startActivity(intent_diet);
                break;
            case R.id.nav_cardio:
                break;
            case R.id.nav_plans:
                Intent intent_plans = new Intent( CardioActivity.this, InfoActivity.class);
                startActivity(intent_plans);
                break;
            case R.id.nav_logout:
                Intent intent_logout = new Intent(CardioActivity.this, SignInActivity.class);
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
        public void onPageSelected(int position) {}

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

    class ListAdapter extends ArrayAdapter<String> {

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