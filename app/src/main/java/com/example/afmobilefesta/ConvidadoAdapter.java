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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.concurrent.Callable;

public class ConvidadoAdapter extends RecyclerView.Adapter<ConvidadoAdapter.ViewHolder> {
    private Context context;
    private List<Convidado> convidados;
    private  FirebaseFirestore db;
    private final String COLLECTION_NAME = "convidado";
    public ConvidadoAdapter(List<Convidado> convidados, Context context){
        this.convidados = convidados;
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(Convidado convidado);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){ this.listener = listener; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_convidado, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Convidado c = convidados.get(position);
        holder.nome.setText(c.getNome());
        holder.statusConvite.setText(c.getConvite() ? "enviado" : "não enviado");
        EPresenca presenca = c.getPresenca();
        holder.statusPresenca.setText(presenca.toString());


        switch (presenca){
            case Confirmado:
                holder.statusPresenca.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));
                break;
            case Nao_respondido:
                holder.statusPresenca.setTextColor(context.getResources().getColor(R.color.grey, context.getTheme()));
                break;
            case Negado:
                holder.statusPresenca.setTextColor(context.getResources().getColor(R.color.red, context.getTheme()));
                break;
            case Talvez:
                holder.statusPresenca.setTextColor(context.getResources().getColor(R.color.yellow, context.getTheme()));
                break;
        }

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
                        deletarConvidado(c.getId(), holder.getAdapterPosition());
                    }
                    lastClickTime = currentTime;
                }
                return false;
            }
        });
    }

    public void deletarConvidado(String idDocumento, int position){
        this.db.collection(COLLECTION_NAME)
                .document(idDocumento)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    convidados.remove(position);
                    notifyItemRemoved(position);
                });
    }

    public void salvarConvidado(Convidado c, Context context, Callable<Integer> onSuccess){
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

    public void getConvidados(List<Convidado> listaConvidados, OnItemClickListener listener){
        db.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener(query -> {
                Log.d("debugggg", "getConvidados");
                listaConvidados.clear();
                for (QueryDocumentSnapshot doc : query) {
                    Convidado c = doc.toObject(Convidado.class);
                    c.setId(doc.getId());
                    Log.d("debugggg", c.getNome());
                    listaConvidados.add(c);
                    this.notifyDataSetChanged();
                }
            });
        setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return convidados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome, statusConvite, statusPresenca;

        public ViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome_convidado);
            statusConvite = itemView.findViewById(R.id.status_convite);
            statusPresenca = itemView.findViewById(R.id.status_presenca);
        }
    }
}
