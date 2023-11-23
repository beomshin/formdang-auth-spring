package com.kr.formdang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {

        http.httpBasic().disable(); // REST API로 사용안함

        http.csrf().disable();

        http.formLogin().disable();

        http.headers().frameOptions().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // REST API 사용안함;

        http.cors().configurationSource(corsConfigurationSource);


        http.authorizeHttpRequests()
//                .antMatchers("/**").permitAll()
//                .anyRequest().hasRole("USER");
                .anyRequest().permitAll();



        return http.build();
    }


}
