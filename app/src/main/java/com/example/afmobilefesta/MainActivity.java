package com.example.afmobilefesta;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    RecyclerView containerConvidados;
    EditText searchBar;
    LinearLayout containerStats;
    List<Convidado> convidados = new ArrayList<>();
    List<Convidado> convidadosFiltrados = new ArrayList<>();
    ConvidadoAdapter adapter;
    ImageButton btnAdd;
    TextView total;
    FirebaseFirestore db;
    EPresenca filtroAtivo;

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

        adapter = new ConvidadoAdapter(convidadosFiltrados, this);
        containerConvidados = findViewById(R.id.container_convidados);
        total = findViewById(R.id.total);
        containerConvidados.setAdapter(adapter);
        containerConvidados.setLayoutManager(new LinearLayoutManager(this));
        containerStats = findViewById(R.id.container_stats);
        containerStats.setOrientation(LinearLayout.HORIZONTAL);
        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                Log.d("debugggg", s.toString());
                convidadosFiltrados.clear();
                convidadosFiltrados.addAll(convidados.stream().filter(c -> c.getNome().contains(s.toString())).collect(Collectors.toList()));
                updateTotal();
                adapter.notifyDataSetChanged();
            }
        });

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(l -> {
            Intent intent = new Intent(MainActivity.this, FormConvidado.class);
            startActivity(intent);
        });

        carregarConvidados();
    }


    @Override
    protected void onResume() {
        super.onResume();
        carregarConvidados();
    }

    private void carregarConvidados() {
        db.collection("convidado")
                .get()
                .addOnSuccessListener(query -> {
                    Log.d("debugggg", "getConvidados");
                    convidados.clear();
                    convidadosFiltrados.clear();
                    searchBar.setText("");
                    for (QueryDocumentSnapshot doc : query) {
                        Convidado c = doc.toObject(Convidado.class);
                        c.setId(doc.getId());
                        Log.d("debugggg", c.getNome());
                        convidadosFiltrados.add(c);
                        convidados.add(c);
                        populateStats();
                    }
                    updateTotal();
                    adapter.notifyDataSetChanged();
                });
        adapter.setOnItemClickListener(convidado -> {
            Intent intent = new Intent(this, FormConvidado.class);
            intent.putExtra("id_convidado", convidado.getId());
            intent.putExtra("nome_convidado", convidado.getNome());
            intent.putExtra("convite_convidado", convidado.getConvite());
            intent.putExtra("presenca_convidado", convidado.getPresenca());
            startActivity(intent);
        });
    }

    private void updateTotal(){
        this.total.setText("Total: " + convidadosFiltrados.stream().count());
    }

    private void populateStats(){
        this.containerStats.removeAllViews();

        for (EPresenca s : EPresenca.values()) {
            LinearLayout container = new LinearLayout(this);
            container.setOnClickListener(l -> {
                if(filtroAtivo == s){
                    l.setBackground(AppCompatResources.getDrawable(this, R.drawable.white_border_blue_background));
                    filtroAtivo = null;
                    convidadosFiltrados.clear();
                    convidadosFiltrados.addAll(convidados);
                }else {
                    l.setBackground(AppCompatResources.getDrawable(this, R.drawable.yellow_border_blue_background));
                    filtroAtivo = s;
                    convidadosFiltrados.clear();
                    convidadosFiltrados.addAll(convidados.stream().filter(c -> c.getPresenca() == s).collect(Collectors.toList()));
                }
                adapter.notifyDataSetChanged();
                updateTotal();
            });
            container.setBackground(AppCompatResources.getDrawable(this, R.drawable.white_border_blue_background));
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f);
            container.setLayoutParams(params);
            container.setPadding(5,5,5,5);
            container.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(this);
            title.setText(s.toString());
            title.setTextColor(getResources().getColor(R.color.white, this.getTheme()));
            title.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            container.addView(title);

            TextView count = new TextView(this);
            count.setText(String.valueOf(convidadosFiltrados.stream().filter(c -> c.getPresenca() == s).count()));
            count.setTextColor(getResources().getColor(R.color.white, this.getTheme()));
            count.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            container.addView(count);
            this.containerStats.addView(container);
        };
    }
}