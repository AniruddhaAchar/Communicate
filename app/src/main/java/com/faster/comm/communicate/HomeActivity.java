package com.faster.comm.communicate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private String TAG = "LOGIN Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Activity currentActivity = this;
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails",MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid","");
        Log.d(TAG, "onCreate: UID="+uid);
        if(uid!=""){
            Log.d(TAG, "onCreate: User created and saved");
            Intent intent = new Intent(currentActivity, UserHome.class);
            startActivity(intent);
            finish();
        }
        else{
            Log.d(TAG, "onCreate: User needs to login");
        }
        final TextView loginEmail_tv = (TextView) findViewById(R.id.loginEmail);
        final TextView loginPassword_tv = (TextView) findViewById(R.id.loginPassword);
        Button login_bt = (Button) findViewById(R.id.loginButton);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button signUp_bt = (Button) findViewById(R.id.signupButton);
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_str = loginEmail_tv.getText().toString();
                String password_str = loginPassword_tv.getText().toString();
                if(email_str.isEmpty()||password_str.isEmpty()){
                    Toast.makeText(currentActivity,"All fields are mandatory",Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email_str,password_str).addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()){
                            Log.w(TAG, "signInWithEmail:failed",task.getException() );
                            Toast.makeText(currentActivity, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final Intent intent = new Intent(currentActivity, UserHome.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                

            }
        });
        signUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
