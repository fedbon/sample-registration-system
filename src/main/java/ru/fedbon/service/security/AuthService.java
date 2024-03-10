package ru.fedbon.service.security;

import ru.fedbon.dto.security.AuthenticationResponse;
import ru.fedbon.dto.security.RefreshTokenRequest;
import ru.fedbon.dto.security.SigninRequest;
import ru.fedbon.model.User;


public interface AuthService {

    AuthenticationResponse signin(SigninRequest signinRequest);

    User getCurrentUser();

    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
