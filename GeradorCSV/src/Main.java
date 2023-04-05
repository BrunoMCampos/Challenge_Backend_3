import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static List<String> bancos = Arrays.asList("BANCO DO BRASIL", "BANCO SANTANDER", "BANCO BRADESCO", "BANCO BANRISUL", "BANCO ITAU", "NUBANK", "BANCO INTER", "CAIXA ECONOMICA FEDERAL");

    public static int dia;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Entre com o dia do CSV.");
        dia = scanner.nextInt();

        while(dia < 0 || dia > 28){
            System.out.println("Entre com o dia do CSV.");
            dia = scanner.nextInt();
        }

        Random random = new Random();
        int numeroDeRegistros = random.nextInt(20);
        String nomeArquivo = "Arquivo_CSV" + random.nextInt(1,10000) + "_dia_" + dia + ".txt";
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
        BigDecimal valorTransacao = new BigDecimal(random.nextInt(1000) * random.nextInt(1000));
        Transacao transacao = new Transacao(
                bancos.get(random.nextInt(bancos.size())),
                String.valueOf(random.nextInt(10000)),
                String.valueOf(random.nextInt(100000)).concat("-").concat(String.valueOf(random.nextInt(10))),
                bancos.get(random.nextInt(bancos.size())),
                String.valueOf(random.nextInt(10000)),
                String.valueOf(random.nextInt(100000)).concat("-").concat(String.valueOf(random.nextInt(10))),
                valorTransacao.divide(new BigDecimal(random.nextInt(1,10)),2,RoundingMode.HALF_UP),
                gerarData()
        );
        return transacao.toString();
    }

    public static LocalDateTime gerarData() {
        Random random = new Random();
        int valor = random.nextInt(100);
        if (valor < 3) {
            return LocalDateTime.now().withDayOfMonth(dia).plusDays(valor);
        } else {
            return LocalDateTime.now().withDayOfMonth(dia).plusSeconds(random.nextInt(1000));
        }
    }
}