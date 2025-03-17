package com.example.MotoBG.Security;

import com.example.MotoBG.User.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/about-us", "/contacts", "/images/**", "/access-denied", "/not-found").permitAll()
                        .requestMatchers("/sign-in", "/sign-up", "/submit").anonymous()
                        .requestMatchers("/orders/show-all", "/orders/submit-update-status/**", "/car-parts/add", "/car-parts/submit", "/car-parts/update-quantity/**", "/car-parts/submit-update-quantity/**", "/cars/add", "/cars/submit", "/cars/delete/**", "/cars/offers/add/**", "/cars/offers/submit", "/cars/offers/delete/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/sign-in")
                        .usernameParameter("usernameOrEmail")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/access-denied");
                        })

                )
                .logout((logout) -> logout.permitAll());
        return http.build();
    }
}