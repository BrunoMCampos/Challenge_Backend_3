package br.com.alura.transacoes.controller;

import br.com.alura.transacoes.model.importacao.DadosImportacao;
import br.com.alura.transacoes.model.importacao.ImportacaoRepository;
import br.com.alura.transacoes.model.transacao.AnaliseTransacaoService;
import br.com.alura.transacoes.model.transacao.DataAnaliseTransacoes;
import br.com.alura.transacoes.model.transacao.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String exibirFormularioImportar(Model model) {
        listarImportacoes(model);
        return "importacoes/formulario";
    }

    @PostMapping("/formulario")
    public String realizarUploadArquivo(@RequestParam("arquivo") List<MultipartFile> arquivos, Model model) throws IOException {
        transacaoService.importar(arquivos, model);
        listarImportacoes(model);
        return "importacoes/formulario";
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
        model.addAttribute("anos", transacaoService.listarAnosComImportacoes());
        if(result.hasErrors()){
            return "transacoes/analise-transacoes";
        }
        analiseService.gerarRelatorio(model, data.ano(), data.mes());
        return "transacoes/analise-transacoes";
    }

    @GetMapping("/detalhes/{dataTransacoes}")
    public String exibirFormulario(Model model, @PathVariable LocalDate dataTransacoes) {
        model.addAttribute("transacoes", transacaoService.listarPorData(dataTransacoes));
        model.addAttribute("importacao", new DadosImportacao(importacaoRepository.getReferenceByDataTransacoes(dataTransacoes)));
        model.addAttribute("telaAtiva", "formulario");
        return "importacoes/detalhar-importacao";
    }

}
