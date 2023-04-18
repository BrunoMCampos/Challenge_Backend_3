package br.com.alura.transacoes.model.transacao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@WithMockUser
class TransacaoServiceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private BindingResult result;

    @Autowired
    private Model model;

    @Test
    @DisplayName("Deveria retornar no atributo 'mensagemCadastro' do modal a quantidade de dez transações cadastradas com sucesso")
    void importarCenario1() throws IOException {
        String arquivoCSV = """
                BANCO DO BRASIL,0001,00001-1,BANCO BRADESCO,0001,00001-1,8000,2022-01-01T07:30:00
                BANCO SANTANDER,0001,00001-1,BANCO BRADESCO,0001,00001-1,210,2022-01-01T08:12:00
                BANCO SANTANDER,0001,00002-1,BANCO BRADESCO,0001,00001-1,79800.22,2022-01-01T08:44:00
                BANCO BRADESCO,0001,00001-1,BANCO SANTANDER,0001,00002-1,,2022-01-01T12:32:00
                BANCO BANRISUL,0001,00001-1,BANCO BRADESCO,0001,00001-1,100,2022-01-01T16:30:00
                BANCO ITAU,0001,00001-1,BANCO BRADESCO,0001,00001-1,19000.50,2022-01-03T16:55:00
                BANCO ITAU,0001,00002-1,BANCO BRADESCO,0001,00001-1,1000,2022-01-01T19:30:00
                NUBANK,0001,00001-1,BANCO BRADESCO,0001,00001-1,2000,2022-01-01T19:34:00
                BANCO INTER,0001,00001-1,BANCO BRADESCO,0001,00001-1,300,2022-01-09T20:30:00
                CAIXA ECONOMICA FEDERAL,0001,00001-1,BANCO BRADESCO,0001,,900,2022-01-01T22:30:00
                """;
        MultipartFile arquivo = new MockMultipartFile("Arquivo CSV", arquivoCSV.getBytes());
        List<MultipartFile> arquivos = List.of(arquivo);
        ArquivosImportados arquivosImportados = new ArquivosImportados(arquivos);
        mvc.;
        transacaoService.importar(arquivosImportados,result, model);
        assertThat(model.getAttribute("mensagemCadastro")).isEqualTo("10 transações cadastradas com sucesso!");
    }

    @DisplayName("Não deve cadastrar arquivos vazios")
    void importarCenario2() {

    }

    @DisplayName("De dez transações apenas nove devem ser registradas, por conter uma com data diferente da primeira transação do arquivo")
    void importarCenario3() {

    }

    @DisplayName("Não deve registrar transações, de uma data que já foi inserida no banco de dados")
    void importarCenario4() {

    }

    @DisplayName("Cadastrando arquivo XML vazio")
    void importarCenario7() {

    }

    @DisplayName("Cadastrando arquivo XML vazio")
    void importarCenario8() {

    }

    @Test
    void listarPorData() {
    }

    @Test
    void listarAnosComImportacoes() {
    }

    @Test
    void listarTransacoesSuspeitas() {
    }

    @Test
    void listarContasSuspeitas() {
    }

    @Test
    void listarAgenciasSuspeitas() {
    }
}