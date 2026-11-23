package model;

public enum Prioridade {
    BAIXA("Baixa"), MEDIA("Média"), ALTA("Alta");

    private final String rotulo;
    Prioridade(String rotulo) { this.rotulo = rotulo; }
    @Override public String toString() { return rotulo; }
}
