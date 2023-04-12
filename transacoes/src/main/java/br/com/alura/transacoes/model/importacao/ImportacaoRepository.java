package br.com.alura.transacoes.model.importacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ImportacaoRepository extends JpaRepository<Importacao,Long> {

    Importacao getReferenceByDataTransacoes(LocalDate data);

}
