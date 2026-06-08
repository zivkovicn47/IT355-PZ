package com.metropolitan.it355pz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin"))
                .roles("ADMIN")
                .build();
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("user"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/css/**", "/js/**").permitAll()
                // Ograničavanje akcija izmena/dodavanja/brisanja samo na ADMIN ulogu
                .requestMatchers(
                    "/projekti/novi", "/projekti/sacuvaj", "/projekti/izmeni/**", "/projekti/obrisi/**",
                    "/komponente/novi", "/komponente/sacuvaj", "/komponente/izmeni/**", "/komponente/obrisi/**",
                    "/inzenjeri/novi", "/inzenjeri/sacuvaj", "/inzenjeri/izmeni/**", "/inzenjeri/obrisi/**",
                    "/licence/novi", "/licence/sacuvaj", "/licence/izmeni/**", "/licence/obrisi/**",
                    "/zadaci/novi", "/zadaci/sacuvaj", "/zadaci/izmeni/**", "/zadaci/obrisi/**"
                ).hasRole("ADMIN")
                // Dozvola za pregled listi i kontrolne table za uloge USER i ADMIN
                .requestMatchers("/", "/projekti", "/komponente", "/inzenjeri", "/licence", "/zadaci").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
}
