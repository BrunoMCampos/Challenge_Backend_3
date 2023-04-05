import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transacao {
    private final String bancoOrigem;
    private final String agenciaOrigem;
    private final String contaOrigem;
    private final String bancoDestino;
    private final String agenciaDestino;
    private final String contaDestino;
    private final BigDecimal valorTransacao;
    private final LocalDateTime dataHoraTransacao;

    public Transacao(String bancoOrigem, String agenciaOrigem, String contaOrigem, String bancoDestino, String agenciaDestino, String contaDestino, BigDecimal valorTransacao, LocalDateTime dataHoraTransacao) {
        this.bancoOrigem = bancoOrigem;
        this.agenciaOrigem = agenciaOrigem;
        this.contaOrigem = contaOrigem;
        this.bancoDestino = bancoDestino;
        this.agenciaDestino = agenciaDestino;
        this.contaDestino = contaDestino;
        this.valorTransacao = valorTransacao;
        this.dataHoraTransacao = dataHoraTransacao;
    }

    @Override
    public String toString() {
        return this.bancoOrigem + "," +
                this.agenciaOrigem + "," +
                this.contaOrigem + "," +
                this.bancoDestino + "," +
                this.agenciaDestino + "," +
                this.contaDestino + "," +
                this.valorTransacao + "," +
                this.dataHoraTransacao;
    }
}
