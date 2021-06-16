package it.unibo.ingsoft.fortuna.autenticazione;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LocalAuthenticationProvider implements AuthenticationProvider {
    private final IAutenticazione authService;

    public static final Set<GrantedAuthority> ADMIN_ROLES = Set.of(new SimpleGrantedAuthority("ROLE_TITOLARE"));

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString(); 

        boolean success;
        try {
            success = authService.autentica(username, password);
        } catch (AutenticazioneException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        if (success) {
            return new UsernamePasswordAuthenticationToken(username, password, ADMIN_ROLES);
        } else {
            throw new BadCredentialsException("Credenziali non autorizzate!");
        }
    }

    /**
     * Returns true if this AuthenticationProvider supports the indicated Authentication object.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
    
}
