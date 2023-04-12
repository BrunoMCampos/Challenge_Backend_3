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
                transacao.getBancoOrigem(),
                transacao.getAgenciaOrigem(),
                transacao.getContaOrigem(),
                transacao.getBancoDestino(),
                transacao.getAgenciaDestino(),
                transacao.getContaDestino(),
                transacao.getValorTransacao()
        );
    }
}
