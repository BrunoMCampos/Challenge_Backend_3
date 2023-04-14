package br.com.alura.transacoes.controller;

import br.com.alura.transacoes.model.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("cadastrar")
    public String formularioCadastroUsuarios(DadosCadastrarUsuario dados, Model model) {
        model.addAttribute("telaAtiva", "usuarios");
        return "usuarios/cadastrar-usuario";
    }

    @GetMapping("alterar/{id}")
    public String formularioAlterarUsuario(DadosAlterarUsuario dados, Model model, @PathVariable Long id) {
        model.addAttribute("telaAtiva", "usuarios");
        Usuario usuario = usuarioRepository.getReferenceById(id);
        dados = new DadosAlterarUsuario(usuario.getNome(), usuario.getEmail());
        model.addAttribute("dadosAlterarUsuario", dados);
        model.addAttribute("id", id);
        return "usuarios/alterar-usuario";
    }

    @PutMapping("alterar/{id}")
    @Transactional
    public String alterarUsuario(@Valid DadosAlterarUsuario dados, BindingResult result, Model model, @PathVariable Long id) {
        if (result.hasErrors()) {
            return "usuarios/alterar-usuario";
        }
        model.addAttribute("telaAtiva", "usuarios");
        Usuario usuario = usuarioRepository.getReferenceById(id);
        return usuarioService.alterar(usuario, dados, model, result);
    }

    @PostMapping("cadastrar")
    @Transactional
    public String cadastrarUsuario(@Valid DadosCadastrarUsuario dados, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "usuarios/cadastrar-usuario";
        }
        return usuarioService.cadastrar(dados, result, model);
    }

    @GetMapping("listar")
    public String listarUsuariosCadastrados(Model model) {
        model.addAttribute("telaAtiva", "usuarios");
        List<DadosListarUsuarios> usuarios = usuarioService.listarTodos().stream().map(DadosListarUsuarios::new).toList();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar-usuarios";
    }

    @GetMapping("login")
    public String formularioLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login/login";
        }
        return "redirect:/formulario";
    }

    @DeleteMapping("/remover/{id}")
    @Transactional
    public String deletarUsuario(Model model, @PathVariable Long id) {
        return usuarioService.deletar(id, model);
    }

}
