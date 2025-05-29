package com.livratech.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.livratech.models.Funcionario;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class FuncionarioService {

    private static final String CAMINHO = "app\\src\\main\\java\\com\\livratech\\data\\funcionarios.csv";

    public void inserirFuncionario(Funcionario funcionario) {
        try {
            // Cria diretório se não existir
            Files.createDirectories(Paths.get("data"));

            // Verifica se o arquivo já existe
            boolean arquivoExiste = Files.exists(Paths.get(CAMINHO));

            Writer writer = new FileWriter(CAMINHO, true);
            StatefulBeanToCsv<Funcionario> beanToCsv = new StatefulBeanToCsvBuilder<Funcionario>(writer)
                    .withSeparator(';')
                    .withApplyQuotesToAll(false)
                    .withOrderedResults(true)
                    .build();

            if (!arquivoExiste) {
                writer.write("id;nome;senha;funcionarioTipo\n");
            }

            beanToCsv.write(funcionario);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public Funcionario encontrarFuncionarioPorId(int id) {
        try {
            Reader reader = new FileReader(CAMINHO);

            CsvToBean<Funcionario> csvToBean = new CsvToBeanBuilder<Funcionario>(reader)
                .withType(Funcionario.class)
                .withSkipLines(1)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .build();

            List<Funcionario> funcionarios = csvToBean.parse();
            reader.close();

            return funcionarios.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
