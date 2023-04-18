package br.com.alura.transacoes.model.transacao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "Transacao")
@Table(name = "transacoes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AttributeOverrides({
            @AttributeOverride(name = "banco", column = @Column(name = "banco_origem")),
            @AttributeOverride(name = "agencia", column = @Column(name = "agencia_origem")),
            @AttributeOverride(name = "conta", column = @Column(name = "conta_origem"))
    })
    @Embedded
    private Conta contaOrigem = new Conta();

    @AttributeOverrides({
            @AttributeOverride(name = "banco", column = @Column(name = "banco_destino")),
            @AttributeOverride(name = "agencia", column = @Column(name = "agencia_destino")),
            @AttributeOverride(name = "conta", column = @Column(name = "conta_destino"))
    })
    @Embedded
    private Conta contaDestino = new Conta();

    private BigDecimal valorTransacao;
    private LocalDateTime dataHoraTransacao;

    public Transacao(String[] valoresLinha) {
        this.contaOrigem.setBanco(valoresLinha[0]);
        this.contaOrigem.setAgencia(valoresLinha[1]);
        this.contaOrigem.setConta(valoresLinha[2]);
        this.contaDestino.setBanco(valoresLinha[3]);
        this.contaDestino.setAgencia(valoresLinha[4]);
        this.contaDestino.setConta(valoresLinha[5]);
        this.valorTransacao = new BigDecimal(valoresLinha[6]);
        this.dataHoraTransacao = extrairDataTexto(valoresLinha[7]);
    }

    public Transacao(TransacaoXML transacaoXML) {
        this.contaOrigem = transacaoXML.origem();
        this.contaDestino = transacaoXML.destino();
        this.valorTransacao = transacaoXML.valor();
        this.dataHoraTransacao = transacaoXML.data();
    }

    private LocalDateTime extrairDataTexto(String s) {
        String[] dataHora = s.split("T");
        LocalDate data = LocalDate.parse(dataHora[0]);
        LocalTime hora = LocalTime.parse(dataHora[1]);
        return LocalDateTime.of(data, hora);
    }

    @Override
    public String toString() {
        return
                this.contaOrigem.getBanco() +
                        this.contaOrigem.getAgencia() +
                        this.contaOrigem.getConta() +
                        this.contaDestino.getBanco() +
                        this.contaDestino.getAgencia() +
                        this.contaDestino.getConta() +
                        this.valorTransacao +
                        this.dataHoraTransacao;
    }
}
