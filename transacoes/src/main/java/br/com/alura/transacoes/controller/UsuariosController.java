package br.com.alura.transacoes.controller;

import br.com.alura.transacoes.model.usuario.DadosCadastrarUsuario;
import br.com.alura.transacoes.model.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("cadastrar")
    public String formularioCadastroUsuarios(DadosCadastrarUsuario dados){
        return "cadastrar-usuario";
    }

    @PostMapping("cadastrar")
    public String cadastrarUsuario(){
        System.out.println("oi");
        return "cadastrar-usuario";
//        if(result.hasErrors()){
//            System.out.println("Aqui");
//            return "cadastrar-usuario";
//        }
//        return usuarioService.cadastrar(dados, model);
    }

    @GetMapping("listar")
    public String listarUsuariosCadastrados(){
        return "listar-usuarios";
    }

    @GetMapping("login")
    public String formularioLogin(){
        return "login";
    }

}
