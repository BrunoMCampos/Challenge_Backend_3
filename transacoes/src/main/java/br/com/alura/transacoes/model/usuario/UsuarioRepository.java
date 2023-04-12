package br.com.alura.transacoes.model.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @Query("select u from Usuario u where u.ativo = true and u.nome != 'admin' and u.email != 'admin@email.com.br' and u.email != :email")
    List<Usuario> listarUsuariosMenosAdminEProprio(String email);
}
