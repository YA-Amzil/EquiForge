package be.pxl.researchproject.security;

import java.security.*;
import java.security.interfaces.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.*;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import be.pxl.researchproject.domain.Role;

@Configuration
public class SecurityConfig {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;
    private static final String[] PATH_STRINGS = {
            "/users/**",
            "/users/login",
            "/foals/**",
            "/horses/**",
            "/h2-console/**",
            "/api/**",
            "/password/**",
            "/notifications/**"
    };

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    public SecurityConfig(CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint,
            CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint,
            CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler)
            throws NoSuchAlgorithmException {
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, PATH_STRINGS[0]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, PATH_STRINGS[1]).permitAll()
                        .requestMatchers(HttpMethod.POST, PATH_STRINGS[0]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, PATH_STRINGS[0]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, PATH_STRINGS[0]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, PATH_STRINGS[6]).permitAll()
                        .requestMatchers(HttpMethod.GET, PATH_STRINGS[6]).permitAll()

                        .requestMatchers(HttpMethod.GET, PATH_STRINGS[7]).permitAll()
                        .requestMatchers(HttpMethod.POST, PATH_STRINGS[7]).permitAll()
                        .requestMatchers(HttpMethod.PUT, PATH_STRINGS[7]).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PATH_STRINGS[7]).permitAll()

                        .requestMatchers(HttpMethod.GET, PATH_STRINGS[2]).permitAll()
                        .requestMatchers(HttpMethod.POST, PATH_STRINGS[2]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, PATH_STRINGS[2]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, PATH_STRINGS[2]).hasAuthority(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, PATH_STRINGS[3]).permitAll()
                        .requestMatchers(HttpMethod.POST, PATH_STRINGS[3]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, PATH_STRINGS[3]).hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, PATH_STRINGS[3]).hasAuthority(Role.ADMIN.name())

                        .requestMatchers(AntPathRequestMatcher.antMatcher(PATH_STRINGS[4])).permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(crsf -> crsf.disable())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher(PATH_STRINGS[5])))
                .cors(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(this.customBearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
