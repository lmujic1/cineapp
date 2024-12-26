package ba.unsa.etf.cineapp.config;

import ba.unsa.etf.cineapp.model.auth.Role;
import ba.unsa.etf.cineapp.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JWTAuthenticationFilter jwtAuthenticationFilter;
  private final UserService userService;
  private final CustomLogoutHandler logoutHandler;
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.GET, "api/movies/**", "api/venues/**", "api/projections/**", "api/genres", "api/cities", "api/countries")
            .permitAll()
            .requestMatchers("api/auth/**", "api/password-reset/**").permitAll()
            .requestMatchers(HttpMethod.POST, "api/user/**", "api/movies/**", "api/venues/**", "api/actors", "api/writers", "api/images").hasAnyAuthority(Role.SUPERADMIN.name(), Role.ADMIN.name())
            .requestMatchers(HttpMethod.PUT,"api/movies/**", "api/venues", "api/actors", "api/writers", "api/images").hasAnyAuthority(Role.SUPERADMIN.name(), Role.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE, "/api/user/**", "api/movies/**", "api/venues", "api/actors", "api/projections/**", "api/writers", "api/images").hasAnyAuthority(Role.SUPERADMIN.name(), Role.ADMIN.name())
            .requestMatchers("/api/user/**", "/api/tickets", "/api/tickets/create-payment-intent", "/api/tickets/**").hasAnyAuthority(Role.USER.name(), Role.SUPERADMIN.name(), Role.ADMIN.name())
            .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority(Role.SUPERADMIN.name(), Role.ADMIN.name())
            .requestMatchers(HttpMethod.GET, "/api/super-admin/**").hasAnyAuthority(Role.SUPERADMIN.name())
            .anyRequest().authenticated())
        .exceptionHandling(e->e.accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider()).addFilterBefore(
            jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
        ).logout(l->l
            .logoutUrl("/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userService.userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
