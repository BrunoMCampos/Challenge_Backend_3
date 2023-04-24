package br.com.alura.transacoes.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder())
                .and()
                .jdbcAuthentication()
                .dataSource(dataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .authorizeHttpRequests()
                        .anyRequest().authenticated()
                        .and().formLogin().loginPage("/usuarios/login").permitAll()
                        .defaultSuccessUrl("/transacoes/formulario", true)
                        .passwordParameter("senha").usernameParameter("email")
                        .and().logout()
                        .logoutSuccessUrl("/usuarios/login").permitAll()
                        .and().httpBasic().disable().build();
    }

}
