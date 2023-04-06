package br.com.alura.transacoes.model.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

    public String cadastrar(DadosCadastrarUsuario dados, Model model) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(dados.email()); // Verificar se e-mail já não está em uso
        if (optionalUsuario.isPresent()) {
            model.addAttribute("erro", "E-mail já cadastrado, favor entrar com outro endereço de e-mail!");
            return "cadastrar-usuario";
        }
        String senha = gerarSenha(); // Gerar senha aleatória
        usuarioRepository.save(new Usuario(dados,encoder.encode(senha))); // Realizar o cadastrado criptografando a senha
        enviarSenhaPorEmail(senha, dados.email()); // Realiza o envio do e-mail contendo a senha aleatória para o email cadastrado
        model.addAttribute("mensagem","Cadastro efetuado com sucesso! \n Acesse o email cadastrado para recuperar sua senha de acesso.");
        return "login";
    }

    private String gerarSenha() { // Gera senha aleatória de 6 números
        Random random = new Random();
        String senha = "";
        for (int i = 0; i < 6; i++) {
            senha = senha.concat(String.valueOf(random.nextInt(10)));
        }
        return senha;
    }

    private void enviarSenhaPorEmail(String senha, String emailDestinatario){ // Realiza o envio de e-mail para o usuário cadastrado
        SimpleMailMessage mensagem = new SimpleMailMessage();
        String titulo = "Senha de Acesso - Integração de CSVs";
        String conteudo = "Seja bem vindo a plataforma de conversão de CSV de transações!\n" + "Abaixo estão suas informações de login." +
                "email: " + emailDestinatario + "\n" + "senha: " + senha;
        mensagem.setTo(emailDestinatario);
        mensagem.setSubject(titulo);
        mensagem.setText(conteudo);
        envioEmail.send(mensagem);
    }

}
