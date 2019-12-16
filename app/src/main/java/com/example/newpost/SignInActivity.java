package com.example.newpost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    EditText emailText,passwordText;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth=FirebaseAuth.getInstance();
        emailText=findViewById(R.id.emailText);
        passwordText=findViewById(R.id.passwordText);

        firebaseUser=firebaseAuth.getCurrentUser();  //kullanıcı daha önceden giriş yapmışsa
        if(firebaseUser!=null){
            Intent intent=new Intent(SignInActivity.this,FeedAvtivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void signinClick(View view){
        String email=emailText.getText().toString();
        String password=passwordText.getText().toString();

        if(email.matches("")){
            Toast.makeText(SignInActivity.this,"Enter Email",Toast.LENGTH_LONG).show();

        }
        else if(password.matches("")){
            Toast.makeText(SignInActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
        }
        else if(password.matches("")&& email.matches(""))
        {
            Toast.makeText(SignInActivity.this,"Enter Email and Password",Toast.LENGTH_LONG).show();
        }

        else{

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent=new Intent(SignInActivity.this,FeedAvtivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });

    }}
    public void signupClick(View view){
        String email=emailText.getText().toString();
        String password=passwordText.getText().toString();
        if(email.matches("")){
            Toast.makeText(SignInActivity.this,"Enter Email",Toast.LENGTH_LONG).show();
        }
        else if(password.matches("")){
            Toast.makeText(SignInActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
        }
        else if(password.matches("")&& email.matches("")){
            Toast.makeText(SignInActivity.this,"Enter Email and Password",Toast.LENGTH_LONG).show();
        }
        else{

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignInActivity.this,"User Created",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SignInActivity.this,FeedAvtivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });

    }}
}
