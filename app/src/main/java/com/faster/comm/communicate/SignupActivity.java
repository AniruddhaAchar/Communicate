package com.faster.comm.communicate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final Activity currentActivity = this;
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Button signup_btn = (Button)findViewById(R.id.signUpButton);
        final TextView name_tv = (TextView)findViewById(R.id.sign_upName);
        final TextView email_tv = (TextView)findViewById(R.id.sign_upEmail);
        final TextView password_tv = (TextView)findViewById(R.id.sign_upPassword);
        final TextView conf_password_tv = (TextView)findViewById(R.id.confirmPassword);
        final String TAG = "Sign Up Activity";

        //Add items to the spinners
        final Spinner userType_spn = (Spinner) findViewById(R.id.user_typeSpinner);
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,R.array.userType_array,android.R.layout.simple_spinner_item);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType_spn.setAdapter(userTypeAdapter);

        final Spinner department_spn = (Spinner)findViewById(R.id.departmentSpinner);
        ArrayAdapter<CharSequence> departmentArrayAdapter = ArrayAdapter.createFromResource(this, R.array.departments,android.R.layout.simple_spinner_item);
        departmentArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_spn.setAdapter(departmentArrayAdapter);

        final Spinner sem_spn = (Spinner)findViewById(R.id.semSpinner);
        ArrayAdapter<CharSequence> semArrayAdapter = ArrayAdapter.createFromResource(this,R.array.sem,android.R.layout.simple_spinner_item);
        semArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sem_spn.setAdapter(semArrayAdapter);
        final TextView sem_tv = (TextView)findViewById(R.id.sem_textView);

        //behaviour
        userType_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(userType_spn.getSelectedItem().toString().equals("Student")){
                   sem_spn.setVisibility(View.VISIBLE);
                   sem_tv.setVisibility(View.VISIBLE);
               }
                else{
                   sem_spn.setVisibility(View.GONE);
                   sem_tv.setVisibility(View.GONE);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(currentActivity,"Select a user type",Toast.LENGTH_LONG).show();
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name_str = name_tv.getText().toString();
                final String email_str = email_tv.getText().toString();
                String password_str = password_tv.getText().toString();
                String conf_password_str = conf_password_tv.getText().toString();
                final String userType_str = userType_spn.getSelectedItem().toString();
                final String dep_str = department_spn.getSelectedItem().toString();
                final String sem_str = sem_spn.getSelectedItem().toString();
                if(name_str.isEmpty()||email_str.isEmpty()||password_str.isEmpty()||conf_password_str.isEmpty()||userType_str.isEmpty()||dep_str.isEmpty()){
                    Toast.makeText(currentActivity,"All fields are mandatory",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!password_str.equals(conf_password_str)){
                        Toast.makeText(currentActivity,"Passwords don't match. Please try again",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mAuth.createUserWithEmailAndPassword(email_str,password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user = null;
                                    if(userType_str.equals("Teacher")){
                                        user = new User(name_str, "-1", email_str, userType_str, dep_str);
                                    }
                                    else{
                                        user = new User(name_str, sem_str, email_str, userType_str, dep_str);
                                    }
                                    FirebaseUser user_fb = mAuth.getCurrentUser();
                                    mDatabase.child("users").child(user_fb.getUid()).setValue(user);
                                    user_fb.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(currentActivity,"Verification sent to the email address. Please follow the link",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Log.w(TAG, "Signup onComplete: ", task.getException());
                                    Toast.makeText(currentActivity,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
        });

    }
}
