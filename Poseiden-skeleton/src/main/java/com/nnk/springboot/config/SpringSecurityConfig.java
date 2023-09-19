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


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(/*"/bidList/**",*/ "/rating/**",
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

    //Cette methode va indiquer à SpringSecurity d'utiliser ma classe
    // MyUserDetailsService (implements UserDetailsService) pour gérer l'authentification.
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}