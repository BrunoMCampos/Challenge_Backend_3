package br.com.alura.transacoes.model.transacao;

import java.math.BigDecimal;

public record DadosContasSuspeitas(
        String banco,
        String agencia,
        String conta,
        BigDecimal valorMovimentado,
        String tipoMovimentacao
) {
    public DadosContasSuspeitas(Conta conta, BigDecimal valorMovimentado, String tipoMovimentacao){
        this(
                conta.getBanco(),
                conta.getAgencia(),
                conta.getConta(),
                valorMovimentado,
                tipoMovimentacao
        );
    }
}
