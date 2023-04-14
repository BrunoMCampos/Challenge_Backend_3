package br.com.alura.transacoes.model.transacao;

import java.math.BigDecimal;

public record DadosContasSuspeitas(
        String banco,
        String agencia,
        String conta,
        BigDecimal valorMovimentado,
        String tipoMovimentacao
) {
}
