package br.com.alura.transacoes.controller;

import br.com.alura.transacoes.model.importacao.DadosImportacao;
import br.com.alura.transacoes.model.importacao.ImportacaoRepository;
import br.com.alura.transacoes.model.transacao.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private void listarImportacoes(Model model) {
        List<DadosImportacao> importacoes = importacaoRepository.findAll().stream().map(DadosImportacao::new).toList();
        model.addAttribute("importacoes", importacoes);
        model.addAttribute("telaAtiva","formulario");
    }

    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        listarImportacoes(model);
        return "importacoes/formulario";
    }

    @PostMapping("/formulario")
    public String realizarUploadArquivo(@RequestParam("arquivo") MultipartFile arquivo, Model model) throws IOException {
        transacaoService.importar(arquivo, model);
        listarImportacoes(model);
        return "importacoes/formulario";
    }

    @GetMapping("/detalhes/{dataTransacoes}")
    public String exibirFormulario(Model model, @PathVariable LocalDate dataTransacoes) {
        model.addAttribute("transacoes",transacaoService.listarPorData(dataTransacoes));
        model.addAttribute("importacao", new DadosImportacao(importacaoRepository.getReferenceByDataTransacoes(dataTransacoes)));
        model.addAttribute("telaAtiva","formulario");
        return "importacoes/detalhar-importacao";
    }

}
