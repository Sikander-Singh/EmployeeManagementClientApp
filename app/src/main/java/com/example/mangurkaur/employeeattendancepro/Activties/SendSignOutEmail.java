package com.example.mangurkaur.employeeattendancepro.Activties;

import android.os.AsyncTask;
import android.util.Log;

public class SendSignOutEmail extends AsyncTask<String, Integer, Long> {
    @Override
    protected Long doInBackground(String... body) {
        try{
            GMailSender sender = new GMailSender("noreplydelight@gmail.com", "rootadmin123");
            sender.sendMail("Sign out "+body[2],"Hi "+body[1]+"\n"+"Your start time: "+body[2]+" "+body[3]+"\n"+"Your end time: "+body[2]+" "+body[3],
                    "noreplydelight@gmail.com",body[0]);
        }catch (Exception e){
            Log.e("Mail Error", e.getMessage());
        }
        return null;
    }
}
