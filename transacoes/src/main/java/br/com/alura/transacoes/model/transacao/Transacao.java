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

    private String bancoOrigem;
    private String agenciaOrigem;
    private String contaOrigem;
    private String bancoDestino;
    private String agenciaDestino;
    private String contaDestino;
    private BigDecimal valorTransacao;
    private LocalDateTime dataHoraTransacao;

    public Transacao(String[] valoresLinha) {
        this.bancoOrigem = valoresLinha[0];
        this.agenciaOrigem = valoresLinha[1];
        this.contaOrigem = valoresLinha[2];
        this.bancoDestino = valoresLinha[3];
        this.agenciaDestino = valoresLinha[4];
        this.contaDestino = valoresLinha[5];
        this.valorTransacao = new BigDecimal(valoresLinha[6]);
        this.dataHoraTransacao = extrairDataTexto(valoresLinha[7]);
    }

    private LocalDateTime extrairDataTexto(String s) {
        String[] dataHora = s.split("T");
        LocalDate data = LocalDate.parse(dataHora[0]);
        LocalTime hora = LocalTime.parse(dataHora[1]);
        return LocalDateTime.of(data, hora);
    }

    @Override
    public String toString() {
        return this.bancoOrigem + this.agenciaOrigem + this.contaOrigem + this.bancoDestino + this.agenciaDestino + this.contaDestino + this.valorTransacao + this.dataHoraTransacao;
    }
}
