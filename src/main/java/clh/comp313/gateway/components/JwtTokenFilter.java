package clh.comp313.gateway.components;

import clh.comp313.gateway.auth.services.AuthGrpcClientService;
import io.clh.gateway.auth.UserPermissionsRequest;
import io.clh.gateway.auth.UserPermissionsResponse;
import io.clh.gateway.auth.ValidateRequest;
import io.clh.gateway.auth.ValidateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final AuthGrpcClientService authGrpcClientService;

    @Autowired
    public JwtTokenFilter(AuthGrpcClientService authGrpcClientService) {
        this.authGrpcClientService = authGrpcClientService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null) {
            try {
                ValidateRequest grpcRequest = ValidateRequest.newBuilder().setToken(token).build();
                ValidateResponse grpcResponse = authGrpcClientService.authServiceStub().validateToken(grpcRequest);

                UserPermissionsRequest userPermissionsRequest = UserPermissionsRequest.newBuilder().setToken(token).build();
                UserPermissionsResponse userPermissions = authGrpcClientService.authServiceStub().getUserPermissions(userPermissionsRequest);

                int permissions = userPermissions.getPermissions();
                List<GrantedAuthority> authorities = mapPermissionsToAuthorities(permissions);

                if (grpcResponse.getValid()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(token, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private List<GrantedAuthority> mapPermissionsToAuthorities(int permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        switch (permissions) {
            case 0:
                authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
                break;
            case 1:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
        }

        return authorities;
    }
}