package br.com.alura.transacoes.controller;

import br.com.alura.transacoes.model.importacao.DadosImportacao;
import br.com.alura.transacoes.model.importacao.ImportacaoRepository;
import br.com.alura.transacoes.model.transacao.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class TransacoesController {
    @Autowired
    private ImportacaoRepository importacaoRepository;

    @Autowired
    private TransacaoService transacaoService;

    private void listarImportacoes(Model model) {
        List<DadosImportacao> importacoes = importacaoRepository.findAll().stream().map(DadosImportacao::new).toList();
        model.addAttribute("importacoes", importacoes);
    }

    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        listarImportacoes(model);
        return "formulario";
    }

    @PostMapping("/formulario")
    public String uploadArquivo(@RequestParam("arquivo") MultipartFile arquivo, Model model) throws IOException {
        transacaoService.importar(arquivo, model);
        listarImportacoes(model);
        return "formulario";
    }

}
