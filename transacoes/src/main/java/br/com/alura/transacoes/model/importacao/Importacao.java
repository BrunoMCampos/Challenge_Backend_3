package br.com.alura.transacoes.model.importacao;

import br.com.alura.transacoes.model.usuario.Usuario;
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

    @JoinColumn(name = "fk_email_usuario")
    @ManyToOne
    private Usuario usuarioUpload;

    public Importacao(LocalDate dataTransacoes, LocalDateTime dataUpload, Usuario usuarioLogado) {
        this.dataTransacoes = dataTransacoes;
        this.dataUpload = dataUpload;
        this.usuarioUpload = usuarioLogado;
    }
}
