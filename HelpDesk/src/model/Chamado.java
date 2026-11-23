package model;

import java.time.LocalDateTime;

public class Chamado {
    private int id;
    private String titulo;
    private String descricao;
    private Prioridade prioridade = Prioridade.MEDIA;
    private Status status = Status.ABERTO;
    private String solucao;
    private int usuarioId;
    private String usuarioNome;     // preenchido pelo JOIN
    private Integer tecnicoId;      // pode ser nulo (ainda sem técnico)
    private String tecnicoNome;     // preenchido pelo JOIN
    private LocalDateTime dataAbertura;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getSolucao() { return solucao; }
    public void setSolucao(String solucao) { this.solucao = solucao; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    public Integer getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Integer tecnicoId) { this.tecnicoId = tecnicoId; }
    public String getTecnicoNome() { return tecnicoNome; }
    public void setTecnicoNome(String tecnicoNome) { this.tecnicoNome = tecnicoNome; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
}
