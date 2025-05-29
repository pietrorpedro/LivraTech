package com.livratech.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.livratech.models.Cliente;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class ClienteService {
    
    private static final String CAMINHO = "app\\src\\main\\java\\com\\livratech\\data\\clientes.csv";

        public void inserirCliente(Cliente cliente) {
        try {
            // Garante que a pasta exista
            Files.createDirectories(Paths.get("app\\src\\main\\java\\com\\livratech\\data"));

            boolean arquivoExiste = Files.exists(Paths.get(CAMINHO));

            Writer writer = new FileWriter(CAMINHO, true);
            StatefulBeanToCsv<Cliente> beanToCsv = new StatefulBeanToCsvBuilder<Cliente>(writer)
                    .withSeparator(';')
                    .withApplyQuotesToAll(false)
                    .withOrderedResults(true)
                    .build();

            if (!arquivoExiste) {
                writer.write("id;nome;clienteVip;CPF;endereco\n");
            }

            beanToCsv.write(cliente);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cliente encontrarClientePorId(int id) {
        try {
            Reader reader = new FileReader(CAMINHO);

            CsvToBean<Cliente> csvToBean = new CsvToBeanBuilder<Cliente>(reader)
                    .withType(Cliente.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Cliente> clientes = csvToBean.parse();
            reader.close();

            return clientes.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

        public Cliente encontrarClientePorCPF(String cpf) {
        try {
            Reader reader = new FileReader(CAMINHO);

            CsvToBean<Cliente> csvToBean = new CsvToBeanBuilder<Cliente>(reader)
                    .withType(Cliente.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Cliente> clientes = csvToBean.parse();
            reader.close();

            return clientes.stream()
                    .filter(c -> c.getCPF().equals(cpf))
                    .findFirst()
                    .orElse(null);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
