package com.mohsinon.core.security;

import com.mohsinon.core.security.authorization.repository.UserGlobalRoleRepository;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserGlobalRoleRepository userGlobalRoleRepository;

    public JwtAuthenticationFilter(TokenProvider tokenProvider,
                                   UserRepository userRepository,
                                   UserGlobalRoleRepository userGlobalRoleRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userGlobalRoleRepository = userGlobalRoleRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token) && tokenProvider.validateAccessToken(token)) {
            try {
                Claims claims = tokenProvider.getClaimsFromToken(token);
                UUID userId = UUID.fromString(claims.getSubject());

                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    if (user.getStatus() == UserStatus.ACTIVE) {
                        Set<String> roleNames = userGlobalRoleRepository.findRoleNamesByUserId(user.getId());
                        List<SimpleGrantedAuthority> authorities;
                        if (roleNames == null || roleNames.isEmpty()) {
                            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                        } else {
                            authorities = roleNames.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList();
                        }

                        UserPrincipal principal = UserPrincipal.fromUser(user, authorities);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.warn("Authenticated user {} has inactive status: {}", userId, user.getStatus());
                    }
                }
            } catch (Exception ex) {
                log.error("Could not set user authentication in security context: {}", ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }
}
