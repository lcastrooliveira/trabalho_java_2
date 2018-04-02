/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2.arquivos;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas.castro
 */
public class ManipulaObjeto implements ManipulaDados {
    
    private File arquivo;
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    
    private FileInputStream fis;
    private ObjectInputStream ois;

    @Override
    public void criarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showSaveDialog(null);//posiciona a janela no centro da tela
        //usando o this a janela é centrada na janela da aplicação. O FileChooser é modal
        if (result == JFileChooser.CANCEL_OPTION) {
            return;//finaliza a execuçao do metodo
        }
        arquivo = fileChooser.getSelectedFile();
        System.out.println(fileChooser.getName());

        if (arquivo == null || arquivo.getName().equals("")) {
            JOptionPane.showMessageDialog(null, "Nome de Arquivo Inválido", "Nome de Arquivo Inválido", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                if (arquivo.exists()) {
                    fos = new FileOutputStream(arquivo, true);
                    System.out.println("existe");
                } else {
                    fos = new FileOutputStream(arquivo, false);
                    System.out.println("NAO existe");
                }
                oos = new ObjectOutputStream(fos);
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "Erro ao Abrir Arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void fecharArquivo() {
        try {
            if (oos != null) {
                oos.close();
                fos.close();
            }
            if (ois != null) {
                ois.close();
                fis.close();
            }
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error ao Fechar Arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    @Override
    public void GravaCliente(Cliente cliente) {
        if (fos == null){
            criarArquivo();
        }
        try {
            oos.writeObject(cliente);
            oos.flush();
        } catch (IOException ioException) {
            fecharArquivo();
        }
    }

    @Override
    public void abrirArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        arquivo = fileChooser.getSelectedFile();
        System.out.println(arquivo);

        if (arquivo == null || arquivo.getName().equals("")) {
            JOptionPane.showMessageDialog(null, "Nome de Arquivo Inválido", "Nome de Arquivo Inválido", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                fis = new FileInputStream(arquivo);
                ois = new ObjectInputStream(fis);
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "Error ao Abrir Arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public Cliente lerRegistro() {
        Cliente cliente = new Cliente();
        if (ois == null){
            abrirArquivo();
        }
        try {
            cliente = (Cliente) ois.readObject();
            return cliente;
        } catch (EOFException endOfFileException) {
            JOptionPane.showMessageDialog(null, "Nao existem mais registros no arquivo.", "Fim do Arquivo", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro durante a leitura do arquivo", "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro durante a leitura do arquivo", "Classe Cliente não encontrada", JOptionPane.ERROR_MESSAGE);
        } finally {
            return cliente;
        }
    }
}
