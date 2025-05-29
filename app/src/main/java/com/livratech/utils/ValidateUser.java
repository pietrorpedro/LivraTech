package com.livratech.utils;

import com.livratech.models.Funcionario;
import com.livratech.services.FuncionarioService;

public class ValidateUser {
    
    public static Funcionario validate(String name, String password) {
        FuncionarioService service = new FuncionarioService();
        Funcionario funcionario = service.encontrarFuncionarioPorNome(name);

        if (funcionario != null) {
            if (funcionario.getSenha().equals(password)) {
                return funcionario;
            } else {
                return null;
            }
        }

        return null;
    }
}
