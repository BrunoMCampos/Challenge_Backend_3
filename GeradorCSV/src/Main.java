import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Main {
    public static List<Conta> contas = ContasBuilder.getContasPadrao();

    public static String formatoArquivo;

    public static LocalDate comeco;
    public static LocalDate fim;

    public static String nomeArquivo;
    public static boolean sobrepor;

    public static int limiteDeTransacoesPorArquivo;

    private static File pastaAno;
    private static final List<String> pastasMes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Bem vindo ao gerador de relatórios!".toUpperCase());

        formatoArquivo = decidirFormatoArquivo();

        decidirPeriodo();

        limiteDeTransacoesPorArquivo = decidirLimiteTransacoesPorArquivo();

        sobrepor = verificarSobrepor();
        verificarECriarPastas();

        System.out.println("Gerando arquivo ".toUpperCase() + formatoArquivo.toUpperCase());
        gerarRelatorio();
    }

    private static String decidirFormatoArquivo() {
        return MensagensBuilder.exibirMensagem("Deseja gerar um relatório em formato CSV ou XML?", List.of("CSV", "XML")).toLowerCase();
    }

    private static int decidirLimiteTransacoesPorArquivo() {
        return MensagensBuilder.exibirMensagem("Defina um número limite de transações dentro de cada arquivo", 1, 10000, 1000);
    }

    private static int decidindoAno() {
        return MensagensBuilder.exibirMensagem("Entre com o ano desejado para o relatório, apenas com 4 números.", 1000, 9999, 2023);
    }

    private static int decidindoMes() {
        return MensagensBuilder.exibirMensagem("Entre com o número referente ao mês desejado para o relatório.", 1, 12, 1);
    }

    private static int decidindoDia() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, comeco.getMonthValue() - 1);
        int primeiroDiaDoMes = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int ultimoDiaDoMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return MensagensBuilder.exibirMensagem("Entre com o dia desejado para o relatório.", primeiroDiaDoMes, ultimoDiaDoMes, 10);
    }

    private static void decidirPeriodo() {
        String periodo = MensagensBuilder.exibirMensagem("Deseja gerar o relatório para o dia, mês ou ano?", List.of("DIA", "MÊS", "ANO"));

        int ano = decidindoAno();

        if (periodo.equalsIgnoreCase("ano")) {
            comeco = LocalDate.of(ano, Month.JANUARY, 1);
            fim = LocalDate.of(ano, Month.JANUARY, 1).plusYears(1);
        }

        if (periodo.equalsIgnoreCase("mês")) {
            int mes = decidindoMes();
            comeco = LocalDate.of(ano, mes, 1);
            fim = LocalDate.of(ano, mes, 1).plusMonths(1);
        }

        if (periodo.equalsIgnoreCase("dia")) {
            int mes = decidindoMes();
            comeco = LocalDate.of(ano, mes, 1);
            fim = LocalDate.of(ano, mes, 1).plusDays(1);
            int dia = decidindoDia();
            comeco = LocalDate.of(ano, mes, dia);
            fim = LocalDate.of(ano, mes, dia).plusDays(1);
        }
    }

    private static void gerarRelatorio() throws IOException {
        int contador = 0;
        String mesAtual = pastasMes.get(contador);
        while (!comeco.equals(fim)) {
            if (!mesAtual.equalsIgnoreCase(String.valueOf(comeco.getMonthValue()))) {
                contador++;
                mesAtual = pastasMes.get(contador);
            }
            OutputStream arquivo = new FileOutputStream(nomeArquivo);
            int numeroDeRegistros = new Random().nextInt(limiteDeTransacoesPorArquivo);
            if (formatoArquivo.equalsIgnoreCase("csv")) {
                for (int i = 0; i < numeroDeRegistros; i++) {
                    arquivo.write(gerarTransacao(comeco, i).toString().getBytes());
                    arquivo.write("\n".getBytes());
                }
            } else if (formatoArquivo.equalsIgnoreCase("xml")) {
                arquivo.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
                arquivo.write("<transacoes>\n".getBytes());
                for (int i = 0; i < numeroDeRegistros; i++) {
                    arquivo.write(gerarTransacao(comeco, i).toXML().getBytes());
                    arquivo.write("\n".getBytes());
                }
                arquivo.write("</transacoes>".getBytes());
            }
            arquivo.close();
            comeco = comeco.plusDays(1);
            nomeArquivo = pastaAno.getAbsolutePath() + "/" + mesAtual + "/" + comeco.getDayOfMonth() + "." + formatoArquivo;
        }
    }

    private static void verificarECriarPastas() {
        pastaAno = new File(new File("").getAbsolutePath() + "/" + comeco.getYear());

        // Diário
        if (comeco.until(fim).getDays() == 1) {

            if (!pastaAno.exists()) { // Verifica se a pasta ano existe e se não existir a cria
                pastaAno.mkdir();
            }

            File pastaMes = new File(pastaAno.getAbsolutePath() + "/" + comeco.getMonthValue());
            if (!pastaMes.exists()) { // Verifica se a pasta mês existe e se não existir a cria
                pastaMes.mkdir();
            }

            File arquivoDia = new File(pastaMes.getAbsolutePath() + "/" + comeco.getDayOfMonth() + "." + formatoArquivo);
            if (arquivoDia.exists()) { // Verifica se o arquivo existe
                if (sobrepor) { // Se for para sobrepor deleta o arquivo
                    arquivoDia.delete();
                } else { // Se não for para sobrepor aumenta o contador e muda o nome do arquivo
                    int contador = 1;
                    while (arquivoDia.exists()) {
                        arquivoDia = new File(pastaMes.getAbsolutePath() + "/" + comeco.getDayOfMonth() + " (" + contador + ")" +"." + formatoArquivo);
                        contador++;
                    }
                }
            }

            pastasMes.add(pastaMes.getName());
            nomeArquivo = arquivoDia.getAbsolutePath();
        }

        // Mensal
        else if (comeco.until(fim).getMonths() == 1) {

            if (!pastaAno.exists()) { // Verifica se a pasta ano existe e se não existir a cria
                pastaAno.mkdir();
            }

            File pastaMes = new File(pastaAno.getAbsolutePath() + "/" + comeco.getMonthValue());
            if (pastaMes.exists()) { // Verifica se a pasta mês existe
                if (sobrepor) { // Se for para sobrepor apaga os arquivos internos da pasta
                    deletarArquivosInternos(pastaMes);
                } else { // Se não for para sobrepor cria uma nova pasta com um contador na frente do nome
                    int contador = 1;
                    while (pastaMes.exists()) {
                        pastaMes = new File(pastaMes.getAbsolutePath() + "/" + comeco.getDayOfMonth() + " (" + contador + ")");
                    }
                    pastaMes.mkdir();
                }
            } else {
                pastaMes.mkdir();
            }

            File arquivoDia = new File(pastaMes.getAbsolutePath() + "/" + comeco.getDayOfMonth() +"." + formatoArquivo);

            pastasMes.add(pastaMes.getName());
            nomeArquivo = arquivoDia.getAbsolutePath();
        }

        // Anual
        else if (comeco.until(fim).getYears() == 1) {
            File pastaMes = new File("");
            if (pastaAno.exists()) { // Verifica se a pasta ano existe
                if (sobrepor) { // Se for para sobrepor apaga os arquivos internos da pasta
                    for (int i = 1; i <= 12; i++) {
                        pastaMes = new File(pastaAno + "/" + i);
                        if (pastaMes.exists()) {
                            deletarArquivosInternos(pastaMes);
                        } else {
                            pastaMes.mkdir();
                        }
                        pastasMes.add(pastaMes.getName());
                    }
                } else { // Se NÃO for para sobrepor cria uma nova pasta com um contador na frente do nome
                    int contador = 1;
                    while (pastaAno.exists()) {
                        pastaAno = new File(pastaAno + " (" + contador + ")");
                    }
                    pastaAno.mkdir();
                    for (int i = 1; i <= 12; i++) {
                        pastaMes = new File(pastaAno + "/" + i);
                        pastaMes.mkdir();
                        pastasMes.add(pastaMes.getName());
                    }
                }
            } else {
                pastaAno.mkdir();
                for (int i = 1; i <= 12; i++) {
                    pastaMes = new File(pastaAno + "/" + i);
                    pastaMes.mkdir();
                    pastasMes.add(pastaMes.getName());
                }
            }

            File arquivoDia = new File(pastaAno.getAbsolutePath() + "/" + pastasMes.get(0) + "/" + comeco.getDayOfMonth() +"." + formatoArquivo);

            nomeArquivo = arquivoDia.getAbsolutePath();
        }


    }

    private static void deletarArquivosInternos(File pasta) {
        File[] files = pasta.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private static Boolean verificarSobrepor() {
        return MensagensBuilder.exibirMensagemDeSimOuNao("Deseja sobrepor qualquer pastas e arquivos existentes?");
    }

    public static Transacao gerarTransacao(LocalDate diaAtual, int numeroRegistro) {
        Random random = new Random();

        Conta contaOrigem = contas.get(random.nextInt(contas.size()));
        Conta contaDestino;

        if (random.nextInt(10) <= 8) {
            while (!contaOrigem.getBanco().equalsIgnoreCase("BANCO DO BRASIL")) {
                contaOrigem = contas.get(random.nextInt(contas.size()));
            }
        }

        do {
            contaDestino = contas.get(random.nextInt(contas.size()));
        } while (contaOrigem.equals(contaDestino));

        BigDecimal valorTransacao;

        if (random.nextInt(10000) <= 1) {
            valorTransacao = new BigDecimal(BigInteger.valueOf(new Random().nextInt(10000100, 80000000)), 2);
        } else {
            valorTransacao = new BigDecimal(BigInteger.valueOf(new Random().nextInt(10000000)), 2);
        }

        return new Transacao(
                contaOrigem.getBanco(),
                contaOrigem.getAgencia(),
                contaOrigem.getNumero(),
                contaDestino.getBanco(),
                contaDestino.getAgencia(),
                contaDestino.getNumero(),
                valorTransacao,
                gerarData(diaAtual, numeroRegistro));
    }

    public static LocalDateTime gerarData(LocalDate diaAtual, int numeroRegistro) {
        Random random = new Random();
        int valor = random.nextInt(100);

        if (valor < 8 && numeroRegistro > 1) {
            return LocalDateTime.of(diaAtual, LocalTime.of(random.nextInt(23), random.nextInt(59), random.nextInt(59))).plusDays(random.nextInt(100));
        } else {
            return LocalDateTime.of(diaAtual, LocalTime.of(random.nextInt(23), random.nextInt(40), random.nextInt(59))).plusSeconds(random.nextInt(1000));
        }
    }
}