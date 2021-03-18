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

public class InfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String email;
    public static String workoutPlan;

    private Button savebtn;

    EditText mHeight, mWeight, mName;
    Spinner spinner;

    DatabaseReference reference;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mName = findViewById(R.id.name_text);
        mHeight = findViewById(R.id.height_text);
        mWeight = findViewById(R.id.weight_text);
        savebtn = findViewById(R.id.save_btn);
        spinner = findViewById(R.id.spinner_example);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.workoutplans, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("users");

                String name = mName.getText().toString().trim();
                String mail = email.replace("@","").replace(".", "");
                int height = Integer.parseInt(mHeight.getText().toString().trim());
                int weight = Integer.parseInt(mWeight.getText().toString().trim());

                member = new Member(name, email, height, weight);
                reference.child(mail).child("info").setValue(member);

                Toast.makeText(InfoActivity.this,"Your information is saved successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InfoActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}