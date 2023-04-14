import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static List<Conta> contas = new ArrayList<>();

    public static int dia;
    public static int mes;
    public static int ano;

    public static void main(String[] args) throws IOException {
        String resposta;
        gerarContas();
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Deseja gerar relatório para todo o mês?");
            resposta = scanner.next();
            if (resposta.substring(0, 1).equalsIgnoreCase("s")) {
                System.out.println("Qual mês? (1 a 12)");
                mes = scanner.nextInt();
                System.out.println("Qual ano?");
                ano = scanner.nextInt();
                for (dia = 1; dia < 28; dia++) {
                    criarArquivo();
                }
                resposta = "n";
            } else {
                System.out.println("Entre com o dia do CSV.");
                dia = scanner.nextInt();
                mes = LocalDate.now().getMonthValue();
                while (dia < 0 || dia > 28) {
                    System.out.println("Entre com o dia do CSV.");
                    dia = scanner.nextInt();
                }
                criarArquivo();
                System.out.println("Deseja gerar um novo arquivo? (S/N)");
                resposta = scanner.next();
            }
        } while (resposta.substring(0, 1).equalsIgnoreCase("S"));
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

    private static void criarArquivo() throws IOException {
        Random random = new Random();
        int numeroDeRegistros = random.nextInt(2000);
        String nomeArquivo = "Arquivo_CSV_ano_" + ano + "_mes_" + mes + "_dia_" + dia +  "_" + random.nextInt(1, 10000) + ".txt";
        OutputStream arquivo = new FileOutputStream(nomeArquivo);
        for (int i = 0; i < numeroDeRegistros; i++) {
            arquivo.write(gerarDados().getBytes());
            arquivo.write("\n".getBytes());
        }
        arquivo.close();
        System.out.println("Arquivo " + nomeArquivo + " criado com " + numeroDeRegistros + " registros!");
    }

    public static String gerarDados() {
        Random random = new Random();

        Conta contaOrigem = contas.get(random.nextInt(contas.size()));
        Conta contaDestino;

        if(random.nextInt(10) <= 8){
            while(!contaOrigem.getBanco().equalsIgnoreCase("BANCO DO BRASIL")) {
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

        Transacao transacao = new Transacao(
                contaOrigem.getBanco(),
                contaOrigem.getAgencia(),
                contaOrigem.getNumero(),
                contaDestino.getBanco(),
                contaDestino.getAgencia(),
                contaDestino.getNumero(),
                valorTransacao,
                gerarData());
        return transacao.toString();
    }

    public static LocalDateTime gerarData() {
        Random random = new Random();
        int valor = random.nextInt(100);
        if (valor < 8) {
            return LocalDateTime.of(ano, mes, dia, random.nextInt(23), random.nextInt(59), random.nextInt(59)).plusDays(random.nextInt(100));
        } else {
            return LocalDateTime.of(ano, mes, dia, random.nextInt(23), random.nextInt(59), random.nextInt(59)).plusSeconds(random.nextInt(1000));
        }
    }
}