package br.com.alura.transacoes.model.usuario;

import br.com.alura.transacoes.infra.security.MyUserDetail;
import br.com.alura.transacoes.infra.security.MyUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.naming.Binding;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class UsuarioServiceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@teste.com.br");
        usuario.setNome("teste");
        usuario.setSenha("teste");
        usuario.setAtivo(true);

        MyUserDetail userDetails = (MyUserDetail) myUserDetailService.loadUserByUsername(usuario.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de cadastro efetuado com sucesso para dados corretos ao cadastrar usuario")
    void cadastrarCenario1() throws Exception {
        MvcResult mvcResult = mvc
                .perform(
                        post("/usuarios/cadastrar")
                                .param("email", "usuario@email.com.br")
                                .param("nome", "usuario")
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
        assertThat(model.get("mensagem")).isEqualTo("Cadastro efetuado com sucesso! \n Acesse o email cadastrado para recuperar sua senha de acesso.");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de erro para nome em branco ou vazio")
    void cadastrarCenario2() throws Exception {
        MvcResult mvcResult = mvc
                .perform(
                        post("/usuarios/cadastrar")
                                .param("email", "usuario@email.com.br")
                                .with(csrf().asHeader()))
                .andReturn();
            BindingResult result = (BindingResult) Objects.requireNonNull(mvcResult.getModelAndView()).getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "dadosCadastrarUsuario");
            assertThat(Objects.requireNonNull(result.getFieldError("nome")).getDefaultMessage()).isEqualTo("O campo nome é obrigatório!");

        mvcResult = mvc
                .perform(
                        post("/usuarios/cadastrar")
                                .param("email", "usuario@email.com.br")
                                .param("nome", "")
                                .with(csrf().asHeader()))
                .andReturn();
        result = (BindingResult) Objects.requireNonNull(mvcResult.getModelAndView()).getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "dadosCadastrarUsuario");
        assertThat(Objects.requireNonNull(result.getFieldError("nome")).getDefaultMessage()).isEqualTo("O campo nome é obrigatório!");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de erro para email em branco ou vazio")
    void cadastrarCenario3() throws Exception {
        MvcResult mvcResult = mvc
                .perform(
                        post("/usuarios/cadastrar")
                                .param("nome", "usuario")
                                .with(csrf().asHeader()))
                .andReturn();
        BindingResult result = (BindingResult) Objects.requireNonNull(mvcResult.getModelAndView()).getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "dadosCadastrarUsuario");
        assertThat(Objects.requireNonNull(result.getFieldError("email")).getDefaultMessage()).isEqualTo("O campo e-mail é obrigatório!");

        mvcResult = mvc
                .perform(
                        post("/usuarios/cadastrar")
                                .param("email", "")
                                .param("nome", "usuario")
                                .with(csrf().asHeader()))
                .andReturn();
        result = (BindingResult) Objects.requireNonNull(mvcResult.getModelAndView()).getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "dadosCadastrarUsuario");
        assertThat(Objects.requireNonNull(result.getFieldError("email")).getDefaultMessage()).isEqualTo("O campo e-mail é obrigatório!");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de erro para email já cadastrado")
    void cadastrarCenario4() throws Exception {
        MvcResult mvcResult = mvc
                .perform(
                        post("/usuarios/cadastrar")
                                .param("email", "usuario@email.com.br")
                                .param("nome", "usuario")
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
        assertThat(model.get("mensagem")).isEqualTo("Cadastro efetuado com sucesso! \n Acesse o email cadastrado para recuperar sua senha de acesso.");

        mvcResult = mvc
                .perform(
                        post("/usuarios/cadastrar")
                                .param("email", "usuario@email.com.br")
                                .param("nome", "usuario")
                                .with(csrf().asHeader()))
                .andReturn();

        BindingResult result = (BindingResult) Objects.requireNonNull(mvcResult.getModelAndView()).getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "dadosCadastrarUsuario");
        assertThat(Objects.requireNonNull(result.getFieldError("email")).getDefaultMessage()).isEqualTo("E-mail já cadastrado, por favor entre com outro endereço de e-mail!");
    }

    @Test
    @DisplayName("Deveria retornar apenas 1 usuario que é o ativo")
    void listarTodos() throws Exception {
        Usuario usuario1 = new Usuario("usuario1", "usuario1@email.com.br", "$2a$12$7Xj9LjZ2vubkWCT0n/n0C.CVnGRy9Yi9ipZQbNS2HABoWBVDRXWpS");
        Usuario usuario2 = new Usuario("usuario2", "usuario2@email.com.br", "$2a$12$Kwl.ZomIv.HnBVimq3lB9uNEEyPg2xIysXI1OEcDmT7YuKJzzKv82");
        Usuario usuario3 = new Usuario("usuario3", "usuario3@email.com.br", "$2a$12$tiUzgCkjJiEArA5usw4AoeZiu6/HcnwcAYmkbboGjD9j94c8DY2Zi");

        usuario1.setAtivo(false);
        usuario3.setAtivo(false);

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);
        usuarioRepository.save(usuario3);

        MvcResult mvcResult = mvc.perform(
                        get("/usuarios/listar")
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();

        List<DadosListarUsuarios> listaUsuarios = List.of(new DadosListarUsuarios(usuario2));

        assertThat(model.get("usuarios")).isEqualTo(listaUsuarios);
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de sucesso ao alterar um usuario que existe")
    void alterarCenario1() throws Exception {
        Usuario usuario1 = new Usuario("usuario1", "usuario1@email.com.br", "$2a$12$7Xj9LjZ2vubkWCT0n/n0C.CVnGRy9Yi9ipZQbNS2HABoWBVDRXWpS");

        usuarioRepository.save(usuario1);

        MvcResult mvcResult = mvc.perform(
                        put("/usuarios/alterar/" + usuario1.getEmail())
                                .param("nome","teste2")
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagem")).isEqualTo("Dados alterados com sucesso!");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de erro ao deixar o nome em branco ou nulo quando alterar um usuário")
    void alterarCenario2() throws Exception {
        Usuario usuario1 = new Usuario("usuario1", "usuario1@email.com.br", "$2a$12$7Xj9LjZ2vubkWCT0n/n0C.CVnGRy9Yi9ipZQbNS2HABoWBVDRXWpS");

        usuarioRepository.save(usuario1);

        MvcResult mvcResult = mvc.perform(
                        put("/usuarios/alterar/" + usuario1.getEmail())
                                .param("nome","")
                                .with(csrf().asHeader()))
                .andReturn();

        BindingResult model = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "dadosAlterarUsuario");
        assertThat(model.getFieldError("nome").getDefaultMessage()).isEqualTo("O campo nome é obrigatório!");

        mvcResult = mvc.perform(
                        put("/usuarios/alterar/" + usuario1.getEmail())
                                .with(csrf().asHeader()))
                .andReturn();

        model = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "dadosAlterarUsuario");
        assertThat(model.getFieldError("nome").getDefaultMessage()).isEqualTo("O campo nome é obrigatório!");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de sucesso ao deletar um usuário que existe")
    void deletarCenario1() throws Exception {
        Usuario usuario1 = new Usuario("usuario1", "usuario1@email.com.br", "$2a$12$7Xj9LjZ2vubkWCT0n/n0C.CVnGRy9Yi9ipZQbNS2HABoWBVDRXWpS");

        usuarioRepository.save(usuario1);

        MvcResult mvcResult = mvc.perform(
                        delete("/usuarios/remover/" + usuario1.getEmail())
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagem")).isEqualTo("Usuário deletado com sucesso!");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de erro ao deletar um usuário que não existe")
    void deletarCenario2() throws Exception {
        Usuario usuario1 = new Usuario("usuario1", "usuario1@email.com.br", "$2a$12$7Xj9LjZ2vubkWCT0n/n0C.CVnGRy9Yi9ipZQbNS2HABoWBVDRXWpS");

        usuarioRepository.save(usuario1);

        MvcResult mvcResult = mvc.perform(
                        delete("/usuarios/remover/" + usuario1.getEmail() + "r")
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("erro")).isEqualTo("Usuario não encontrado!");
    }
}