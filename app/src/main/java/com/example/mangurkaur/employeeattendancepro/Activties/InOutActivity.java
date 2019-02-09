package com.example.mangurkaur.employeeattendancepro.Activties;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;



public class InOutActivity extends AppCompatActivity {

    private Button ok;
    private ImageView connfirm;
    private EditText empId;
    private ImageView signInImage;
    private ImageView signOutImage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    private Date date;
    private SimpleDateFormat dt;
    private  SimpleDateFormat dt1;
    private  String dateStr;
    private  String TimeStr;
    private  String str;
    private Intent intent;
    private boolean status=false;
    EmpClass empClass;
    TimeCardClass timeCardClass=new TimeCardClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out);
        ok =findViewById(R.id.done);
        connfirm=findViewById(R.id.confirm);
        empId=findViewById(R.id.empId);
        connfirm.setVisibility(View.GONE);
        signInImage=findViewById(R.id.signin);
        signOutImage=findViewById(R.id.signout);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        intent=getIntent();
        str = intent.getStringExtra("key");

        connfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(str.equals("signIn")){
            signInImage.setVisibility(View.VISIBLE);
            signOutImage.setVisibility(View.GONE);

        }
        else if(str.equals("signOut")){
            signInImage.setVisibility(View.GONE);
            signOutImage.setVisibility(View.VISIBLE);

        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = new Date();
                dt = new SimpleDateFormat("dd-MM-yyyy");
                dt1 = new SimpleDateFormat("HH:mm:ss");
                dateStr = dt.format(date);
                TimeStr = dt1.format(date);

                if(empId.getText().toString().isEmpty()){

                     //nothing
                }
                else{

                    // User Cannot Sign in and out multiple times
                    getEmployeeData(); //Check Sign in and out confirmation
                }

            }
        });

    }

    private void getEmployeeData() {

        myRef.child("Employee").orderByChild("empId").equalTo(empId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot empdatasnapshot : dataSnapshot.getChildren()) {

                    empClass = empdatasnapshot.getValue(EmpClass.class);
                    //Sign In
                    if (str.equals("signIn")) {
                        if (empId.getText().toString().equals(empClass.getEmpId() )) {

                            if(empClass.getStatusDate()==null){

                                //SendSignEmail sendSignEmail=new SendSignEmail();
                                //sendSignEmail.execute(empClass.getEmail(),empClass.getFirstName(),dateStr,TimeStr);
                                SignIn(empClass.getEmpId());
                            }
                            else {

                                if (empClass.getStatusDate().equals(dateStr)) {
                                    Toast.makeText(InOutActivity.this,"You can not sign in second time in a day",Toast.LENGTH_SHORT).show();

                                }
                                else {

                                     //SendSignEmail sendSignEmail=new SendSignEmail();
                                     //sendSignEmail.execute(empClass.getEmail(),dateStr,TimeStr);
                                    SignIn(empClass.getEmpId());

                                }
                            }
                        }

                    }
                    // Sign In

                    //Sign Out
                    if (str.equals("signOut")) {

                        if (empId.getText().toString().equals(empClass.getEmpId())&&empClass.getStatus().equals("Active")&&empClass.getStatusDate().equals(dateStr)) {

                            SignOut(empClass.getEmpId());
                        }
                    }

                    //Sign Out

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
    private void SignIn(String empId) {

        Intent intent =new Intent(InOutActivity.this,FaceActivity.class);
        intent.putExtra("value","signIn");
        intent.putExtra("empId",empId);
        startActivityForResult(intent,71);

    }
    private void SignOut(String empId) {

        Intent intent =new Intent(InOutActivity.this,FaceActivity.class);
        intent.putExtra("value","signOut");
        intent.putExtra("empId",empId);
        startActivityForResult(intent,71);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            final String value=data.getStringExtra("backValue");
            if(value.equals("signIn")){

                //Write sign in employee time card after signin
                String key = myRef.push().getKey();
                timeCardClass.setTimeCardId(key);
                timeCardClass.setEmpId(empClass.getEmpId());
                timeCardClass.setDate(dateStr);
                timeCardClass.setStartTime(TimeStr);
                myRef.child("TimeCard").child(key).setValue(timeCardClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){

                            Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                myRef.child("Notification").child(key).setValue(timeCardClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //Write sign in employee time card after signin

                //Update Employee status InActive to Active after signin
                empClass.setTimeCardId(key);
                empClass.setStatusDate(timeCardClass.getDate());
                empClass.setStatus("Active");
                myRef.child("Employee").child(empClass.getDataId()).setValue(empClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            connfirm.setVisibility(View.VISIBLE);
                            empId.setVisibility(View.GONE);
                            ok.setVisibility(View.GONE);

                        }
                        else{

                            Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                //Update Employee status InActive to Active after signin
            }
            else if(value.equals("signOut")){

                myRef.child("TimeCard").orderByChild("timeCardId").equalTo(empClass.getTimeCardId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot timedatasnapshot : dataSnapshot.getChildren()) {


                            //Update employee time card after signout
                            timeCardClass=timedatasnapshot.getValue(TimeCardClass.class);
                            SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
                            Date date = new Date();
                            String TimeStr = dt1.format(date);
                            timeCardClass.setEndTime(TimeStr);

                            myRef.child("Notification").child(empClass.getTimeCardId()).setValue(timeCardClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){

                                        Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                            myRef.child("TimeCard").child(empClass.getTimeCardId()).setValue(timeCardClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                }
                            });
                            //Update employee time card after signout

                            //Update employee status Active to InActive after signout
                            empClass.setStatusDate(timeCardClass.getDate());
                            empClass.setStatus("InActive");
                            myRef.child("Employee").child(empClass.getDataId()).setValue(empClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        connfirm.setVisibility(View.VISIBLE);
                                        empId.setVisibility(View.GONE);
                                        ok.setVisibility(View.GONE);
                                        //SendSignOutEmail sendSignOutEmail=new SendSignOutEmail();
                                        //sendSignOutEmail.execute(empClass.getEmail(),empClass.getFirstName(),timeCardClass.getDate(),timeCardClass.getStartTime(),timeCardClass.getEndTime());

                                    }
                                    else{

                                        Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                            myRef.child("Employee").child(empClass.getDataId()).child("timeCardId").removeValue();
                            //Update employee status Active to InActive after signout
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        }
    }
}
