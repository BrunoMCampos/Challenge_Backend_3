package br.com.alura.transacoes.model.importacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DadosImportacao(LocalDate dataTransacoes, LocalDateTime dataUpload) {
    public DadosImportacao(Importacao importacao){
        this(
                importacao.getDataTransacoes(),
                importacao.getDataUpload()
        );
    }
}
