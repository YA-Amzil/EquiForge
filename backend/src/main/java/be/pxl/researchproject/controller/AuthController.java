package be.pxl.researchproject.controller;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import be.pxl.researchproject.service.AuthService;

@RestController
@RequestMapping(path = "/users")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, Object>> login(Authentication authentication) {
        LOGGER.info("User logged in: {}", authentication.getName());
        return new ResponseEntity<>(authService.createLogInfo(authentication), HttpStatus.OK);
    }
}
