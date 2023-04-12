package br.com.alura.transacoes.infra.security;

import br.com.alura.transacoes.model.usuario.Usuario;
import br.com.alura.transacoes.model.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if(optionalUsuario.isEmpty()){
            throw new UsernameNotFoundException("Usuário não existe!");
        }
        return optionalUsuario.get();
    }
}
