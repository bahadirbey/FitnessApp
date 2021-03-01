package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private Button signoutBtn;
    private FirebaseAuth mAuth;

    EditText mHeight, mWeight;
    Button savebtn;

    DatabaseReference reference;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signoutBtn = findViewById(R.id.signout_btn);
        mAuth = FirebaseAuth.getInstance();

        mHeight = findViewById(R.id.height_text);
        mWeight = findViewById(R.id.weight_text);
        savebtn = findViewById(R.id.save_btn);

        reference = FirebaseDatabase.getInstance().getReference().child("Member");

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = Integer.parseInt(mHeight.getText().toString().trim());
                int weight = Integer.parseInt(mWeight.getText().toString().trim());

                member = new Member(height, weight);

                reference.push().setValue(member);
                Toast.makeText(HomeActivity.this,"Your information is saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}