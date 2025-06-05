package com.example.afmobilefesta;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView containerConvidados;
    EditText searchBar;
    List<Convidado> convidados = new ArrayList<>();
    ConvidadoAdapter adapter;

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

        adapter = new ConvidadoAdapter(convidados);
        containerConvidados = findViewById(R.id.container_convidados);
        containerConvidados.setAdapter(adapter);
        searchBar = findViewById(R.id.search_bar);

        carregar
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