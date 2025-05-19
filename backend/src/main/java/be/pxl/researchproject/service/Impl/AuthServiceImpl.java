package be.pxl.researchproject.service.Impl;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import be.pxl.researchproject.api.response.UserDTO;
import be.pxl.researchproject.domain.User;
import be.pxl.researchproject.security.JwtProvider;
import be.pxl.researchproject.security.UserPrincipal;
import be.pxl.researchproject.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtProvider jwtProvider;

    public AuthServiceImpl(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Map<String, Object> createLogInfo(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        UserDTO userDTO = new UserDTO(user);
        String token = jwtProvider.createToken(authentication);
        Map<String, Object> logInfoMap = Map.of("userInfo", userDTO, "token", token);
        return logInfoMap;
    }

}
