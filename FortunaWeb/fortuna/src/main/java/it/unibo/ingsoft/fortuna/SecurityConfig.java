package it.unibo.ingsoft.fortuna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import it.unibo.ingsoft.fortuna.autenticazione.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
        new AntPathRequestMatcher("/gest/**")
      );
    private static final RequestMatcher PUBLIC_URLS = new NegatedRequestMatcher(PROTECTED_URLS);
    
    // @Autowired
    // private MyBasicAuthenticationEntryPoint authenticationEntryPoint;

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.inMemoryAuthentication()
    //       .withUser("user1").password(passwordEncoder().encode("user1Pass"))
    //       .authorities("ROLE_USER");
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //Autorizza richieste solo a pagine pubbliche, richiedi autenticazione con ruolo titorale per altre
            .authorizeRequests()
                .requestMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().hasAnyAuthority("ROLE_TITOLARE")
                .and()
            // Pagina di logout di default non necessaria
            .logout().disable()
            // Disabilita Cross-Site-Request-Forgery, secondo qua richiederebbe anche modifiche a form https://www.marcobehler.com/guides/spring-security
            .csrf().disable()
            // Disabilita form login di default
            .formLogin().disable()
            // Abilita login via http basic, non è il massimo e richiederebbe un trattamento più ad HOC per cose REST
            // ma per ora tengo questo
            .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Autowired
    public AuthenticationProvider authenticationProvider(final IAutenticazione authService) {
        return new LocalAuthenticationProvider(authService);
    }
}