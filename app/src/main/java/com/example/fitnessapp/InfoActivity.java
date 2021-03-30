package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static String email;
    public static String workout_plan;
    public static String diet_plan;
    public static String cardio_plan;

    private Button save_btn;

    EditText mHeight, mWeight, mName;
    Spinner spinner_workout, spinner_diet, spinner_cardio;

    DatabaseReference reference;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mName = findViewById(R.id.name_text);
        mHeight = findViewById(R.id.height_text);
        mWeight = findViewById(R.id.weight_text);
        save_btn = findViewById(R.id.save_btn);

        spinner_workout = findViewById(R.id.spinner_workout);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.workout_plan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_workout.setAdapter(adapter);
        spinner_workout.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner_diet = findViewById(R.id.spinner_diet);
        ArrayAdapter<CharSequence> adapter_diet = ArrayAdapter.createFromResource(this, R.array.diet_plan, android.R.layout.simple_spinner_item);
        adapter_diet.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_diet.setAdapter(adapter_diet);
        spinner_diet.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner_cardio = findViewById(R.id.spinner_diet);
        ArrayAdapter<CharSequence> adapter_cardio = ArrayAdapter.createFromResource(this, R.array.cardio_plan, android.R.layout.simple_spinner_item);
        adapter_cardio.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_cardio.setAdapter(adapter_diet);
        spinner_cardio.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("users");

                String name = mName.getText().toString().trim();
                String mail = email.replace("@","").replace(".", "");
                int height = Integer.parseInt(mHeight.getText().toString().trim());
                int weight = Integer.parseInt(mWeight.getText().toString().trim());

                member = new Member(name, email, height, weight);
                reference.child(mail).child("info").setValue(member);
                reference.child(mail).child("workout").child("plan").setValue(workout_plan);

                int caloriePerDay = (int) (10 * height + 6.25 * weight + 5);
                if (diet_plan == "lose"){
                    if(caloriePerDay < 2000){
                        reference.child(mail).child("diet").child("plan").setValue("1500");
                    }else if(caloriePerDay > 2000 && caloriePerDay < 2500){
                        reference.child(mail).child("diet").child("plan").setValue("2000");
                    }else{
                        reference.child(mail).child("diet").child("plan").setValue("2500");
                    }
                }else if (diet_plan == "gain"){
                    if(caloriePerDay < 1500){
                        reference.child(mail).child("diet").child("plan").setValue("1500");
                    }else if(caloriePerDay > 1500 && caloriePerDay < 2000){
                        reference.child(mail).child("diet").child("plan").setValue("2000");
                    }else{
                        reference.child(mail).child("diet").child("plan").setValue("2500");
                    }
                }else {
                    if (caloriePerDay < 1750){
                        reference.child(mail).child("diet").child("plan").setValue("1500");
                    }else if(caloriePerDay > 1750 && caloriePerDay < 2250){
                        reference.child(mail).child("diet").child("plan").setValue("2000");
                    }else{
                        reference.child(mail).child("diet").child("plan").setValue("2500");
                    }
                }

                Toast.makeText(InfoActivity.this,"Your information is saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).toString().contains("stronger")){
            workout_plan = "power";
        }else if (parent.getItemAtPosition(position).toString().contains("fit")){
            workout_plan = "fit";
        }else if (parent.getItemAtPosition(position).toString().contains("maintain")){
            workout_plan = "maintain";
        }

        //BMR = 10Weight + 6.25Height - 5Activity + 5
        if (parent.getItemAtPosition(position).toString().contains("lose")){
            diet_plan = "lose";
        }else if (parent.getItemAtPosition(position).toString().contains("gain")){
            diet_plan = "gain";
        }else{
            diet_plan = "maintain";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}