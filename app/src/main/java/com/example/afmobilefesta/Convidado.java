package com.example.afmobilefesta;

public class Convidado {
    private String id;
    private String nome;
    private Boolean conviteEnviado;
    private EPresenca presenca;

    public Convidado(){}
    public Convidado(String id, String nome, Boolean conviteEnviado, EPresenca presenca){
        this.id = id;
        this.nome = nome;
        this.conviteEnviado = conviteEnviado;
        this.presenca = presenca;
    }

    public String getId() { return id; }
    public Boolean getConviteEnviado() { return conviteEnviado; }
    public EPresenca getPresenca() { return presenca; }
    public String getNome() { return nome; }

    public void setId(String id) { this.id = id; }
}
