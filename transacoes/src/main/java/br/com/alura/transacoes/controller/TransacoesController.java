package br.com.alura.transacoes.controller;

import br.com.alura.transacoes.model.importacao.DadosImportacao;
import br.com.alura.transacoes.model.importacao.ImportacaoRepository;
import br.com.alura.transacoes.model.transacao.AnaliseTransacaoService;
import br.com.alura.transacoes.model.transacao.ArquivosImportados;
import br.com.alura.transacoes.model.transacao.DataAnaliseTransacoes;
import br.com.alura.transacoes.model.transacao.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transacoes")
public class TransacoesController {
    @Autowired
    private ImportacaoRepository importacaoRepository;

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private AnaliseTransacaoService analiseService;

    private void listarImportacoes(Model model) {
        List<DadosImportacao> importacoes = importacaoRepository.findAll().stream().map(DadosImportacao::new).toList();
        model.addAttribute("importacoes", importacoes);
        model.addAttribute("telaAtiva", "formulario");
    }

    @GetMapping("/formulario")
    public String exibirFormularioImportar(ArquivosImportados arquivosImportados, Model model) {
        listarImportacoes(model);
        return "importacoes/formulario";
    }

    @PostMapping("/formulario")
    public String realizarUploadArquivo(ArquivosImportados arquivosImportados, BindingResult result, Model model) throws IOException {
        transacaoService.importar(arquivosImportados, result, model);
        return this.exibirFormularioImportar(arquivosImportados, model);
    }

    @GetMapping("/analise")
    public String exibirFormularioAnalise(Model model, DataAnaliseTransacoes data) {
        model.addAttribute("telaAtiva", "analise");
        model.addAttribute("anos", transacaoService.listarAnosComImportacoes());
        return "transacoes/analise-transacoes";
    }

    @PostMapping("/analise")
    public String gerarAnalise(Model model, @Valid DataAnaliseTransacoes data, BindingResult result) {
        model.addAttribute("telaAtiva", "analise");
        if (result.hasErrors()) {
            return this.exibirFormularioAnalise(model, data);
        }
        analiseService.gerarRelatorio(model, data.ano(), data.mes());
        return this.exibirFormularioAnalise(model, data);
    }

    @GetMapping("/detalhes/{dataTransacoes}")
    public String exibirFormulario(Model model, @PathVariable LocalDate dataTransacoes) {
        model.addAttribute("transacoes", transacaoService.listarPorData(dataTransacoes));
        model.addAttribute("importacao", new DadosImportacao(importacaoRepository.getReferenceByDataTransacoes(dataTransacoes)));
        model.addAttribute("telaAtiva", "formulario");
        return "importacoes/detalhar-importacao";
    }

}
