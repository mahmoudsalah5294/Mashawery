package com.mnm.mashawery.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mnm.mashawery.MainActivity;
import com.mnm.mashawery.databinding.ActivitySignupBinding;


public class Signup extends AppCompatActivity {

    ActivitySignupBinding signUpBinding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        signUpBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        signUpBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });



    }
    public void signup(View view){

        String email = signUpBinding.emailTxt.getText().toString();
        String password = signUpBinding.passwordTxt.getText().toString();
        String cPassword = signUpBinding.passwordTxt2.getText().toString();
        if (!email.isEmpty()|| !password.isEmpty()|| !cPassword.isEmpty()){
            if (password.length()>=6) {
                if (password.equals(cPassword)) {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = auth.getCurrentUser();
                                        startActivity(new Intent(Signup.this, Login.class));
                                    } /*else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Signup.this, "Authentication failed",
                                                Toast.LENGTH_SHORT).show();

                                    }*/

                                    // ...
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Signup.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else
                    Toast.makeText(this, "The passwords do not match", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Password must be at least 6 digits", Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this, "Please Fill the blanks", Toast.LENGTH_SHORT).show();
    }
}