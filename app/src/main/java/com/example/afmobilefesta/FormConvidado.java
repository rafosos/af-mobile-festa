package com.example.afmobilefesta;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FormConvidado extends AppCompatActivity {
    ImageButton btnVoltar;
    Button btnSalvar;
    Button btnDeletar;
    Button btnCancelar;
    CheckBox checkConvite;
    EditText inputNome;
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
    }

    private void clearAndFinish(){
        inputNome.setText("");
        checkConvite.setChecked(false);
        finish();
    }

    private void salvar(){

        clearAndFinish();
    }

    private void deletar(){
        clearAndFinish();
    }
}