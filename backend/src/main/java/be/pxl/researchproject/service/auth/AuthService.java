package be.pxl.researchproject.service.auth;

import java.util.Map;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Map<String, Object> createLogInfo(Authentication authentication);
}