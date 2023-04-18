import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static List<Conta> contas = new ArrayList<>();

    public static String formatoArquivo;
    public static String periodoRelatorio;
    public static String nomeArquivo;

    public static int dia = 1;
    public static int mes = 1;
    public static int ano;

    public static File pastaAno;
    public static File pastaMes;

    public static int limiteDeTransacoesPorArquivo;

    public static Scanner scanner = new Scanner(System.in);

    public static boolean sobrepor;

    public static void main(String[] args) throws IOException {
        System.out.println("Bem vindo ao gerador de relatórios!");
        gerarContas();

        decidindoFormatoArquivo();

        decidindoPeriodo();
        decidindoDiaMesAno();
        decidindoQuantidadeLimiteDeTransacoesPorArquivo();

        verificarECriarPastas();

        gerarRelatorio();
    }

    private static void decidindoQuantidadeLimiteDeTransacoesPorArquivo() {
        System.out.println("Cada arquivo gerado tem um número aleatório de transações com um limite definido como padrão para até 1.000 transações.");
        System.out.println("Caso queira, você pode definir um novo limite de transações para ser utilizado, dentro do máximo de 10.000 transações e mínimo de 1 transação.");
        System.out.println("Caso um valor inválido seja digitado será utilizado o valor default de 1.000 transações para realizar a geração do relatório.");
        System.out.println("Defina um número limite de transações dentro de cada arquivo [Default - 1.000]");
        limiteDeTransacoesPorArquivo = scanner.nextInt();
        if (limiteDeTransacoesPorArquivo < 1 || limiteDeTransacoesPorArquivo > 10000) {
            System.out.println("Limite Default de 1.000 transações definido para utilização");
            limiteDeTransacoesPorArquivo = 1000;
        } else {
            System.out.println("Limite de " + limiteDeTransacoesPorArquivo + " transações definido para utilização");
        }
    }

    private static void decidindoDiaMesAno() {
        decidindoAno();
        if (!periodoRelatorio.equalsIgnoreCase("ANUAL")) {
            decidindoMes();
            if (!periodoRelatorio.equalsIgnoreCase("MENSAL")) {
                decidindoDia();
            }
        }
    }

    private static void decidindoDia() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, ano);
        calendar.set(Calendar.MONTH, mes);
        int primeiroDiaDoMes = calendar.getMinimum(Calendar.DAY_OF_MONTH);
        int ultimoDiaDoMes = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("Entre com o dia desejado para o relatório [" + primeiroDiaDoMes + " a " + ultimoDiaDoMes + "]");
        dia = scanner.nextInt();
        while (dia > ultimoDiaDoMes || dia < primeiroDiaDoMes) {
            System.out.println("Valor selecionado inválido, favor digitar os números referentes ao dia do mês desejado [" + primeiroDiaDoMes + " a " + ultimoDiaDoMes + "]");
            System.out.println("Entre com o dia desejado para o relatório [" + primeiroDiaDoMes + " a " + ultimoDiaDoMes + "]");
            dia = scanner.nextInt();
        }
    }

    private static void decidindoMes() {
        System.out.println("Entre com o mês desejado para o relatório [1 a 12]");
        mes = scanner.nextInt();
        while (mes > 12 || mes < 1) {
            System.out.println("Valor selecionado inválido, favor digitar o número referente ao mês desejado [1 a 12]");
            System.out.println("Entre com o mês desejado para o relatório [1 A 12]");
            mes = scanner.nextInt();
        }
    }

    private static void decidindoAno() {
        System.out.println("Entre com o ano desejado para o relatório, apenas com 4 números [Ex.: 2023]");
        ano = scanner.nextInt();
        while (String.valueOf(ano).length() != 4) {
            System.out.println("Valor selecionado inválido, favor digitar 4 números referentes ao ano desejado [Ex.: 2023]");
            System.out.println("Entre com o ano desejado para o relatório, apenas com 4 números [Ex.: 2023]");
            ano = scanner.nextInt();
        }
    }

    private static void decidindoPeriodo() {
        System.out.println("Deseja gerar o relatório diário, mensal ou anual? [1 - Diário ; 2 - Mensal ; 3 - Anual]");
        int opcaoPeriodoRelatorio = scanner.nextInt();
        do {
            if (opcaoPeriodoRelatorio == 1) {
                periodoRelatorio = "DIARIO";
            } else if (opcaoPeriodoRelatorio == 2) {
                periodoRelatorio = "MENSAL";
            } else if (opcaoPeriodoRelatorio == 3) {
                periodoRelatorio = "ANUAL";
            } else {
                System.out.println("Valor selecionado inválido, favor digitar apenas o número referente a opção desejada [1 - Diário ; 2 - Mensal ; 3 - Anual]");
                System.out.println("Deseja gerar o relatório diário, mensal ou anual? [1 - Diário ; 2 - Mensal ; 3 - Anual]");
                opcaoPeriodoRelatorio = scanner.nextInt();
            }
        } while (opcaoPeriodoRelatorio > 3 || opcaoPeriodoRelatorio < 1);
    }

    private static void decidindoFormatoArquivo() {
        System.out.println("Deseja gerar um relatório em formato CSV ou XML? [Escolha o número correspondente]");
        System.out.println("1 - CSV");
        System.out.println("2 - XML");
        int opcaoFormatoArquivo;
        do {
            opcaoFormatoArquivo = scanner.nextInt();
            if (opcaoFormatoArquivo == 1) {
                System.out.println("Opção escolhida: CSV");
                formatoArquivo = "CSV";
            } else if (opcaoFormatoArquivo == 2) {
                System.out.println("Opção escolhida: XML");
                formatoArquivo = "XML";
            } else {
                System.out.println("Valor selecionado inválido, favor digitar apenas o número referente a opção desejada [1 - CSV ; 2 - XML]");
                System.out.println("Deseja gerar um relatório em formato CSV ou XML? [Escolha o número correspondente]");
                System.out.println("1 - CSV");
                System.out.println("2 - XML");
            }
        } while (opcaoFormatoArquivo > 2 || opcaoFormatoArquivo < 1);
    }

    private static void gerarContas() {
        List<String> bancos = Arrays.asList("BANCO DO BRASIL", "BANCO BANRISUL", "BANCO ITAU", "BANCO SANTANDER", "NUBANK", "BANCO INTER", "BANCO BRADESCO", "CAIXA ECONOMICA FEDERAL");

        bancos.forEach(banco -> {
            for (int i = 1; i < 5001; i++) {
                Conta conta = new Conta(String.format("%04d", i) + "-1", "0001", banco);
                contas.add(conta);
            }
        });
    }

    private static void gerarRelatorio() throws IOException {
        LocalDate comeco;
        LocalDate fim;

        if (periodoRelatorio.equalsIgnoreCase("ANUAL")) {
            System.out.println("Gerando relatório para o ano de " + ano);
            comeco = LocalDate.of(ano, 1, 1);
            fim = LocalDate.of(ano + 1, 1, 1);
        } else if (periodoRelatorio.equalsIgnoreCase("MENSAL")) {
            System.out.println("Gerando relatório para o mês " + mes + "/" + ano);
            comeco = LocalDate.of(ano, mes, 1);
            fim = LocalDate.of(ano, mes + 1, 1);
        } else{
            System.out.println("Gerando relatório para dia " + dia + "/" + mes + "/" + ano);
            comeco = LocalDate.of(ano, mes, dia);
            fim = LocalDate.of(ano, mes, dia + 1);
        }

        gerarArquivos(comeco, fim);
    }

    private static void gerarArquivos(LocalDate comeco, LocalDate fim) throws IOException {
        String path = pastaAno.getAbsolutePath();
        if(mes > 0){
            path = path.concat("/" + mes);
        }
        while(!comeco.equals(fim)){
            nomeArquivo = path + "/Arquivo_" + formatoArquivo + "_" + comeco.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + ".txt";
            OutputStream arquivo = new FileOutputStream(nomeArquivo);
            int numeroDeRegistros = new Random().nextInt(limiteDeTransacoesPorArquivo);
            if(formatoArquivo.equalsIgnoreCase("csv")){
                for (int i = 0; i < numeroDeRegistros; i++) {
                    arquivo.write(gerarTransacao(comeco, i).toString().getBytes());
                    arquivo.write("\n".getBytes());
                }
            } else if(formatoArquivo.equalsIgnoreCase("xml")){
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
        }
    }

    private static void verificarECriarPastas() {
        verificarSobrepor();

        if (periodoRelatorio.equalsIgnoreCase("MENSAL") || periodoRelatorio.equalsIgnoreCase("DIARIO")) {
            pastaMes = new File(new File("").getAbsolutePath() + "/" + ano + "/" + mes);
            verificarECriarPasta(pastaMes, mes);
        } else {
            File pasta = new File(pastaAno.getAbsolutePath());
            for(int i = 1 ; i <= 12 ; i++){
                File subpasta = new File(pasta.getAbsolutePath() + "/" + i);
                File[] files = subpasta.listFiles();
                if(files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
                subpasta.delete();
                subpasta.mkdir();
            }
        }
    }

    private static void verificarSobrepor() {
        pastaAno = new File(new File("").getAbsolutePath() + "/" + ano);
        if(periodoRelatorio.equalsIgnoreCase("MENSAL") || periodoRelatorio.equalsIgnoreCase("DIARIO")){
            pastaMes = new File(pastaAno.getAbsolutePath() + "/" + mes);
            if(pastaMes.exists()){
                String opcaoSobrepor;
                do{
                    System.out.println("A pasta " + pastaMes.getAbsolutePath() + " já existe, deseja sobrepor? [S - Sim ; N - Não]");
                    opcaoSobrepor = scanner.next();
                    if(opcaoSobrepor.equalsIgnoreCase("s")){
                        sobrepor = true;
                    } else if(opcaoSobrepor.equalsIgnoreCase("n")) {
                        sobrepor = false;
                    }
                } while (!opcaoSobrepor.equalsIgnoreCase("s") && !opcaoSobrepor.equalsIgnoreCase("n"));
            }
        } else {
            if(pastaMes.exists()){
                String opcaoSobrepor;
                do{
                    System.out.println("A pasta " + pastaMes.getAbsolutePath() + " já existe, deseja sobrepor? [S - Sim ; N - Não]");
                    opcaoSobrepor = scanner.next();
                    if(opcaoSobrepor.equalsIgnoreCase("s")){
                        sobrepor = true;
                    } else if(opcaoSobrepor.equalsIgnoreCase("n")) {
                        sobrepor = false;
                    }
                } while (!opcaoSobrepor.equalsIgnoreCase("s") && !opcaoSobrepor.equalsIgnoreCase("n"));
            }
        }
    }

    private static void deletarPastaECriarNovamente(File pasta) {
        if (!pasta.delete()) {
            File[] files = pasta.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
                pasta.delete();
            }
        }
        pasta.mkdir();
    }

    private static boolean reutilizarPasta(File pasta, int mesOuAno) {
        String opcaoReutilizar;
        do {
            System.out.println("Pasta referete a " + mesOuAno + " já existente como \"" + pasta.getPath() + "\", deseja reutilizar? (Todos os dados dentro da pasta serão apagados) [S - Sim ; N - Não]");
            opcaoReutilizar = scanner.next();
            if (opcaoReutilizar.equalsIgnoreCase("s")) {
                return true;
            } else if (opcaoReutilizar.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Valor selecionado inválido, favor digitar apenas a letra referente a opção desejada [S - Sim ; N - Não]");
            }
        } while (!opcaoReutilizar.equalsIgnoreCase("s") && !opcaoReutilizar.equalsIgnoreCase("n"));
        throw new RuntimeException("Erro");
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