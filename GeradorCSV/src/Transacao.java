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

    public String toXML() {
        String XML = "  <transacao>\n";

        XML = XML.concat("    <origem>\n");

        XML = XML.concat("      <banco>");
        XML = XML.concat(this.bancoOrigem);
        XML = XML.concat("</banco>\n");

        XML = XML.concat("      <agencia>");
        XML = XML.concat(this.agenciaOrigem);
        XML = XML.concat("</agencia>\n");

        XML = XML.concat("      <conta>");
        XML = XML.concat(this.contaOrigem);
        XML = XML.concat("</conta>\n");

        XML = XML.concat("    </origem>\n");

        XML = XML.concat("    <destino>\n");

        XML = XML.concat("      <banco>");
        XML = XML.concat(this.bancoDestino);
        XML = XML.concat("</banco>\n");

        XML = XML.concat("      <agencia>");
        XML = XML.concat(this.agenciaDestino);
        XML = XML.concat("</agencia>\n");

        XML = XML.concat("      <conta>");
        XML = XML.concat(this.contaDestino);
        XML = XML.concat("</conta>\n");

        XML = XML.concat("    </destino>\n");

        XML = XML.concat("    <valor>");
        XML = XML.concat(this.valorTransacao.toPlainString());
        XML = XML.concat("</valor>\n");

        XML = XML.concat("    <data>");
        XML = XML.concat(this.dataHoraTransacao.toString());
        XML = XML.concat("</data>\n");

        XML = XML.concat("  </transacao>");
        return XML;
    }
}
