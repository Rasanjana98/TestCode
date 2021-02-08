package com.rasanjana.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {
    private Button mVerificationBtn;
    private EditText optEdit;
    private  String OTP;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mVerificationBtn =findViewById(R.id.verify_code_btn);
        optEdit=findViewById(R.id.verify_code_edit);
        OTP=getIntent().getStringExtra("auth");
        auth=FirebaseAuth.getInstance();
        mVerificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verification_code=optEdit.getText().toString();
                if(!verification_code.isEmpty()){
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(OTP,verification_code);
                    signIn(credential);
                }else{
                    Toast.makeText(OtpActivity.this, "Please enter the otp ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendToMain();
                }else{
                    Toast.makeText(OtpActivity.this, " Verification failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=auth.getCurrentUser();
        if(currentUser !=null){
            sendToMain();

        }
    }

    private  void sendToMain(){
        startActivity(new Intent(OtpActivity.this,MainActivity.class));
        finish();
    }
}