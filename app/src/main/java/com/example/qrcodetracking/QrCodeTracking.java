package com.example.qrcodetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.qrcodetracking.Helper.QCTModel;
import com.example.qrcodetracking.Helper.QTCAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QrCodeTracking extends AppCompatActivity {

    RecyclerView recyclerView;
    QTCAdapter adapter;
    List<QCTModel> model;
    Toolbar toolbarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_tracking);
        toolbarr = findViewById(R.id.toolbar);

        toolbarr.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbarr.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        model = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleView);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    QCTModel data = new QCTModel(key);
                    model.add(data);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(QrCodeTracking.this));
                adapter = new QTCAdapter(QrCodeTracking.this,model);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(QrCodeTracking.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}