package com.example.afmobilefesta;

public class Convidado {
    private String id;
    private String nome;
    private Boolean convite;
    private EPresenca presenca;

    public Convidado(){}
    public Convidado(String id, String nome, Boolean convite, EPresenca presenca){
        this.id = id;
        this.nome = nome;
        this.convite = convite;
        this.presenca = presenca;
    }

    public String getId() { return id; }
    public Boolean getConvite() { return convite; }
    public EPresenca getPresenca() { return presenca; }
    public String getNome() { return nome; }

    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setPresenca(EPresenca presenca) { this.presenca = presenca; }
    public void setConvite(Boolean convite) { this.convite = convite; }
}
