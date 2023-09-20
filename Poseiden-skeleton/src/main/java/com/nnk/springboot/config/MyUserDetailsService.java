package com.nnk.springboot.config;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation personnalisée de l'interface UserDetailsService utilisé par SpringSecurity.
 * Va permettre de charger les informations d'un utilisateur depuis la base de donnée
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    /**
     * Le Repository pour accéder aux informations des utilisateurs.
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * Récupère à partir du "username" des utilisateurs les informations nécessaires à l'authentification
     * Spring va créer une instance de UserDetail à partir des informations présente dans la BDD
     * et vérifier que le mot de passe saisi par l'utilisateur correspond bien au mot de passe de UserDetail
     *
     * @param username Le nom de l'utilisateur à récupérer dans la base de donnée.
     * @return un Objet UserDetails générée par Spring Security, qui sera utilisé pour l'authentification.
     * Il contiendra le nom, le mot de passe et le role de l'utilisateur.
     * @throws UsernameNotFoundException Si le nom d'utilisateur n'est pas trouvé dans la base de donnée.
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }
}