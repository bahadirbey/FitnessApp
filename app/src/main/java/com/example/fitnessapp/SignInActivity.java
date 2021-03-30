package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private EditText mEmail, mPass;
    private TextView mTextView;
    private Button signInBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = findViewById(R.id.email_signin);
        mPass = findViewById(R.id.pass_signin);
        mTextView = findViewById(R.id.textView2);
        signInBtn = findViewById(R.id.signin_btn);

        mAuth = FirebaseAuth.getInstance();

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser(){
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if(!pass.isEmpty()){
                if (mAuth.getCurrentUser() != null){
                    InfoActivity.email = email;
                    Toast.makeText(SignInActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
                    String mail = email.replace("@","").replace(".","");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(mail).exists()){
                                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                getDatabaseReferencePlan();
                                finish();
                            }else{
                                startActivity(new Intent(SignInActivity.this, InfoActivity.class));
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(SignInActivity.this, "Cannot Log In!", Toast.LENGTH_SHORT).show();
                }
            }else{
                mPass.setError("Please Enter Your Password!");
            }
        }else if(email.isEmpty()){
            mEmail.setError("Please Enter Correct Email!");
        }
    }

    public void getDatabaseReferencePlan(){
        String mail = InfoActivity.email.replace("@","").replace(".","");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mail);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("workout").child("plan").exists()){
                    InfoActivity.workout_plan = snapshot.child("workout").child("plan").getValue().toString();
                }

                if (snapshot.child("diet").child("plan").exists()){
                    InfoActivity.diet_plan = snapshot.child("diet").child("plan").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}