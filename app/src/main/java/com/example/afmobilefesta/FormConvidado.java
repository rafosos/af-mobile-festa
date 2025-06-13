package com.example.afmobilefesta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class FormConvidado extends AppCompatActivity {
    ImageButton btnVoltar;
    Button btnSalvar;
    Button btnDeletar;
    Button btnCancelar;
    CheckBox checkConvite;
    EditText inputNome;
    ConvidadoAdapter adapter;
    List<Convidado> convidados = new ArrayList<>();
    Convidado convidado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_convidado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        convidado = new Convidado();
        convidado.setId(intent.getStringExtra("id_convidado"));
        convidado.setNome(intent.getStringExtra("nome_convidado"));
        convidado.setConvite(intent.getBooleanExtra("convite_convidado", false));
        convidado.setPresenca((EPresenca) intent.getSerializableExtra("presenca_convidado"));

        adapter = new ConvidadoAdapter(convidados,this);

        btnSalvar = findViewById(R.id.btn_salvar);
        btnDeletar = findViewById(R.id.btn_deletar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnVoltar = findViewById(R.id.btnVoltar);
        inputNome = findViewById(R.id.input_nome);
        checkConvite = findViewById(R.id.checkbox_convite);
        btnVoltar.setOnClickListener(l -> {finish();});
        btnCancelar.setOnClickListener(l -> {clearAndFinish();});
        btnSalvar.setOnClickListener(l -> {salvar();});
        btnDeletar.setOnClickListener(l -> {deletar();});

        populateFields();
    }

    private void populateFields(){
        inputNome.setText(convidado.getNome());
        checkConvite.setChecked(convidado.getConvite());
    }

    private void readFields(){
        convidado.setNome(inputNome.getText().toString());
        convidado.setConvite(checkConvite.isChecked());
    }

    private Integer clearAndFinish(){
        inputNome.setText("");
        checkConvite.setChecked(false);
        finish();
        return 0; //gambiarra
    }

    private void salvar(){
        readFields();
        adapter.salvarConvidado(convidado, this, this::clearAndFinish);
    }

    private void deletar() {
        adapter.deletarConvidado(convidado.getId(), 0);
        Toast.makeText(this, "Convidado deletado!", Toast.LENGTH_SHORT).show();
        clearAndFinish();
    }
}