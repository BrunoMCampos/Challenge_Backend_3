package br.com.alura.transacoes.model.transacao;

import jakarta.validation.constraints.NotNull;

public record DataAnaliseTransacoes(
        @NotNull
        Integer ano,
        @NotNull
        Integer mes
) {
}
