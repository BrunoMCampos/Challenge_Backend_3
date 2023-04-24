package br.com.alura.transacoes.model.transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoXML(
        Conta origem,
        Conta destino,
        BigDecimal valor,
        LocalDateTime data
) {
}
