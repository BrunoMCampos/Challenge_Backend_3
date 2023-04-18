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

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosContasSuspeitas(t.contaOrigem, sum(t.valorTransacao), 'Saída') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.contaOrigem.conta, t.contaOrigem.agencia, t.contaOrigem.banco having sum(t.valorTransacao) > 1000000")
    List<DadosContasSuspeitas> listarContasSuspeitasNaDataPorOrigem(Integer ano, Integer mes);

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosContasSuspeitas(t.contaDestino, sum(t.valorTransacao), 'Entrada') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.contaDestino.conta, t.contaDestino.agencia, t.contaDestino.banco having sum(t.valorTransacao) > 1000000")
    List<DadosContasSuspeitas> listarContasSuspeitasNaDataPorDestino(Integer ano, Integer mes);

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosAgenciasSuspeitas(t.contaDestino.banco,t.contaDestino.agencia, sum(t.valorTransacao), 'Entrada') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.contaDestino.agencia, t.contaDestino.banco having sum(t.valorTransacao) > 1000000000")
    List<DadosAgenciasSuspeitas> listarAgenciasSuspeitasNaDataPorDestino(Integer ano, Integer mes);

    @Query("SELECT new br.com.alura.transacoes.model.transacao.DadosAgenciasSuspeitas(t.contaOrigem.banco,t.contaOrigem.agencia, sum(t.valorTransacao), 'Saída') FROM Transacao t where year(t.dataHoraTransacao) = :ano and month(t.dataHoraTransacao) = :mes group by t.contaOrigem.agencia, t.contaOrigem.banco having sum(t.valorTransacao) > 1000000000")
    List<DadosAgenciasSuspeitas> listarAgenciasSuspeitasNaDataPorOrigem(Integer ano, Integer mes);

}
