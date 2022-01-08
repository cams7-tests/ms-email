package br.cams7.tests.ms.infra.configuration;

import br.cams7.tests.ms.infra.dataprovider.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    // @formatter:off
    return http.csrf()
        .disable()
        .authorizeExchange()
        .pathMatchers(HttpMethod.POST, "/send-email-directly", "/send-email-to-queue")
        .hasRole("ADMIN")
        .pathMatchers(HttpMethod.GET, "/emails", "/emails?**", "/emails/**")
        .hasRole("USER")
        .pathMatchers(HttpMethod.GET, "/webjars/**", "/v3/api-docs/**", "/swagger-ui.html")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .formLogin()
        .and()
        .httpBasic()
        .and()
        .build();
    // @formatter:on
  }

  @Bean
  public ReactiveAuthenticationManager authenticationManager(UserDetailsService service) {
    return new UserDetailsRepositoryReactiveAuthenticationManager(service);
  }
}
