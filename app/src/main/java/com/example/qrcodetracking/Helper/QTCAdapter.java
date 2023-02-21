package com.example.qrcodetracking.Helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodetracking.QrCodeTrackingDetail;
import com.example.qrcodetracking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QTCAdapter extends RecyclerView.Adapter<QTCAdapter.ViewHolder> {
    Context context;
    List<QCTModel> list;

    public QTCAdapter(Context context , List<QCTModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public QTCAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_id_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QTCAdapter.ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getID());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QrCodeTrackingDetail.class);
                intent.putExtra("detailed", list.get(holder.getAbsoluteAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textID);
        }
    }
}
