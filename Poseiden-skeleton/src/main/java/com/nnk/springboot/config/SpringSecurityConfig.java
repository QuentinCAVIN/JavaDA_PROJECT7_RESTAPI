package com.nnk.springboot.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Classe de Configuration de SringSecurity.
 * Configure les paramètres de sécurité, la gestion des accès,
 * l'authentification, la gestion des sessions, et les pages d'erreur personnalisées.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    /**
     * Interface UserDetailsService personnalisée dans la classe MyUserDetailsService
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Fournis un encodeur de mot de passe
     *
     * @return Un PasswordEncoder pour encoder les mots de passe.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Chaîne de filtres qui contrôle la manière dont les requêtes
     * entrantes sont traitées par Spring Security.
     *
     * @param http Objet HttpSecurity fournis par SpringSecurity utilisé pour configurer la chaine de filtres
     * @return Un objet SecurityFilterChain qui est un conteneur pour la configuration de sécurité,
     * configuré avec des règles spécifiques.
     * @throws Exception Si une exception se produit lors de la configuration de la sécurité.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/bidList/**", "/rating/**",
                                "/trade/**", "/curvePoint/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers( "/ruleName/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/bidList/**").hasAnyAuthority("USER")// TEST
                        .requestMatchers("/","/login","/user/**",
                                "/css/bootstrap.min.css","/app/**").permitAll()
                ).formLogin(
                        form -> form
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/bidList/list", true)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/")
                                .permitAll()
                ).exceptionHandling(ex -> ex.accessDeniedPage("/app/error"));// Ne fonctionne pas

        return http.build();
    }

    /**
     * Configuration les authentifications dans l'application.
     * Précise quel implementation de UserDetails Service et quel encodeur de mot de passe utiliser
     *
     * @param auth Classe AuthenticationManagerBuilder fournie par Spring
     *            pour configurer l'authentification.
     * @throws Exception Si une exception se produit lors de la configuration de l'authentification.
     */
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}