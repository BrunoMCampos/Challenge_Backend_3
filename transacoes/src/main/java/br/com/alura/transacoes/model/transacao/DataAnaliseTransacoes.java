package br.com.alura.transacoes.model.transacao;

import jakarta.validation.constraints.Positive;

public record DataAnaliseTransacoes(
        @Positive(message = "Favor selecionar um ano!")
        Integer ano,
        @Positive(message = "Favor selecionar um mês!")
        Integer mes
) {
}
