package com.example.devs.global.config;

import com.example.devs.global.security.jwt.JwtAuthenticationFilter;
import com.example.devs.global.security.jwt.JwtProperties;
import com.example.devs.global.security.oauth.GitHubOAuth2UserService;
import com.example.devs.global.security.oauth.OAuthProperties;
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

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class, OAuthProperties.class, CorsProperties.class})
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuthProperties oauthProperties;
    private final GitHubOAuth2UserService gitHubOAuth2UserService;
    private final CorsProperties corsProperties;

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
                        .requestMatchers(HttpMethod.POST, "/user/oauth/google/token").hasAuthority("OIDC_USER")
                        .requestMatchers(HttpMethod.POST, "/user/oauth/github/token").hasAuthority("OAUTH2_USER")
                        .requestMatchers(HttpMethod.PUT, "/user/major").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/tech-stack").authenticated()

                        //oauth
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                        //major
                        .requestMatchers(HttpMethod.GET, "/majors").permitAll()

                        //dashboard
                        .requestMatchers(HttpMethod.GET, "/dashboard/summary").authenticated()
                        .requestMatchers(HttpMethod.GET, "/dashboard/popular-tech-stacks").authenticated()
                        .requestMatchers(HttpMethod.GET, "/dashboard/company-size-tech-stacks").authenticated()
                        .requestMatchers(HttpMethod.GET, "/dashboard/best-tech-stacks").authenticated()

                        .anyRequest().denyAll())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(gitHubOAuth2UserService))
                        .defaultSuccessUrl(
                                oauthProperties.frontendRedirectUri().toString(),
                                true
                        ))
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

        configuration.setAllowedOrigins(corsProperties.allowedOrigins());
        configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
