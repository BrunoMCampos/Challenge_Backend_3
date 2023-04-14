package br.com.alura.transacoes.model.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender envioEmail;

    public String cadastrar(DadosCadastrarUsuario dados, BindingResult result, Model model) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(dados.email()); // Verificar se e-mail já não está em uso
        if (optionalUsuario.isPresent()) {
            result.rejectValue("email", "400", "E-mail já cadastrado, por favor entre com outro endereço de e-mail!");
            return "usuarios/cadastrar-usuario";
        }
        String senha = gerarSenha(); // Gerar senha aleatória
        usuarioRepository.save(new Usuario(dados, encoder.encode(senha))); // Realizar o cadastrado criptografando a senha
        enviarSenhaPorEmail(senha, dados.email()); // Realiza o envio do e-mail contendo a senha aleatória para o email cadastrado
        model.addAttribute("mensagem", "Cadastro efetuado com sucesso! \n Acesse o email cadastrado para recuperar sua senha de acesso.");
        model.addAttribute("usuarios",this.listarTodos());
        return "usuarios/listar-usuarios";
    }

    private String gerarSenha() { // Gera senha aleatória de 6 números
        Random random = new Random();
        String senha = "";
        for (int i = 0; i < 6; i++) {
            senha = senha.concat(String.valueOf(random.nextInt(10)));
        }
        return senha;
    }

    private void enviarSenhaPorEmail(String senha, String emailDestinatario) { // Realiza o envio de e-mail para o usuário cadastrado
        SimpleMailMessage mensagem = new SimpleMailMessage();
        String titulo = "Senha de Acesso - Integração de CSVs";
        String conteudo = "Seja bem vindo a plataforma de conversão de CSV de transações!\n" + "Abaixo estão suas informações de login." +
                "email: " + emailDestinatario + "\n" + "senha: " + senha;
        mensagem.setTo(emailDestinatario);
        mensagem.setSubject(titulo);
        mensagem.setText(conteudo);
        envioEmail.send(mensagem);
    }

    public List<Usuario> listarTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        return usuarioRepository.listarUsuariosMenosAdminEProprio(usuario.getEmail());
    }

    public String alterar(Usuario usuario, DadosAlterarUsuario dados, Model model, BindingResult result) {
        if (!dados.email().equals(usuario.getEmail())) {
            Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(dados.email());
            if (optionalUsuario.isPresent()) {
                result.rejectValue("email", "400", "E-mail já cadastrado, por favor entre com outro endereço de e-mail!");
                return "usuarios/alterar-usuario";
            }
            usuario.setEmail(dados.email());
        }
        if (!dados.nome().equals(usuario.getNome())) {
            usuario.setNome(dados.nome());
        }
        model.addAttribute("mensagem", "Dados alterados com sucesso!");
        List<DadosListarUsuarios> usuarios = this.listarTodos().stream().map(DadosListarUsuarios::new).toList();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar-usuarios";
    }

    public String deletar(Long id, Model model) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) {
            model.addAttribute("erro", "Usuario não encontrado!");
            List<Usuario> usuarios = this.listarTodos();
            model.addAttribute("usuarios", usuarios);
            return "usuarios/listar-usuarios";
        }
        Usuario usuario = optionalUsuario.get();
        usuario.setAtivo(false);
        List<Usuario> usuarios = this.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("mensagem", "Usuário deletado com sucesso!");
        return "usuarios/listar-usuarios";
    }
}
