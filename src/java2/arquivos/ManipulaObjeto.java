/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2.arquivos;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

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
    private List<Cliente> clientesCache;
    private int currentIndex = -1;
    

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
                    oos = new AppendingObjectOutputStream(fos);
                    System.out.println("existe");
                } else {
                    fos = new FileOutputStream(arquivo, false);
                    oos = new ObjectOutputStream(fos);
                    System.out.println("NAO existe");
                }
                
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
            JOptionPane.showMessageDialog(null, "Cliente Gravado Com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
                clientesCache = new ArrayList<>();
                fis = new FileInputStream(arquivo);
                ois = new ObjectInputStream(fis);
                System.out.println("Mark supported? "+ois.markSupported());
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "Error ao Abrir Arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public Cliente lerRegistro() {
        Cliente cliente = new Cliente();
        boolean abriuArquivo = false;
        if (ois == null){
            abrirArquivo();
            abriuArquivo = true;
        }
        try {
            System.out.println(currentIndex);
            if(abriuArquivo == false && currentIndex + 1 < clientesCache.size()) {
                currentIndex++;
                return clientesCache.get(currentIndex);
            } else {
                cliente = (Cliente) ois.readObject();
                clientesCache.add(cliente);
                currentIndex++;
                return cliente;
            }
        } catch (EOFException endOfFileException) {
            JOptionPane.showMessageDialog(null, "Nao existem mais registros no arquivo.", "Fim do Arquivo", JOptionPane.INFORMATION_MESSAGE);
            return clientesCache.get(currentIndex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro durante a leitura do arquivo", "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return clientesCache.get(currentIndex);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro durante a leitura do arquivo", "Classe Cliente não encontrada", JOptionPane.ERROR_MESSAGE);
            return clientesCache.get(currentIndex);
        } 
    }

    @Override
    public Cliente lerRegistroAnterior() {
        System.out.println(currentIndex);
        if(clientesCache == null) {
            JOptionPane.showMessageDialog(null, "Carregue um arquivo com dados primeiro no botão Próximo!", "Erro", JOptionPane.ERROR_MESSAGE);
            return new Cliente();
        }
        if(currentIndex == 0) {
            JOptionPane.showMessageDialog(null, "Você está no inicio da Lista", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return clientesCache.get(0);
        }
        Cliente returnedClient = clientesCache.get(--currentIndex);
        System.out.println("ao final da operação: "+currentIndex);
        return returnedClient;
        
    }
}
