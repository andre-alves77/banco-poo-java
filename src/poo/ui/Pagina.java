package poo.ui;

public class Pagina {
    private final String titulo;
    private final Runnable acao;

    public static final Pagina SAIR = new Pagina("Sair", null);

    public Pagina(String titulo, Runnable acao) {
        this.titulo = titulo;
        this.acao = acao;
    }

    public String getTitulo() {
        return titulo;
    }

    public Runnable getAcao() {
        return acao;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pagina)) return false;
        Pagina outra = (Pagina) obj;
        return titulo.equals(outra.titulo);
    }

    @Override
    public int hashCode() {
        return titulo.hashCode();
    }

    @Override
    public String toString() {
        return titulo;
    }
}
