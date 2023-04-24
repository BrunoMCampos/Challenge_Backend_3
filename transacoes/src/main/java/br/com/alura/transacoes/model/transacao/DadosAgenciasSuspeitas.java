package br.com.alura.transacoes.model.transacao;

import java.math.BigDecimal;

public record DadosAgenciasSuspeitas(String banco, String agencia, BigDecimal valorMovimentado, String tipoMovimentacao) {
}
