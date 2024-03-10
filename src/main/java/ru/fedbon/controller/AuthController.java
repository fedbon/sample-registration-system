package ru.fedbon.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fedbon.dto.security.AuthenticationResponse;
import ru.fedbon.dto.security.RefreshTokenRequest;
import ru.fedbon.dto.security.SigninRequest;
import ru.fedbon.service.security.UserDetailsServiceImpl;
import ru.fedbon.service.security.AuthServiceImpl;
import ru.fedbon.service.security.RefreshTokenServiceImpl;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    private final RefreshTokenServiceImpl refreshTokenService;

    private final UserDetailsServiceImpl userDetailsService;


    @PostMapping("/signin")
    public AuthenticationResponse handleSignin(@RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse handleRefreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> handleSignout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity
                .status(OK)
                .body("Refresh token deleted successfully");
    }

    @GetMapping("/user/{username}")
    public UserDetails handleSendDraftUserRequest(@PathVariable String username) {
        return userDetailsService.loadUserByUsername(username);
    }
}
