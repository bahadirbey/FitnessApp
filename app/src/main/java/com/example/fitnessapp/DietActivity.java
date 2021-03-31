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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessapp.fragments.diet.Diet_PageFragment1;
import com.example.fitnessapp.fragments.diet.Diet_PageFragment2;
import com.example.fitnessapp.fragments.diet.Diet_PageFragment3;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment1;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment2;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment3;
import com.example.fitnessapp.fragments.workout.Workout_PageFragment4;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class DietActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    ListView listView;

    String myDietTitle[] = {"There is no active program!"};
    String myDietSub[] = {"Make sure you have chosen your program!"};

    String twoThousandTitle[] = {
    "Kahvaltı", "1 porsiyon kahvaltılık menemen", "2 dilim az yağlı beyaz peynir", "2 dilim kepek ekmek", "5 adet zeytin", "1 fincan şekersiz çay ya da kahve",
    "Ara Öğün", "4 adet kuru kayısı", "10 adet badem", "1 bardak yarım yağlı süt",
    "Öğle Yemeği", "6 adet ızgara veya fırında ızgara köfte", "1 kase yoğurt", "2 dilim kepek ekmeği ya da 5 kaşık bulgur pilavı", "1 porsiyon mevsim salata",
    "Ara Öğün", "3 adet diyet bisküvi", "1 bardak light süt",
    "Akşam Yemeği", "1 porsiyon etli taze fasulye yemeği", "1 tane tam buğday ekmeği", "1 kase yoğurt",
    "Ara Öğün", "1 bardak tarçınlı süt", "1 adet küçük elma",
    "TOTAL"};

    String twoThousandSubTitle[]= {"" ,"177 cal", "122 cal", "110 cal", "25 cal", "0 cal",
    "", "100 cal", "120 cal", "94 cal",
    "", "300 cal", "112 cal", "110 cal", "59 cal",
    "", "51 cal", "69 cal",
    "", "158 cal", "103 cal", "112 cal",
    "", "120 cal", "47 cal"
    ,"1989 cal"};

    String fifteenHundredTitle[] = {"Kahvaltı", "1 adet haşlanmış yumurta", "1 dilim az yağlı beyaz peynir", "2 dilim kepek ekmek", "5 adet zeytin", "Domates, salatalık",
    "Ara Öğün", "2 adet ceviz", "1/2 bardak light süt",
    "Öğle Yemeği", "1 porsiyon tavuk haşlama", "4 kaşık bulgur pilavı", "1 bardak ayran",
    "Ara Öğün", "3 adet kuru kayısı", "2 adet ceviz",
    "Akşam Yemeği", "1 porsiyon yoğurtlu ıspanak yemeği", "1 kase sade brokoli çorbası veya 1 kepçe ev usulü ezogelin çorbası", "1 dilim kepek ekmek", "1/2 kase yoğurt",
    "Ara Öğün", "1 bardak tarçınlı süt"};

    String fifteenHundredSubTitle[] = {"", "", "", "", "", "",
    "", "", "",
    "", "", "", "",
    "", "", "",
    "", "", "", "", "",
    "", ""};

    String twentyfiveHundredTitle[] = {"Kahvaltı", "1 Büyük su bardağı şekersiz ve az yağlı süt", "2 ince dilim kepek ekmeği", "1 adet haşlanmış yumurta", "2 Kibrit kutusu büyüklüğünde beyaz peynir",
    "Ara Öğün", "1 Porsiyon karışık meyve tabağı",
    "Öğle Yemeği", "4 çorba kaşığı kadar mevsim sebzesi yemeği", "5 Adet az yağlı köfte", "3 Çorba kaşığı makarna", "2 İnce dilim kepek ekmeği ", "1 Kase yoğurt", "1 Porsiyon meyve",
    "Ara Öğün", "2 adet ceviz içi",
    "Akşam Yemeği", "6 yemek kaşığı mantar sote", "1 Porsiyon az yağlı pirinç pilavı", "Bol yeşillikli mevsim salatası", "1 kase kadar az yağlı yoğurt","1 İnce dilim çavdar veya tam buğday ekmeği"};

    String twentyfiveHundredSubTitle[] = {"", "", "", "", "",
    "", "",
    "", "", "", "", "", "", "",
    "", "",
    "", "", "", "", "", ""} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        navigationView.setCheckedItem(R.id.nav_diet);

        //Fragments
        List<Fragment> list = new ArrayList<>();
        list.add(new Diet_PageFragment1());
        list.add(new Diet_PageFragment2());
        list.add(new Diet_PageFragment3());

        pager = findViewById(R.id.pager_diet);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(viewListener);

        if (InfoActivity.diet_plan.equals("1500")){
            myDietTitle = fifteenHundredTitle;
            myDietSub = fifteenHundredSubTitle;
        }

        if(InfoActivity.diet_plan.equals("2000")){
            myDietTitle = twoThousandTitle;
            myDietSub = twoThousandSubTitle;
        }

        if(InfoActivity.diet_plan.equals("2500")){
            myDietTitle = twentyfiveHundredTitle;
            myDietSub = twentyfiveHundredSubTitle;
        }

        listView = findViewById(R.id.list_diet_item);
        ListAdapter adapter = new ListAdapter(this, myDietTitle, myDietSub);
        listView.setAdapter(adapter);
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
                Intent intent_home = new Intent(DietActivity.this, HomeActivity.class);
                startActivity(intent_home);
                break;
            case R.id.nav_workout:
                Intent intent_workout = new Intent(DietActivity.this, WorkoutActivity.class);
                startActivity(intent_workout);
                break;
            case R.id.nav_diet:
                break;
            case R.id.nav_cardio:
                Intent intent_cardio = new Intent(DietActivity.this, CardioActivity.class);
                startActivity(intent_cardio);
                break;
            case R.id.nav_plans:
                Intent intent_plans = new Intent( DietActivity.this, InfoActivity.class);
                startActivity(intent_plans);
                break;
            case R.id.nav_logout:
                Intent intent_logout = new Intent(DietActivity.this, SignInActivity.class);
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