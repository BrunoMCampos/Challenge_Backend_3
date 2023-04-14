package br.com.alura.transacoes.model.transacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao,Long> {
    @Query("select t from Transacao t where year(t.dataHoraTransacao) = year(:data) and month(t.dataHoraTransacao) = month(:data) and day(t.dataHoraTransacao) = day(:data)")
    List<Transacao> findByData(LocalDate data);

    @Query("select distinct year(t.dataHoraTransacao) from Transacao t")
    List<Integer> listarAnos();

    @Query("select t from Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes and t.valorTransacao >= 100000")
    List<Transacao> listarTransacoesSuspeitasNaData(Integer ano, Integer mes);

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosContasSuspeitas(t.bancoOrigem,t.agenciaOrigem, t.contaOrigem, sum(t.valorTransacao), 'Saída') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.contaOrigem, t.agenciaOrigem, t.bancoOrigem having sum(t.valorTransacao) > 1000000")
    List<DadosContasSuspeitas> listarContasSuspeitasNaDataPorOrigem(Integer ano, Integer mes);

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosContasSuspeitas(t.bancoDestino,t.agenciaDestino, t.contaDestino, sum(t.valorTransacao), 'Entrada') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.contaDestino, t.agenciaDestino, t.bancoDestino having sum(valorTransacao) > 1000000")
    List<DadosContasSuspeitas> listarContasSuspeitasNaDataPorDestino(Integer ano, Integer mes);

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosAgenciasSuspeitas(t.bancoDestino,t.agenciaDestino, sum(t.valorTransacao), 'Entrada') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.agenciaDestino, t.bancoDestino having sum(valorTransacao) > 1000000000")
    List<DadosAgenciasSuspeitas> listarAgenciasSuspeitasNaDataPorDestino(Integer ano, Integer mes);

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosAgenciasSuspeitas(t.bancoOrigem,t.agenciaOrigem, sum(t.valorTransacao), 'Saída') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.agenciaOrigem, t.bancoOrigem having sum(valorTransacao) > 1000000000")
    List<DadosAgenciasSuspeitas> listarAgenciasSuspeitasNaDataPorOrigem(Integer ano, Integer mes);

}
