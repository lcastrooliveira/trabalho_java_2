package java2.arquivos;

/**
 * @author lucas.castro
 */
public interface ManipulaDados {
    void criarArquivo();
    void fecharArquivo();
    void GravaCliente(Cliente cliente);
    void abrirArquivo();
    Cliente lerRegistro();
    Cliente lerRegistroAnterior();
}
