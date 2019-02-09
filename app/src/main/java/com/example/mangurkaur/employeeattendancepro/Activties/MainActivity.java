package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView empsign;
    private ImageView signout;
    private FirebaseAuth auth;
    private DatabaseReference connectedRef;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectedRef= FirebaseDatabase.getInstance().getReference(".info/connected");
        appLogin();
        empsign = (ImageView) findViewById(R.id.in);
        dateText=findViewById(R.id.date);
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM yyyy");
        dateText.setText(simpleDateFormat.format(date));
        empsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, InOutActivity.class);
                intent.putExtra("key","signIn");
                startActivity(intent);

            }
        });

        signout = (ImageView)findViewById(R.id.out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InOutActivity.class);
                intent.putExtra("key","signOut");
                startActivity(intent);
            }
        });


        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Boolean connected=dataSnapshot.getValue(Boolean.class);
                if(connected){

                    Toast.makeText(getApplicationContext(),"Internet is  connected",Toast.LENGTH_LONG).show();


                }
                else {

                    Toast.makeText(getApplicationContext(),"Internet  connection is lost ",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void appLogin() {


        auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("noreplydelight@gmail.com", "rootadmin123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
