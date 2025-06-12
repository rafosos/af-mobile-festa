package com.example.afmobilefesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView containerConvidados;
    EditText searchBar;
    List<Convidado> convidados = new ArrayList<>();
    ConvidadoAdapter adapter;
    ImageButton btnAdd;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        adapter = new ConvidadoAdapter(convidados, this);
        containerConvidados = findViewById(R.id.container_convidados);
        containerConvidados.setAdapter(adapter);
        searchBar = findViewById(R.id.search_bar);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(l -> {
            Intent intent = new Intent(MainActivity.this, FormConvidado.class);
            startActivity(intent);
        });


        carregarConvidados();
//        adapter.getConvidados(convidados, convidado -> {
//            //TODO - enviar para activity de edição
//        });
//        adapter.notifyDataSetChanged();
    }

    private void carregarConvidados(){
        db.collection("convidado")
                .get()
                .addOnSuccessListener(query -> {
                    Log.d("debugggg", "getConvidados");
                    convidados.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        Convidado c = doc.toObject(Convidado.class);
                        c.setId(doc.getId());
                        Log.d("debugggg", c.getNome());
                        convidados.add(c);
                        adapter.notifyDataSetChanged();
                    }
                });
        adapter.setOnItemClickListener(convidado -> {
            //TODO - enviar para activity de edição
        });
    }


    private LinearLayout buildCard(Convidado c){
        LinearLayout card = new LinearLayout(this);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, //width
                ViewGroup.LayoutParams.WRAP_CONTENT //height
        ));
        card.setOrientation(LinearLayout.VERTICAL);

        TextView nome = new TextView(this);
        nome.setText(c.getNome());
        nome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);

        return card;
    }


}