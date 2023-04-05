package br.com.alura.transacoes.model.transacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao,Long> {
    @Query("select t from Transacao t where year(t.dataHoraTransacao) = year(:data) and month(t.dataHoraTransacao) = month(:data) and day(t.dataHoraTransacao) = day(:data)")
    List<Transacao> findByData(LocalDate data);
}
