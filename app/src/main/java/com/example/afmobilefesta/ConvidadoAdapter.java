package com.example.afmobilefesta;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.Callable;

public class ConvidadoAdapter extends RecyclerView.Adapter<ConvidadoAdapter.ViewHolder> {
    private List<Convidado> convidados;
    private  FirebaseFirestore db;
    private final String COLLECTION_NAME = "convidado";
    public ConvidadoAdapter(List<Convidado> convidados){
        this.convidados = convidados;
        this.db = FirebaseFirestore.getInstance();
    }

    public interface OnItemClickListener{
        void onItemClick(Convidado convidado);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){ this.listener = listener; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Convidado c = convidados.get(position);
        holder.txt1.setText(c.getNome());
        holder.txt2.setText(c.getConviteEnviado().toString());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(c);
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            private long lastClickTime = 0;
            @Override
            public boolean onTouch(View v, android.view.MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    long currentTime = System.currentTimeMillis();
                    if(currentTime - lastClickTime < 300){
                        deletarConvidado(c.getId(), holder.getAdapterPosition(), v);
                    }
                    lastClickTime = currentTime;
                }
                return false;
            }
        });
    }

    private void deletarConvidado(String idDocumento, int position, View view){
        this.db.collection(COLLECTION_NAME)
                .document(idDocumento)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    convidados.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(view.getContext(), "Convidado deletado!", Toast.LENGTH_SHORT).show();
                });
    }

    public void salvarConvidado(Convidado c, Context context, Callable onSuccess){
        String convidadoId = c.getId();
        if(convidadoId == null || convidadoId.isBlank()){
            this.db.collection(COLLECTION_NAME)
                .add(c)
                .addOnSuccessListener(doc -> {
                    c.setId(doc.getId());
                    Toast.makeText(context, "Convidado adicionado!", Toast.LENGTH_SHORT).show();
                    try {
                        onSuccess.call();
                    } catch (Exception e) {
                        Log.d("debugggg", "Probelma no onsccess add");
                    }
                });
        } else {
            this.db.collection(COLLECTION_NAME)
                .document(c.getId())
                .set(c)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Convidado editado com sucesso.", Toast.LENGTH_SHORT).show();
                    try {
                        onSuccess.call();
                    } catch (Exception e) {
                        Log.d("debugggg", "Probelma no onsccess edit");
                    }
                });

        }
    }

    @Override
    public int getItemCount() {
        return convidados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt1, txt2;

        public ViewHolder(View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(android.R.id.text1);
            txt2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
