package com.example.qrcodetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qrcodetracking.Helper.QCTModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Map;

public class QrCodeTrackingDetail extends AppCompatActivity {

    Toolbar toolbar;
    QCTModel model = null;
    TextView company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_tracking_detail);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof QCTModel) {
            model = (QCTModel) obj;
        }

        if (model != null) {
            company.setText(model.getCompany());
        }
        /*DatabaseReference qrCodeRefRoot = FirebaseDatabase.getInstance().getReference();

        qrCodeRefRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the QR code image data from the snapshot
                //String qrCodeImageData = dataSnapshot.getValue(String.class);
                for ( DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Log.i("","" + key);
                    DatabaseReference qrCodeRef = FirebaseDatabase.getInstance().getReference(key).child("data");
                    Log.i("","" + qrCodeRef);
                    qrCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, Object> qrCodeImageData = (Map<String, Object>) snapshot.getValue();

                            //Convert the QR code image data to a bitmap
                            BitMatrix bitMatrix = null;
                            try {
                                bitMatrix = new MultiFormatWriter().encode(
                                        String.valueOf(qrCodeImageData), BarcodeFormat.QR_CODE, 100, 100);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap qrCode = barcodeEncoder.createBitmap(bitMatrix);

                            //Set the bitmap as the image of the ImageView
                            ImageView qrCodeImageView = findViewById(R.id.qr_image_data);
                            qrCodeImageView.setImageBitmap(qrCode);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                /*Map<String, String> qrCodeImageData = (Map<String, String>) dataSnapshot.getValue();

                 //Convert the QR code image data to a bitmap
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = new MultiFormatWriter().encode(
                            String.valueOf(qrCodeImageData), BarcodeFormat.QR_CODE, 100, 100);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap qrCode = barcodeEncoder.createBitmap(bitMatrix);

                 //Set the bitmap as the image of the ImageView
                ImageView qrCodeImageView = findViewById(R.id.qr_image_data);
                qrCodeImageView.setImageBitmap(qrCode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });*/
    }

    public void onBackPressed() {
        Intent intent = new Intent(QrCodeTrackingDetail.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}