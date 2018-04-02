/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2.arquivos;

/**
 *
 * @author lucas.castro
 */
public interface ManipulaDados {
    void criarArquivo();
    void fecharArquivo();
    void GravaCliente(Cliente cliente);
    void abrirArquivo();
    Cliente lerRegistro();
}
