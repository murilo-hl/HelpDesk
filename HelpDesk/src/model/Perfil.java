package model;

public enum Perfil {
    USUARIO("Usuário"), TECNICO("Técnico"), ADMIN("Administrador");

    private final String rotulo;
    Perfil(String rotulo) { this.rotulo = rotulo; }
    @Override public String toString() { return rotulo; }
}
