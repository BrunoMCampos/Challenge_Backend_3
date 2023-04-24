package br.com.alura.transacoes.model.transacao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
@Setter
public class Conta {
    private String banco;
    private String agencia;
    private String conta;
}
