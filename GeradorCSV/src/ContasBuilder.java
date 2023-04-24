import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContasBuilder {

    public static List<Conta> contasPadrao = new ArrayList<>();

    private static void gerarContas() {
        List<String> bancos = Arrays.asList("BANCO DO BRASIL", "BANCO BANRISUL", "BANCO ITAU", "BANCO SANTANDER", "NUBANK", "BANCO INTER", "BANCO BRADESCO", "CAIXA ECONOMICA FEDERAL");

        bancos.forEach(banco -> {
            for (int i = 1; i < 5001; i++) {
                Conta conta = new Conta(String.format("%04d", i) + "-1", "0001", banco);
                contasPadrao.add(conta);
            }
        });
    }

    public static List<Conta> getContasPadrao(){
        if(contasPadrao == null || contasPadrao.size() == 0){
            gerarContas();
        }
        return contasPadrao;
    }
}
