package com.example.sampleauth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // allow all users to access the home pages and the static images directory
                .mvcMatchers("*").permitAll()
                .antMatchers("/callback**", "/login", "/swagger-ui/**", "/v3/api-docs/**", "/test1", "/actuator/**").permitAll()
                // all other requests must be authenticated
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .decoder(jwtDecoder())
        ;

        return http.build();
    }


    JwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator("https://dev-uy72e6f5.us.auth0.com/api/v2/");
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer("https://dev-uy72e6f5.us.auth0.com/");
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation("https://dev-uy72e6f5.us.auth0.com/");
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }


}


class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String audience;

    AudienceValidator(String audience) {
        Assert.hasText(audience, "audience is null or empty");
        this.audience = audience;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        List<String> audiences = jwt.getAudience();
        if (audiences.contains(this.audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error err = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN);
        return OAuth2TokenValidatorResult.failure(err);
    }
}

