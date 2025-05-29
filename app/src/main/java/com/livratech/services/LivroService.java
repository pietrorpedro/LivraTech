package com.livratech.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.livratech.models.Livro;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class LivroService {
    
    private static final String CAMINHO = "app\\src\\main\\java\\com\\livratech\\data\\livros.csv";

        public void inserirLivro(Livro livro) {
        try {
            Files.createDirectories(Paths.get("app\\src\\main\\java\\com\\livratech\\data"));
            boolean arquivoExiste = Files.exists(Paths.get(CAMINHO));

            Writer writer = new FileWriter(CAMINHO, true);
            StatefulBeanToCsv<Livro> beanToCsv = new StatefulBeanToCsvBuilder<Livro>(writer)
                    .withSeparator(';')
                    .withApplyQuotesToAll(false)
                    .withOrderedResults(true)
                    .build();

            if (!arquivoExiste) {
                writer.write("id;titulo;autor;categoriaLivro;preco;numeroPaginas;estoque\n");
            }

            beanToCsv.write(livro);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Livro encontrarLivroPorId(int id) {
        try (Reader reader = new FileReader(CAMINHO)) {
            CsvToBean<Livro> csvToBean = new CsvToBeanBuilder<Livro>(reader)
                    .withType(Livro.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Livro> livros = csvToBean.parse();
            return livros.stream()
                    .filter(l -> l.getId() == id)
                    .findFirst()
                    .orElse(null);

        } catch (IOException e) {
            System.out.println("Livro não encontrado.");
            return null;
        }
    }


    public void atualizarEstoque(int id, boolean adicionar, int quantidade) {
    try {
        Reader reader = new FileReader(CAMINHO);
        CsvToBean<Livro> csvToBean = new CsvToBeanBuilder<Livro>(reader)
                .withType(Livro.class)
                .withSkipLines(1)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<Livro> livros = csvToBean.parse();
        reader.close();

        boolean alterado = false;

        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);

            if (livro.getId() == id) {
                if (adicionar) {
                    livro.setEstoque(livro.getEstoque() + quantidade);
                    alterado = true;
                } else {
                    if (livro.getEstoque() < quantidade) {
                        System.out.println("Quantidade insuficiente");
                        return;
                    } else if (livro.getEstoque() == quantidade) {
                        livros.remove(i);
                    } else {
                        livro.setEstoque(livro.getEstoque() - quantidade);
                    }
                    alterado = true;
                }
                break;
            }
        }

        if (!alterado) {
            System.out.println("Livro não encontrado");
            return;
        }

        Writer writer = new FileWriter(CAMINHO);
        writer.write("id;titulo;autor;categoriaLivro;preco;numeroPaginas;estoque\n");

        StatefulBeanToCsv<Livro> beanToCsv = new StatefulBeanToCsvBuilder<Livro>(writer)
                .withSeparator(';')
                .withApplyQuotesToAll(false)
                .withOrderedResults(true)
                .build();

        beanToCsv.write(livros);
        writer.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
