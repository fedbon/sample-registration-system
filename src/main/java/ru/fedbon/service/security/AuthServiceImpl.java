package ru.fedbon.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fedbon.constants.ErrorMessage;
import ru.fedbon.dto.security.AuthenticationResponse;
import ru.fedbon.dto.security.RefreshTokenRequest;
import ru.fedbon.dto.security.SigninRequest;
import ru.fedbon.exception.NotFoundException;
import ru.fedbon.model.User;
import ru.fedbon.repository.UserRepository;
import ru.fedbon.util.JwtProvider;

import java.time.Instant;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResponse signin(SigninRequest signinRequest) {
        var authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinRequest.getUsername(),
                signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        var token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(signinRequest.getUsername())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var username = principal.getSubject();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND + username));
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshTokenRequest.getUsername());
        var token = jwtProvider.generateTokenWithUserDetails(userDetails);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
