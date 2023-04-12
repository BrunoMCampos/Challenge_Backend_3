package br.com.alura.transacoes.model.usuario;

public record DadosListarUsuarios(Long id, String nome, String email) {
    public DadosListarUsuarios(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
