package com.example.qrcodetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class QrCodeGenerator extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    Button btnGenerate;
    EditText editText1, editText2, editText3, editText4;
    ImageView imageView;
    Toolbar toolbar;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator);
        btnGenerate = findViewById(R.id.btn_generate);
        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        editText3 = findViewById(R.id.edit_text3);
        editText4 = findViewById(R.id.edit_text4);
        imageView = findViewById(R.id.qr_image);
        toolbar = findViewById(R.id.detailed_toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data1 = editText1.getText().toString();
                String data2 = editText2.getText().toString();
                String data3 = editText3.getText().toString();
                String data4 = editText4.getText().toString();

                String dataAll = "Company = " + data1 + " , " + "Number =  " +  data2 + " , " + "Origin = " + data3 + " , " + "Weight = " + data4;

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = qrCodeWriter.encode(dataAll, BarcodeFormat.valueOf(BarcodeFormat.QR_CODE.toString()), 200, 200);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                // Display the QR code in the ImageView
                imageView.setImageBitmap(bmp);

                // Create HashMap to hold the data
                HashMap<String, Object> qrCode = new HashMap<>();

                String key = ref.push().getKey();

                // Create child node inside the HashMap data
                HashMap<String, String> qrCodeInfo = new HashMap<>();
                qrCodeInfo.put("Company ", data1 );
                qrCodeInfo.put("Number ", data2 );
                qrCodeInfo.put("Origin ", data3 );
                qrCodeInfo.put("Weight ", data4);

                // Add the child node to the main HashMap
                qrCode.put("data",qrCodeInfo);

                assert key != null;
                ref.child(key).setValue(qrCode).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(QrCodeGenerator.this, "Success add to firebase", Toast.LENGTH_SHORT).show();
                    }
                });

                String fileName = "QRCode_"+System.currentTimeMillis()+".jpg";
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), fileName);
                try {
                    // Create a file output stream and write the bitmap to it
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(QrCodeGenerator.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("","Permission Granted");
                } else {
                    Log.e("","Permission Denied");
                }
            }
            break;
        }
    }

}