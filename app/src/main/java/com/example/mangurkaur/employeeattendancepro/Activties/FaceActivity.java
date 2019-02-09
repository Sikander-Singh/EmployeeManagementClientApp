package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatSwitcher;
import io.fotoapparat.facedetector.Rectangle;
import io.fotoapparat.facedetector.processor.FaceDetectorProcessor;
import io.fotoapparat.facedetector.view.RectanglesView;
import io.fotoapparat.parameter.LensPosition;
import io.fotoapparat.photo.BitmapPhoto;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;

import static io.fotoapparat.log.Loggers.fileLogger;
import static io.fotoapparat.log.Loggers.logcat;
import static io.fotoapparat.log.Loggers.loggers;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.lensPosition;

public class FaceActivity extends AppCompatActivity {

    private final PermissionsDelegate permissionsDelegate = new PermissionsDelegate(this);
    private boolean hasCameraPermission;
    private CameraView cameraView;
    private RectanglesView rectanglesView;

    private FotoapparatSwitcher fotoapparatSwitcher;
    private Fotoapparat frontFotoapparat;
    private Fotoapparat backFotoapparat;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private  Intent intent;
    private boolean status=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        cameraView = (CameraView) findViewById(R.id.camera_view);
        rectanglesView = (RectanglesView) findViewById(R.id.rectanglesView);

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();


        hasCameraPermission = permissionsDelegate.hasCameraPermission();

        if (hasCameraPermission) {
            cameraView.setVisibility(View.VISIBLE);
        } else {
            permissionsDelegate.requestCameraPermission();
        }

        frontFotoapparat = createFotoapparat(LensPosition.FRONT);
        backFotoapparat = createFotoapparat(LensPosition.BACK);
        fotoapparatSwitcher = FotoapparatSwitcher.withDefault(frontFotoapparat);

        /*View switchCameraButton = findViewById(R.id.switchCamera);
        switchCameraButton.setVisibility(
                canSwitchCameras()
                        ? View.VISIBLE
                        : View.GONE
        );
        switchCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });*/

    }

   /* private boolean canSwitchCameras() {
        return frontFotoapparat.isAvailable() == backFotoapparat.isAvailable();
    }*/

    private Fotoapparat createFotoapparat(LensPosition position) {
        return Fotoapparat
                .with(this)
                .into(cameraView)
                .lensPosition(lensPosition(position))
                .frameProcessor(
                        FaceDetectorProcessor.with(this)
                                .listener(new FaceDetectorProcessor.OnFacesDetectedListener() {
                                    @Override
                                    public void onFacesDetected( List<Rectangle> faces) {

                                        Log.d("&&&", "Detected faces: " + faces.size());



                                        if(faces.isEmpty()){

                                            //nothing

                                        }
                                        else if(!status){

                                            Date date = new Date();
                                            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
                                            String dateStr = dt.format(date);
                                            intent=getIntent();
                                            String value=intent.getStringExtra("value");
                                            String empId=intent.getStringExtra("empId");
                                            if(value.equals("signIn")){


                                                intent.putExtra("backValue", "signIn");
                                                uploadPhoto(faces,empId+"signIn"+dateStr);

                                            }
                                            else if(value.equals("signOut")){


                                                intent.putExtra("backValue", "signOut");setResult(RESULT_OK,intent);
                                                uploadPhoto(faces,empId+"signOut"+dateStr);
                                            }


                                        }
                                    }
                                })
                                .build()
                )
                .logger(loggers(
                        logcat(),
                        fileLogger(this)
                ))
                .build();

    }

   /* private void switchCamera() {
        if (fotoapparatSwitcher.getCurrentFotoapparat() == frontFotoapparat) {
            fotoapparatSwitcher.switchTo(backFotoapparat);
        } else {
            fotoapparatSwitcher.switchTo(frontFotoapparat);
        }
    }*/

private void uploadPhoto(final List<Rectangle> faces, final String imageName){


    rectanglesView.setRectangles(faces);
    PhotoResult photoResult=frontFotoapparat.takePicture();
    photoResult
            .toBitmap()
            .whenAvailable(new PendingResult.Callback<BitmapPhoto>() {
                @Override
                public void onResult(BitmapPhoto bitmapPhoto) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapPhoto.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    storageReference.child(imageName).putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()){

                                status=true;
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                        }
                    });
                    }
            });


}

    @Override
    protected void onStart() {
        super.onStart();
        if (hasCameraPermission) {
            fotoapparatSwitcher.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (hasCameraPermission) {
            fotoapparatSwitcher.stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
            fotoapparatSwitcher.start();
            cameraView.setVisibility(View.VISIBLE);
        }
    }

}


