package model;

public enum Status {
    ABERTO("Aberto"), EM_ANDAMENTO("Em andamento"), RESOLVIDO("Resolvido");

    private final String rotulo;
    Status(String rotulo) { this.rotulo = rotulo; }
    @Override public String toString() { return rotulo; }
}
