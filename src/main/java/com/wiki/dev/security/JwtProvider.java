package com.wiki.dev.security;

import com.wiki.dev.exception.DevWikiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jks.password}")
    private String keyStorePassword;

    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/dev-wiki.jks");
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
            jwtParser = Jwts.parserBuilder().setSigningKey(getPublicKey()).build();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new DevWikiException("Exception occurred while loading keystore : " + e);
        }
    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey()).compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("dev-wiki", keyStorePassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new DevWikiException("Exception occurred while retrieving public key from keystore : " + e);
        }
    }

    public boolean validateToken(String jwt) {
        jwtParser.parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("dev-wiki").getPublicKey();
        } catch (KeyStoreException e) {
            throw new DevWikiException("Exception occurred while retrieving public key from keyStore : " + e);
        }
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
