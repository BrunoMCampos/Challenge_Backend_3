package br.com.alura.transacoes.model.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastrarUsuario(@NotBlank(message = "O campo nome é obrigatório!") String nome,@NotBlank(message = "O campo e-mail é obrigatório!") String email) {
}
