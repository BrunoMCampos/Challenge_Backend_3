package br.com.alura.transacoes.model.usuario;

public record DadosListarUsuarios( String nome, String email) {
    public DadosListarUsuarios(Usuario usuario){
        this(
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
