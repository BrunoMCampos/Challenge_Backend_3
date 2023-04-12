package br.com.alura.transacoes.model.transacao;

import br.com.alura.transacoes.model.importacao.Importacao;
import br.com.alura.transacoes.model.importacao.ImportacaoRepository;
import br.com.alura.transacoes.model.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ImportacaoRepository importacaoRepository;

    private LocalDate dataPrimeiraTransacao;

    private static List<Transacao> extrairTransacoes(MultipartFile arquivo) throws IOException {
        Scanner scanner = new Scanner(arquivo.getInputStream());
        List<Transacao> transacoes = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            String[] valoresLinha = linha.split(",");
            Transacao transacao = new Transacao(valoresLinha);
            transacoes.add(transacao);
        }
        return transacoes;
    }

    public void importar(MultipartFile arquivo, Model model) throws IOException {
        List<Transacao> transacoes = extrairTransacoes(arquivo);
        List<Transacao> transacoesValidas = validarTransacoes(transacoes, model);

        if (transacoesValidas != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = (Usuario) auth.getPrincipal();
            importacaoRepository.save(new Importacao(this.dataPrimeiraTransacao, LocalDateTime.now(), usuario));

            transacoesValidas.forEach(transacao -> {
                transacaoRepository.save(transacao);
            });

            String mensagem;
            if (transacoesValidas.size() > 1) {
                mensagem = transacoesValidas.size() + " transações cadastradas com sucesso!";
            } else {
                mensagem = transacoesValidas.size() + " transação cadastrada com sucesso!";
            }
            model.addAttribute("mensagemCadastro", mensagem);
        }
    }

    private List<Transacao> validarTransacoes(List<Transacao> transacoes, Model model) {
        if (transacoes.size() == 0) { //Verificar transações em branco
            model.addAttribute("mensagemErro", "Arquivo CSV vazio!");
            return null;
        }

        this.dataPrimeiraTransacao = transacoes.get(0).getDataHoraTransacao().toLocalDate(); // Ler primeira transação e determinar a data aceita para este arquivo
        List<Transacao> dataCadastrada = transacaoRepository.findByData(dataPrimeiraTransacao); // Com base na data verificar se as transações deste dia já não foram registradas
        if (dataCadastrada != null && dataCadastrada.size() > 0) {
            model.addAttribute("mensagemErro", "As transações desta data já foram importadas!");
            return null;
        }

        List<Transacao> transacoesValidas = new ArrayList<>();
        transacoes.forEach(transacao -> {
            if (datasSaoIguais(transacao.getDataHoraTransacao()) && !temDadosNulos(transacao)) {
                transacoesValidas.add(transacao);
            }
        });
        return transacoesValidas;
    }

    private boolean temDadosNulos(Transacao transacao) {
        if (transacao.getDataHoraTransacao() == null) {
            return true;
        }
        if (transacao.getValorTransacao() == null) {
            return true;
        }
        if (transacao.getContaOrigem() == null || transacao.getContaOrigem() == "") {
            return true;
        }
        if (transacao.getBancoOrigem() == null || transacao.getBancoOrigem() == "") {
            return true;
        }
        if (transacao.getAgenciaOrigem() == null || transacao.getAgenciaOrigem() == "") {
            return true;
        }
        if (transacao.getContaDestino() == null || transacao.getContaDestino() == "") {
            return true;
        }
        if (transacao.getAgenciaDestino() == null || transacao.getAgenciaDestino() == "") {
            return true;
        }
        if (transacao.getBancoDestino() == null || transacao.getBancoDestino() == "") {
            return true;
        }
        return false;
    }

    private boolean datasSaoIguais(LocalDateTime dataTransacao) {
        if (dataTransacao.getYear() == this.dataPrimeiraTransacao.getYear() &&
                dataTransacao.getMonthValue() == this.dataPrimeiraTransacao.getMonthValue() &&
                dataTransacao.getDayOfMonth() == this.dataPrimeiraTransacao.getDayOfMonth()) {
            return true;
        }
        return false;
    }

    public List<DadosListarTransacoes> listarPorData(LocalDate dataTransacoes) {
        return transacaoRepository.findByData(dataTransacoes).stream().map(DadosListarTransacoes::new).toList();
    }
}
