package br.com.alura.transacoes.model.transacao;

import br.com.alura.transacoes.infra.security.MyUserDetail;
import br.com.alura.transacoes.infra.security.MyUserDetailService;
import br.com.alura.transacoes.model.usuario.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class TransacaoServiceTest {

    private final Transacao transacaoSemBancoOrigem = new Transacao(new String[]{"", "0001", "0013-1", "NUBANK", "0001", "3771-1", "26433.14", "2023-01-04T08:29:45"});
    private final Transacao transacaoSemAgenciaOrigem = new Transacao(new String[]{"BANCO DO BRASIL", "", "2739-1", "BANCO BANRISUL", "0001", "0631-1", "57168.76", "2023-01-04T18:06:43"});
    private final Transacao transacaoSemContaOrigem = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "", "BANCO DO BRASIL", "0001", "2174-1", "40843.85", "2023-01-04T19:42:01"});
    private final Transacao transacaoSemBancoDestino = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "2404-1", "", "0001", "2660-1", "62349.42", "2023-01-04T03:40:52"});
    private final Transacao transacaoSemAgenciaDestino = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "1037-1", "CAIXA ECONOMICA FEDERAL", "", "2525-1", "4030.31", "2023-01-04T20:36:56"});
    private final Transacao transacaoSemContaDestino = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "4679-1", "BANCO ITAU", "0001", "", "41764.59", "2023-02-04T17:37:26"});
    private final Transacao transacaoSemValorTransacao = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "4948-1", "NUBANK", "0001", "4592-1", "", "2023-01-04T22:19:42"});

    private final Transacao transacaoSemData = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "4118-1", "NUBANK", "0001", "1489-1", "35995.34", ""});
    private final Transacao transacaoValida1 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "2012-1", "BANCO DO BRASIL", "0001", "4840-1", "68513.19", "2023-01-04T14:42:45"});
    private final Transacao transacaoValida2 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "3958-1", "BANCO INTER", "0001", "4752-1", "57021.41", "2023-01-04T00:12:33"});
    private final Transacao transacaoValida3 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "4709-1", "BANCO ITAU", "0001", "3309-1", "12720.63", "2023-01-04T12:38:14"});
    private final Transacao transacaoValida4 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "3915-1", "BANCO BANRISUL", "0001", "3688-1", "45837.44", "2023-01-04T07:10:51"});
    private final Transacao transacaoValida5 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "2633-1", "NUBANK", "0001", "4084-1", "29592.56", "2023-01-04T17:17:19"});
    private final Transacao transacaoValida6 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "0439-1", "BANCO DO BRASIL", "0001", "0210-1", "74612.93", "2023-01-04T13:48:18"});
    private final Transacao transacaoValida7 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "1533-1", "BANCO DO BRASIL", "0001", "1917-1", "75565.88", "2023-01-04T12:52:55"});
    private final Transacao transacaoValida8 = new Transacao(new String[]{"BANCO DO BRASIL", "0001", "3592-1", "BANCO SANTANDER", "0001", "4476-1", "57882.53", "2023-01-04T08:49"});

    List<Transacao> transacoesComErro = Arrays.asList(
            transacaoSemBancoOrigem, transacaoSemAgenciaOrigem, transacaoSemContaOrigem, transacaoSemBancoDestino,
            transacaoSemAgenciaDestino, transacaoSemContaDestino, transacaoSemValorTransacao, transacaoSemData
    );

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private TransacaoService transacaoService;

    private static String converterParaCSV(Transacao... transacoes) {
        String csv = "";
        for (Transacao transacao : transacoes) {
            csv = csv.concat(transacao.getContaOrigem().getBanco()) + ",";
            csv = csv.concat(transacao.getContaOrigem().getAgencia()) + ",";
            csv = csv.concat(transacao.getContaOrigem().getConta()) + ",";
            csv = csv.concat(transacao.getContaDestino().getBanco()) + ",";
            csv = csv.concat(transacao.getContaDestino().getAgencia()) + ",";
            csv = csv.concat(transacao.getContaDestino().getConta()) + ",";
            if (transacao.getValorTransacao() != null) {
                csv = csv.concat(transacao.getValorTransacao().toPlainString()) + ",";
            }
            if (transacao.getDataHoraTransacao() != null) {
                csv = csv.concat(transacao.getDataHoraTransacao().toString());
            }
            csv = csv.concat("\n");
        }
        return csv;
    }

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

    private MockMultipartFile criarArquivoCSVtxt(Transacao... transacoes) {
        String csv = converterParaCSV(transacoes);
        return new MockMultipartFile("arquivos", "Arquivo CSV.txt", MediaType.TEXT_PLAIN_VALUE, csv.getBytes());
    }

    private MockMultipartFile criarArquivoCSVcsv(Transacao... transacoes) {
        String csv = converterParaCSV(transacoes);
        return new MockMultipartFile("arquivos", "Arquivo CSV.csv", MediaType.TEXT_PLAIN_VALUE, csv.getBytes());
    }

    private MockMultipartFile criarArquivoXML(Transacao... transacoes) throws JsonProcessingException {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<TransacaoXML> lista = new ArrayList<>();
        for (Transacao t : transacoes) {
            lista.add(new TransacaoXML(t.getContaOrigem(), t.getContaDestino(), t.getValorTransacao(), t.getDataHoraTransacao()));
        }

        String xml = mapper.writeValueAsString(lista);
        return new MockMultipartFile("arquivos", "Arquivo XML.xml", MediaType.TEXT_PLAIN_VALUE, xml.getBytes());
    }


    @Test
    @DisplayName("Deveria retornar no atributo 'mensagemCadastro' do modal a quantidade de " +
            "oito transações cadastradas com sucesso, sendo enviado um arquivo com oito corretas e oito faltando dados no arquivo CSV" +
            "com a extensão txt")
    void importarCenario1() throws Exception {
        MockMultipartFile arquivoCSVtxt = criarArquivoCSVtxt(
                transacaoSemBancoOrigem, transacaoSemAgenciaOrigem, transacaoSemContaOrigem, transacaoSemBancoDestino,
                transacaoSemAgenciaDestino, transacaoSemContaDestino, transacaoSemValorTransacao, transacaoSemData,
                transacaoValida1, transacaoValida2, transacaoValida3, transacaoValida4,
                transacaoValida5, transacaoValida6, transacaoValida7, transacaoValida8
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSVtxt)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("8 transações cadastradas com sucesso!");
    }

    @Test
    @DisplayName("Deveria retornar no atributo 'mensagemCadastro' do modal a quantidade de " +
            "oito transações cadastradas com sucesso, sendo enviado um arquivo com oito corretas e oito faltando dados no arquivo CSV" +
            "com a extensão csv")
    void importarCenario2() throws Exception {
        MockMultipartFile arquivoCSVcsv = criarArquivoCSVcsv(
                transacaoSemBancoOrigem, transacaoSemAgenciaOrigem, transacaoSemContaOrigem, transacaoSemBancoDestino,
                transacaoSemAgenciaDestino, transacaoSemContaDestino, transacaoSemValorTransacao, transacaoSemData,
                transacaoValida1, transacaoValida2, transacaoValida3, transacaoValida4,
                transacaoValida5, transacaoValida6, transacaoValida7, transacaoValida8
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSVcsv)
                                .with(csrf().asHeader()))
                .andReturn();


        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("8 transações cadastradas com sucesso!");
    }

    @Test
    @DisplayName("Deveria retornar no atributo 'mensagemCadastro' do modal a quantidade de " +
            "oito transações cadastradas com sucesso, sendo enviado um arquivo com oito corretas e oito faltando dados no arquivo XML")
    void importarCenario3() throws Exception {
        MockMultipartFile arquivoXML = criarArquivoXML(
                transacaoSemBancoOrigem, transacaoSemAgenciaOrigem, transacaoSemContaOrigem, transacaoSemBancoDestino,
                transacaoSemAgenciaDestino, transacaoSemContaDestino, transacaoSemValorTransacao, transacaoSemData,
                transacaoValida1, transacaoValida2, transacaoValida3, transacaoValida4,
                transacaoValida5, transacaoValida6, transacaoValida7, transacaoValida8
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoXML)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("8 transações cadastradas com sucesso!");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de erro de formato não suportado quando enviado um arquivo com outra extensão senão CSV, TXT e XML ")
    void importarCenario4() throws Exception {
        String csv = converterParaCSV(transacaoValida1);
        MockMultipartFile arquivo = new MockMultipartFile("arquivos", "Arquivo.jpg", MediaType.TEXT_PLAIN_VALUE, csv.getBytes());

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivo)
                                .with(csrf().asHeader()))
                .andReturn();

        BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "arquivosImportados");
        assertThat(bindingResult.getFieldError("arquivos").getDefaultMessage()).isEqualTo("Formato de arquivo não suportado! (" + arquivo.getOriginalFilename() + ")");
    }

    @Test
    @DisplayName("Deveria retornar uma mensagem de erro de arquivo vazio quando enviado um arquivo apenas com transações inválidas (CSV e XML)")
    void importarCenario5() throws Exception {
        for (Transacao transacao : transacoesComErro) {
            MockMultipartFile arquivoCSV = criarArquivoCSVcsv(transacao);

            MvcResult mvcResult = mvc
                    .perform(
                            multipart(HttpMethod.POST, "/transacoes/formulario")
                                    .file(arquivoCSV)
                                    .with(csrf().asHeader()))
                    .andReturn();

            BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "arquivosImportados");
            assertThat(bindingResult.getFieldError("arquivos").getDefaultMessage()).isEqualTo("Arquivo CSV/XML vazio!");

            MockMultipartFile arquivoXML = criarArquivoXML(transacao);

            mvcResult = mvc
                    .perform(
                            multipart(HttpMethod.POST, "/transacoes/formulario")
                                    .file(arquivoXML)
                                    .with(csrf().asHeader()))
                    .andReturn();

            bindingResult = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "arquivosImportados");
            assertThat(bindingResult.getFieldError("arquivos").getDefaultMessage()).isEqualTo("Arquivo CSV/XML vazio!");
        }
    }


    @Test
    @DisplayName("Deveria retornar uma mensagem de erro de arquivo vazio quando enviado um arquivo vazio (CSV e XML)")
    void importarCenario6() throws Exception {
        String texto = "";
        MockMultipartFile arquivo = new MockMultipartFile("arquivos", "Arquivo.csv", MediaType.TEXT_PLAIN_VALUE, texto.getBytes());

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivo)
                                .with(csrf().asHeader()))
                .andReturn();

        BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "arquivosImportados");
        assertThat(bindingResult.getFieldError("arquivos").getDefaultMessage()).isEqualTo("Arquivo CSV/XML vazio!");

        arquivo = new MockMultipartFile("arquivos", "Arquivo.xml", MediaType.TEXT_PLAIN_VALUE, texto.getBytes());

        mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivo)
                                .with(csrf().asHeader()))
                .andReturn();

        bindingResult = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "arquivosImportados");
        assertThat(bindingResult.getFieldError("arquivos").getDefaultMessage()).isEqualTo("Arquivo CSV/XML vazio!");
    }

    @Test
    @DisplayName("Deveria cadastrar apenas sete das oito transacoes enviadas, pois uma está com a data diferente da primeira transacao")
    void importarCenario7() throws Exception {
        transacaoValida8.setDataHoraTransacao(transacaoValida8.getDataHoraTransacao().plusDays(1));

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1, transacaoValida2, transacaoValida3, transacaoValida4,
                transacaoValida5, transacaoValida6, transacaoValida7, transacaoValida8
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("7 transações cadastradas com sucesso!");
    }

    @Test
    @DisplayName("Não deveria registrar transações, de uma data que já foi inserida no banco de dados")
    void importarCenario8() throws Exception {
        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1, transacaoValida2, transacaoValida3, transacaoValida4
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("4 transações cadastradas com sucesso!");

        arquivoCSV = criarArquivoCSVcsv(
                transacaoValida5, transacaoValida6, transacaoValida7, transacaoValida8
        );

        mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModelMap().get(BindingResult.MODEL_KEY_PREFIX + "arquivosImportados");
        assertThat(bindingResult.getFieldError("arquivos").getDefaultMessage()).isEqualTo("As transações do dia: " + transacaoValida5.getDataHoraTransacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " já foram importadas!");
    }

    @Test
    @DisplayName("Deveria retornar apenas as 4 transações com a data de hoje")
    void listarPorData() throws Exception {
        transacaoValida1.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida2.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida3.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida4.setDataHoraTransacao(LocalDateTime.now());

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1, transacaoValida2, transacaoValida3, transacaoValida4
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("4 transações cadastradas com sucesso!");

        transacaoValida5.setDataHoraTransacao(LocalDateTime.now().minusDays(4));
        transacaoValida6.setDataHoraTransacao(LocalDateTime.now().minusDays(4));
        transacaoValida7.setDataHoraTransacao(LocalDateTime.now().minusDays(4));
        transacaoValida8.setDataHoraTransacao(LocalDateTime.now().minusDays(4));

        arquivoCSV = criarArquivoCSVcsv(
                transacaoValida5, transacaoValida6, transacaoValida7, transacaoValida8
        );

        mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("4 transações cadastradas com sucesso!");

        List<DadosListarTransacoes> dadosListarTransacoes = transacaoService.listarPorData(LocalDate.now());

        List<DadosListarTransacoes> listaTransacoes = Arrays.asList(
                new DadosListarTransacoes(transacaoValida1),
                new DadosListarTransacoes(transacaoValida2),
                new DadosListarTransacoes(transacaoValida3),
                new DadosListarTransacoes(transacaoValida4)
        );

        assertThat(dadosListarTransacoes).isEqualTo(listaTransacoes);
    }

    @Test
    @DisplayName("Deveria retornar apenas o ano atual e o ano seguinte")
    void listarAnosComImportacoes() throws Exception {
        transacaoValida1.setDataHoraTransacao(LocalDateTime.now());

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(transacaoValida1);

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("1 transação cadastrada com sucesso!");

        transacaoValida2.setDataHoraTransacao(LocalDateTime.now().plusYears(1));
        transacaoValida3.setDataHoraTransacao(LocalDateTime.now().plusYears(1));
        transacaoValida4.setDataHoraTransacao(LocalDateTime.now().plusYears(1));
        transacaoValida5.setDataHoraTransacao(LocalDateTime.now().plusYears(1));

        arquivoCSV = criarArquivoCSVcsv(transacaoValida2, transacaoValida3, transacaoValida4, transacaoValida5);

        mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("4 transações cadastradas com sucesso!");

        List<Integer> anosComImportacoes = transacaoService.listarAnosComImportacoes();

        List<Integer> listaAnos = Arrays.asList(LocalDate.now().getYear(),LocalDate.now().plusYears(1).getYear());

        assertThat(anosComImportacoes).isEqualTo(listaAnos);
    }

    @Test
    @DisplayName("Deveria retornar apenas 2 transações suspeitas (com valor maior que 100.000) - transacaoValida1 e transacaoValida2")
    void listarTransacoesSuspeitas() throws Exception {
        transacaoValida1.setValorTransacao(new BigDecimal(100100));
        transacaoValida2.setValorTransacao(new BigDecimal(200100));
        transacaoValida3.setValorTransacao(new BigDecimal(5050));
        transacaoValida4.setValorTransacao(new BigDecimal(10050));

        transacaoValida1.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida2.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida3.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida4.setDataHoraTransacao(LocalDateTime.now());

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1,
                transacaoValida2,
                transacaoValida3,
                transacaoValida4
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("4 transações cadastradas com sucesso!");

        List<Transacao> transacoesSuspeitas = transacaoService.listarTransacoesSuspeitas(LocalDate.now().getYear(),LocalDate.now().getMonthValue());

        transacaoValida1.setId(transacoesSuspeitas.get(0).getId());
        transacaoValida2.setId(transacoesSuspeitas.get(1).getId());

        List<Transacao> transacoesSuspeitasEsperadas = Arrays.asList(transacaoValida1,transacaoValida2);

        assertThat(transacoesSuspeitas).isEqualTo(transacoesSuspeitasEsperadas);
    }

    @Test
    @DisplayName("Deveria retornar apenas 2 contas suspeitas (com valor maior que 1.000.000) - 0001-1 e 0001-2 - Conta Origem")
    void listarContasSuspeitas1() throws Exception {
        transacaoValida1.setValorTransacao(new BigDecimal(500100));
        transacaoValida2.setValorTransacao(new BigDecimal(500100));

        transacaoValida3.setValorTransacao(new BigDecimal(700500));
        transacaoValida4.setValorTransacao(new BigDecimal(300100));

        transacaoValida5.setValorTransacao(new BigDecimal(300050));
        transacaoValida6.setValorTransacao(new BigDecimal(300050));

        transacaoValida1.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida2.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida3.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida4.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida5.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida6.setDataHoraTransacao(LocalDateTime.now());

        Conta contaOrigemSuspeita1 = new Conta("BANCO DO BRASIL", "0001", "0001-1");
        Conta contaOrigemSuspeita2 = new Conta("BANCO DO BRASIL", "0001", "0001-2");
        Conta contaOrigemSemSuspeita= new Conta("BANCO DO BRASIL", "0001", "0001-3");

        transacaoValida1.setContaOrigem(contaOrigemSuspeita1);
        transacaoValida2.setContaOrigem(contaOrigemSuspeita1);
        transacaoValida3.setContaOrigem(contaOrigemSuspeita2);
        transacaoValida4.setContaOrigem(contaOrigemSuspeita2);
        transacaoValida5.setContaOrigem(contaOrigemSemSuspeita);
        transacaoValida6.setContaOrigem(contaOrigemSemSuspeita);

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1,
                transacaoValida2,
                transacaoValida3,
                transacaoValida4,
                transacaoValida5,
                transacaoValida6
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("6 transações cadastradas com sucesso!");

        List<DadosContasSuspeitas> transacoesSuspeitas = transacaoService.listarContasSuspeitas(LocalDate.now().getYear(),LocalDate.now().getMonthValue());

        List<DadosContasSuspeitas> transacoesSuspeitasEsperadas = Arrays.asList(
                new DadosContasSuspeitas(contaOrigemSuspeita1,new BigDecimal("1000200.00"),"Saída"),
                new DadosContasSuspeitas(contaOrigemSuspeita2,new BigDecimal("1000600.00"),"Saída")
        );

        assertThat(transacoesSuspeitas).isEqualTo(transacoesSuspeitasEsperadas);
    }

    @Test
    @DisplayName("Deveria retornar apenas 2 contas suspeitas (com valor maior que 1.000.000) - 0001-1 e 0001-2 - Conta Destino")
    void listarContasSuspeitas2() throws Exception {
        transacaoValida1.setValorTransacao(new BigDecimal(500100));
        transacaoValida2.setValorTransacao(new BigDecimal(500100));

        transacaoValida3.setValorTransacao(new BigDecimal(700500));
        transacaoValida4.setValorTransacao(new BigDecimal(300100));

        transacaoValida5.setValorTransacao(new BigDecimal(300050));
        transacaoValida6.setValorTransacao(new BigDecimal(300050));

        transacaoValida1.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida2.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida3.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida4.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida5.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida6.setDataHoraTransacao(LocalDateTime.now());

        Conta contaDestinoSuspeita1 = new Conta("BANCO DO BRASIL", "0001", "0001-1");
        Conta contaDestinoSuspeita2 = new Conta("BANCO DO BRASIL", "0001", "0001-2");
        Conta contaDestinoSemSuspeita= new Conta("BANCO DO BRASIL", "0001", "0001-3");

        transacaoValida1.setContaDestino(contaDestinoSuspeita1);
        transacaoValida2.setContaDestino(contaDestinoSuspeita1);
        transacaoValida3.setContaDestino(contaDestinoSuspeita2);
        transacaoValida4.setContaDestino(contaDestinoSuspeita2);
        transacaoValida5.setContaDestino(contaDestinoSemSuspeita);
        transacaoValida6.setContaDestino(contaDestinoSemSuspeita);

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1,
                transacaoValida2,
                transacaoValida3,
                transacaoValida4,
                transacaoValida5,
                transacaoValida6
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("6 transações cadastradas com sucesso!");

        List<DadosContasSuspeitas> transacoesSuspeitas = transacaoService.listarContasSuspeitas(LocalDate.now().getYear(),LocalDate.now().getMonthValue());

        List<DadosContasSuspeitas> transacoesSuspeitasEsperadas = Arrays.asList(
                new DadosContasSuspeitas(contaDestinoSuspeita1,new BigDecimal("1000200.00"),"Entrada"),
                new DadosContasSuspeitas(contaDestinoSuspeita2,new BigDecimal("1000600.00"),"Entrada")
        );

        assertThat(transacoesSuspeitas).isEqualTo(transacoesSuspeitasEsperadas);
    }

    @Test
    @DisplayName("Deveria retornar apenas 1 agência suspeita (com valor maior que 1.000.000.000) - 0001 - Agencia Origem")
    void listarAgenciasSuspeitas1() throws Exception {
        transacaoValida1.setValorTransacao(new BigDecimal(500000000));
        transacaoValida2.setValorTransacao(new BigDecimal(500000000));
        transacaoValida3.setValorTransacao(new BigDecimal(500000000));
        transacaoValida4.setValorTransacao(new BigDecimal(500000000));
        transacaoValida5.setValorTransacao(new BigDecimal(500000000));

        transacaoValida6.setValorTransacao(new BigDecimal(500000000));

        transacaoValida1.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida2.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida3.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida4.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida5.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida6.setDataHoraTransacao(LocalDateTime.now());

        Conta agenciaOrigemSuspeita = new Conta("BANCO DO BRASIL", "0001", "0001-1");
        Conta agenciaOrigemSemSuspeita= new Conta("BANCO DO BRASIL", "0002", "0001-1");

        transacaoValida1.setContaOrigem(agenciaOrigemSuspeita);
        transacaoValida2.setContaOrigem(agenciaOrigemSuspeita);
        transacaoValida3.setContaOrigem(agenciaOrigemSuspeita);
        transacaoValida4.setContaOrigem(agenciaOrigemSuspeita);
        transacaoValida5.setContaOrigem(agenciaOrigemSuspeita);
        transacaoValida6.setContaOrigem(agenciaOrigemSemSuspeita);

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1,
                transacaoValida2,
                transacaoValida3,
                transacaoValida4,
                transacaoValida5,
                transacaoValida6
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("6 transações cadastradas com sucesso!");

        List<DadosAgenciasSuspeitas> agenciasSuspeitas = transacaoService.listarAgenciasSuspeitas(LocalDate.now().getYear(),LocalDate.now().getMonthValue());

        List<DadosAgenciasSuspeitas> agenciasSuspeitasEsperadas = List.of(
                new DadosAgenciasSuspeitas(
                        agenciaOrigemSuspeita.getBanco(),
                        agenciaOrigemSuspeita.getAgencia(),
                        new BigDecimal("2500000000.00"),
                        "Saída")
        );

        assertThat(agenciasSuspeitas).isEqualTo(agenciasSuspeitasEsperadas);
    }

    @Test
    @DisplayName("Deveria retornar apenas 1 agência suspeita (com valor maior que 1.000.000.000) - 0001 - Agencia Destino")
    void listarAgenciasSuspeitas2() throws Exception {
        transacaoValida1.setValorTransacao(new BigDecimal(10000000));
        transacaoValida2.setValorTransacao(new BigDecimal(200000000));
        transacaoValida3.setValorTransacao(new BigDecimal(300000000));
        transacaoValida4.setValorTransacao(new BigDecimal(500000000));
        transacaoValida5.setValorTransacao(new BigDecimal(40000000));

        transacaoValida6.setValorTransacao(new BigDecimal(500000000));

        transacaoValida1.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida2.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida3.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida4.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida5.setDataHoraTransacao(LocalDateTime.now());
        transacaoValida6.setDataHoraTransacao(LocalDateTime.now());

        Conta agenciaDestinoSuspeita = new Conta("BANCO DO BRASIL", "0001", "0001-1");
        Conta agenciaDestinoSemSuspeita1= new Conta("BANCO DO BRASIL", "0002", "0001-1");
        Conta agenciaDestinoSemSuspeita2= new Conta("BANCO DO BRASIL", "0003", "0001-1");
        Conta agenciaDestinoSemSuspeita3= new Conta("BANCO DO BRASIL", "0004", "0001-1");
        Conta agenciaDestinoSemSuspeita4= new Conta("BANCO DO BRASIL", "0005", "0001-1");
        Conta agenciaDestinoSemSuspeita5= new Conta("BANCO DO BRASIL", "0006", "0001-1");
        Conta agenciaDestinoSemSuspeita6= new Conta("BANCO DO BRASIL", "0007", "0001-1");

        transacaoValida1.setContaDestino(agenciaDestinoSuspeita);
        transacaoValida2.setContaDestino(agenciaDestinoSuspeita);
        transacaoValida3.setContaDestino(agenciaDestinoSuspeita);
        transacaoValida4.setContaDestino(agenciaDestinoSuspeita);
        transacaoValida5.setContaDestino(agenciaDestinoSuspeita);
        transacaoValida6.setContaDestino(agenciaDestinoSemSuspeita1);

        transacaoValida1.setContaOrigem(agenciaDestinoSemSuspeita1);
        transacaoValida2.setContaOrigem(agenciaDestinoSemSuspeita2);
        transacaoValida3.setContaOrigem(agenciaDestinoSemSuspeita3);
        transacaoValida4.setContaOrigem(agenciaDestinoSemSuspeita4);
        transacaoValida5.setContaOrigem(agenciaDestinoSemSuspeita5);
        transacaoValida6.setContaOrigem(agenciaDestinoSemSuspeita6);

        MockMultipartFile arquivoCSV = criarArquivoCSVcsv(
                transacaoValida1,
                transacaoValida2,
                transacaoValida3,
                transacaoValida4,
                transacaoValida5,
                transacaoValida6
        );

        MvcResult mvcResult = mvc
                .perform(
                        multipart(HttpMethod.POST, "/transacoes/formulario")
                                .file(arquivoCSV)
                                .with(csrf().asHeader()))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        assertThat(model.get("mensagemCadastro")).isEqualTo("6 transações cadastradas com sucesso!");

        List<DadosAgenciasSuspeitas> agenciasSuspeitas = transacaoService.listarAgenciasSuspeitas(LocalDate.now().getYear(),LocalDate.now().getMonthValue());

        List<DadosAgenciasSuspeitas> agenciasSuspeitasEsperadas = List.of(
                new DadosAgenciasSuspeitas(
                        agenciaDestinoSuspeita.getBanco(),
                        agenciaDestinoSuspeita.getAgencia(),
                        new BigDecimal("1050000000.00"),
                        "Entrada")
        );

        assertThat(agenciasSuspeitas).isEqualTo(agenciasSuspeitasEsperadas);
    }
}