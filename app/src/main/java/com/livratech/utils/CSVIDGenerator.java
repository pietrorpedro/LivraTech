package com.livratech.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVIDGenerator {

    public static int generate(String caminhoCSV) {
        int maiorId = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(caminhoCSV))) {
            String linha = br.readLine(); // ignora o cabeçalho

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] campos = linha.split(";");

                if (campos.length > 0) {
                    try {
                        int id = Integer.parseInt(campos[0].trim());
                        if (id > maiorId) {
                            maiorId = id;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("ID inválido na linha: " + linha);
                    }
                }
            }

        } catch (IOException e) {
            
        }

        return maiorId + 1;
    }

}
