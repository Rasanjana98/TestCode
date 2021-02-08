package com.rasanjana.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginActivity extends AppCompatActivity {
    private Button mSendOtpBtn;
    private TextView processText;
    private EditText countryCode,PhoneNumberInput;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSendOtpBtn=findViewById(R.id.send_codebtn);
        processText=findViewById(R.id.textProcess);
        countryCode=findViewById(R.id.input_contry_code);
        PhoneNumberInput=findViewById(R.id.input_phone);
        auth=FirebaseAuth.getInstance();

        mSendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country_code=countryCode.getText().toString();
                String phone =PhoneNumberInput.getText().toString();
                String phoneNumber = "+" + country_code + "" + phone;
                if(!country_code.isEmpty() || !phone.isEmpty()){
                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(loginActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else{
                    processText.setText("Please enter country code and phone  number");
                    processText.setTextColor(Color.RED);
                    processText.setVisibility(View.VISIBLE);
                }
            }
        });
        mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            processText.setText(e.getMessage());
            processText.setTextColor(Color.RED);
            processText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                processText.setText("OTP has been sent");
                processText.setVisibility(View.VISIBLE);

                Intent otpIntent =new Intent(loginActivity.this,OtpActivity.class);
                otpIntent.putExtra("auth",s);
                startActivity(otpIntent);






            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if (user !=null){
            sendToMain();
        }

    }
    private void sendToMain(){
        Intent mainIntent =new Intent(loginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private  void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendToMain();
                }else{
                    processText.setText(task.getException().getMessage());
                    processText.setTextColor(Color.RED);
                    processText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}