package br.com.alura.transacoes.model.transacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class AnaliseTransacaoService {

    @Autowired
    private TransacaoService transacaoService;

    public void gerarRelatorio(Model model, Integer ano, Integer mes) {
        carregarTransacoesSuspeitas(model, ano, mes);
        carregarContasSuspeitas(model, ano, mes);
        carregarAgenciasSuspeitas(model, ano, mes);

    }

    private void carregarAgenciasSuspeitas(Model model, Integer ano, Integer mes) {
        List<DadosAgenciasSuspeitas> agenciasSuspeitas = transacaoService.listarAgenciasSuspeitas(ano, mes);
        model.addAttribute("agenciasSuspeitas", agenciasSuspeitas);
    }

    private void carregarContasSuspeitas(Model model, Integer ano, Integer mes) {
        List<DadosContasSuspeitas> contasSuspeitas = transacaoService.listarContasSuspeitas(ano, mes);// 1.000.000 - Entrada OU Saída
        model.addAttribute("contasSuspeitas",contasSuspeitas);
    }

    private void carregarTransacoesSuspeitas(Model model, Integer ano, Integer mes) {
        List<DadosListarTransacoes> transacoesSuspeitas = transacaoService.listarTransacoesSuspeitas(ano, mes).stream().map(DadosListarTransacoes::new).toList();// Listar todas as trasações suspeitas, ou seja, com valor acima de 100.000
        model.addAttribute("transacoesSuspeitas", transacoesSuspeitas);
    }
}
