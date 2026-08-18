package com.example.devs.global.config;

import com.example.devs.global.security.jwt.JwtAuthenticationFilter;
import com.example.devs.global.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        //preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        //error
                        .requestMatchers("/error").permitAll()

                        //email
                        .requestMatchers(HttpMethod.POST, "/user/email/send").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/email/verify").permitAll()

                        //user
                        .requestMatchers(HttpMethod.POST, "/user/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/reissue").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/user/major").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/tech-stack").authenticated()

                        //major
                        .requestMatchers(HttpMethod.GET, "/majors").permitAll()

                        //dashboard
                        .requestMatchers(HttpMethod.GET, "/dashboard/summary").authenticated()
                        .requestMatchers(HttpMethod.GET, "/dashboard/popular-tech-stacks").authenticated()
                        .requestMatchers(HttpMethod.GET, "/dashboard/company-size-tech-stacks").authenticated()

                        .anyRequest().denyAll())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173"
        ));
        configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
