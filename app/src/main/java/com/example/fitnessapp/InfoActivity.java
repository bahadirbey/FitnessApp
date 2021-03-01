package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InfoActivity extends AppCompatActivity {

    public static String email;

    private Button signoutBtn;
    private FirebaseAuth mAuth;

    TextView mail_text;
    EditText mHeight, mWeight, mName;
    Button savebtn;

    DatabaseReference reference;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        signoutBtn = findViewById(R.id.signout_btn);
        mAuth = FirebaseAuth.getInstance();


        mName = findViewById(R.id.name_text);
        mHeight = findViewById(R.id.height_text);
        mWeight = findViewById(R.id.weight_text);
        savebtn = findViewById(R.id.save_btn);
        mail_text = findViewById(R.id.text_mail);

        String mail = email.replace("@","").replace(".", "");
        mail_text.setText(mail);


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("users");

                String name = mName.getText().toString().trim();

                int height = Integer.parseInt(mHeight.getText().toString().trim());
                int weight = Integer.parseInt(mWeight.getText().toString().trim());
                String mails = mail_text.getText().toString().trim();
                member = new Member(name, email, height, weight);
                reference.child(mails).setValue(member);

                Toast.makeText(InfoActivity.this,"Your information is saved successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InfoActivity.this, HomeActivity.class));
                finish();
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(InfoActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}