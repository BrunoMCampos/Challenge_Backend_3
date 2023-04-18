package br.com.alura.transacoes.model.transacao;

import br.com.alura.transacoes.model.importacao.Importacao;
import br.com.alura.transacoes.model.importacao.ImportacaoRepository;
import br.com.alura.transacoes.model.usuario.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static List<Transacao> extrairTransacoes(MultipartFile arquivo, BindingResult result) throws IOException {
        String nomeArquivo = arquivo.getOriginalFilename();
        if (nomeArquivo != null && nomeArquivo.length() >= 3) {
            String extensao = nomeArquivo.substring(nomeArquivo.length() - 3);
            if (extensao.equalsIgnoreCase("xml")) {
                return extrairDeXML(arquivo);
            } else if (extensao.equalsIgnoreCase("txt")) {
                return extrairDeCSV(arquivo);
            }
            result.rejectValue("arquivos", "415", "Formato de arquivo não suportado! (" + nomeArquivo + ")");
        }
        return new ArrayList<>();
    }

    private static List<Transacao> extrairDeXML(MultipartFile arquivo) throws IOException {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(arquivo.getInputStream(), new TypeReference<List<TransacaoXML>>() {
        }).stream().map(Transacao::new).toList();
    }

    private static List<Transacao> extrairDeCSV(MultipartFile arquivo) throws IOException {
        List<Transacao> transacoes = new ArrayList<>();

        Scanner scanner = new Scanner(arquivo.getInputStream());

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            String[] valoresLinha = linha.split(",");
            Transacao transacao = new Transacao(valoresLinha);
            transacoes.add(transacao);
        }

        return transacoes;
    }

    public void importar(ArquivosImportados arquivos, BindingResult result, Model model) throws IOException {
        int transacoesRealizadas = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        String mensagem;
        List<Importacao> importacoesParaCadastrar = new ArrayList<>();
        List<Transacao> transacoesParaCadastrar = new ArrayList<>();
        for (MultipartFile arquivo : arquivos.arquivos()) {
            List<Transacao> transacoes = extrairTransacoes(arquivo, result);
            List<Transacao> transacoesValidas = validarTransacoes(transacoes, result);
            if (transacoesValidas != null && transacoesValidas.size() > 0) {
                transacoesParaCadastrar.addAll(transacoesValidas);
                importacoesParaCadastrar.add(new Importacao(this.dataPrimeiraTransacao, LocalDateTime.now(), usuario));
            }
        }
        if (transacoesParaCadastrar.size() > 0) {
            importacaoRepository.saveAll(importacoesParaCadastrar);
            transacaoRepository.saveAll(transacoesParaCadastrar);
            transacoesRealizadas += transacoesParaCadastrar.size();
        }
        if (transacoesRealizadas > 1) {
            mensagem = transacoesRealizadas + " transações cadastradas com sucesso!";
        } else if (transacoesRealizadas == 1) {
            mensagem = transacoesRealizadas + " transação cadastrada com sucesso!";
        } else {
            mensagem = null;
        }
        model.addAttribute("mensagemCadastro", mensagem);
    }

    private List<Transacao> validarTransacoes(List<Transacao> transacoes, BindingResult result) {
        if (transacoes.size() == 0) { //Verificar transações em branco
            result.rejectValue("arquivos", "404", "Arquivo CSV vazio!");
            return null;
        }

        this.dataPrimeiraTransacao = transacoes.get(0).getDataHoraTransacao().toLocalDate(); // Ler primeira transação e determinar a data aceita para este arquivo
        List<Transacao> dataCadastrada = transacaoRepository.findByData(dataPrimeiraTransacao); // Com base na data verificar se as transações deste dia já não foram registradas
        if (dataCadastrada != null && dataCadastrada.size() > 0) {
            result.rejectValue(
                    "arquivos",
                    "400",
                    "As transações do dia: " + dataPrimeiraTransacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " já foram importadas!");
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
        if (transacao.getContaOrigem().getConta() == null || transacao.getContaOrigem().getConta().equals("")) {
            return true;
        }
        if (transacao.getContaOrigem().getBanco() == null || transacao.getContaOrigem().getBanco().equals("")) {
            return true;
        }
        if (transacao.getContaOrigem().getAgencia() == null || transacao.getContaOrigem().getAgencia().equals("")) {
            return true;
        }
        if (transacao.getContaDestino().getConta() == null || transacao.getContaDestino().getConta().equals("")) {
            return true;
        }
        if (transacao.getContaDestino().getAgencia() == null || transacao.getContaDestino().getAgencia().equals("")) {
            return true;
        }
        if (transacao.getContaDestino().getBanco() == null || transacao.getContaDestino().getBanco().equals("")) {
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

    public List<Integer> listarAnosComImportacoes() {
        return transacaoRepository.listarAnos();
    }

    public List<Transacao> listarTransacoesSuspeitas(Integer ano, Integer mes) {
        return transacaoRepository.listarTransacoesSuspeitasNaData(ano, mes);
    }

    public List<DadosContasSuspeitas> listarContasSuspeitas(Integer ano, Integer mes) {
        List<DadosContasSuspeitas> contasDestino = transacaoRepository.listarContasSuspeitasNaDataPorDestino(ano, mes);
        List<DadosContasSuspeitas> contasOrigem = transacaoRepository.listarContasSuspeitasNaDataPorOrigem(ano, mes);

        List<DadosContasSuspeitas> contasSuspeitas = new ArrayList<>(contasDestino.stream().toList());
        contasSuspeitas.addAll(contasOrigem.stream().toList());

        return contasSuspeitas;
    }

    public List<DadosAgenciasSuspeitas> listarAgenciasSuspeitas(Integer ano, Integer mes) {
        List<DadosAgenciasSuspeitas> agenciasDestino = transacaoRepository.listarAgenciasSuspeitasNaDataPorDestino(ano, mes);
        List<DadosAgenciasSuspeitas> agenciasOrigem = transacaoRepository.listarAgenciasSuspeitasNaDataPorOrigem(ano, mes);

        List<DadosAgenciasSuspeitas> agenciasSuspeitas = new ArrayList<>(agenciasOrigem.stream().toList());
        agenciasSuspeitas.addAll(agenciasDestino.stream().toList());

        return agenciasSuspeitas;
    }
}
