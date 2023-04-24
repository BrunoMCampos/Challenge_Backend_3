import java.util.Objects;

public class Conta {
    private final String numero;
    private final String agencia;
    private final String banco;

    public Conta(String numero, String agencia, String banco) {
        this.numero = numero;
        this.agencia = agencia;
        this.banco = banco;
    }

    public String getNumero() {
        return numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getBanco() {
        return banco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Objects.equals(numero, conta.numero) && Objects.equals(agencia, conta.agencia) && Objects.equals(banco, conta.banco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, agencia, banco);
    }
}
