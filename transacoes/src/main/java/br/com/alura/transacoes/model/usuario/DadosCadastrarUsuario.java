package br.com.alura.transacoes.model.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastrarUsuario(@NotBlank String nome,@NotBlank String email) {
}
