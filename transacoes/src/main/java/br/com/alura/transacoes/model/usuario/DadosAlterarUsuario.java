package br.com.alura.transacoes.model.usuario;

import jakarta.validation.constraints.NotBlank;


public record DadosAlterarUsuario(@NotBlank(message = "O campo nome é obrigatório!") String nome, @NotBlank(message = "O campo email é obrigatório!") String email) {

}
