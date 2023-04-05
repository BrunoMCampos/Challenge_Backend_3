package br.com.alura.transacoes.model.importacao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "Importacao")
@Table(name = "importacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Importacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataTransacoes;
    private LocalDateTime dataUpload;

    public Importacao(LocalDate dataTransacoes, LocalDateTime dataUpload) {
        this.dataTransacoes = dataTransacoes;
        this.dataUpload = dataUpload;
    }
}
