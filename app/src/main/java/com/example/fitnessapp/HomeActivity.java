package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    private TextView welcome_text;
    private Button readBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        readBtn = findViewById(R.id.read_btn);
        welcome_text = findViewById(R.id.welcome_text);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        String mail = InfoActivity.email.replace("@","").replace(".","");
        Toast.makeText(HomeActivity.this,mail,Toast.LENGTH_SHORT).show();

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String mail = InfoActivity.email.replace("@","").replace(".","");
                        String name = dataSnapshot.child(mail).child("name").getValue().toString();
                        welcome_text.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}