package br.com.alura.transacoes.model.transacao;

import java.math.BigDecimal;

public record DadosListarTransacoes(
        String bancoOrigem,
        String agenciaOrigem,
        String contaOrigem,
        String bancoDestino,
        String agenciaDestino,
        String contaDestino,
        BigDecimal valorTransacao
) {
    public DadosListarTransacoes(Transacao transacao){
        this(
                transacao.getContaOrigem().getBanco(),
                transacao.getContaOrigem().getAgencia(),
                transacao.getContaOrigem().getConta(),
                transacao.getContaDestino().getBanco(),
                transacao.getContaDestino().getAgencia(),
                transacao.getContaDestino().getConta(),
                transacao.getValorTransacao()
        );
    }
}
