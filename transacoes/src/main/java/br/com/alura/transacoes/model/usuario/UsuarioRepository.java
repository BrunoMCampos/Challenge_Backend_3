package br.com.alura.transacoes.model.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);

    Usuario getReferenceByEmail(String email);

    @Query("select u from Usuario u where u.ativo = true and u.nome != 'admin' and u.email != 'admin@email.com.br' and u.email != :email")
    List<Usuario> listarUsuariosAtivosMenosAdminEProprio(String email);
}