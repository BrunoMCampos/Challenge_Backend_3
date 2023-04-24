package br.com.alura.transacoes.infra.security;

import br.com.alura.transacoes.model.usuario.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetail extends Usuario implements UserDetails {

    public MyUserDetail(Usuario usuario) {
        this.setAtivo(usuario.getAtivo());
        this.setEmail(usuario.getEmail());
        this.setNome(usuario.getNome());
        this.setSenha(usuario.getSenha());
    }
}
