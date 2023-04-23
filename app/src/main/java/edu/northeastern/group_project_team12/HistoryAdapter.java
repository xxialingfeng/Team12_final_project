package edu.northeastern.group_project_team12;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<String> mHistoryList;

    public HistoryAdapter(List<String> historyList) {
        mHistoryList = historyList;
    }
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.historyTextView.setText(mHistoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    // Delete a record and update the adapter
    public void deleteRecord(int position) {
        mHistoryList.remove(position);
        notifyItemRemoved(position);

        StorageReference reference = storage.getReference().child("username/" + mHistoryList.get(position));
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("delete file", "delete " + mHistoryList.get(position) + " successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("delete file", e.getMessage());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView historyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historyTextView = itemView.findViewById(R.id.history_text_view);
        }
    }
}
