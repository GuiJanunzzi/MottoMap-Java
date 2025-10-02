package br.com.fiap.mottomap.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import br.com.fiap.mottomap.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    // Gera um novo token JWT para o usuário
    public String generateToken(Usuario usuario) {
        long expirationMillis = System.currentTimeMillis() + 3600000; // Expira em 1 hora
        Date expirationDate = new Date(expirationMillis);

        return Jwts.builder()
                .setSubject(usuario.getEmail()) // Identificador do usuário (email)
                .setIssuer("MottoMap") // Quem emitiu o token
                .setIssuedAt(new Date()) // Data de emissão
                .setExpiration(expirationDate) // Data de expiração
                .signWith(getSigninKey()) // Assina com a chave secreta
                .compact();
    }

    // Valida o token e retorna o "claim" (o email do usuário)
    public String getValidatedClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método auxiliar para gerar a chave de assinatura
    private SecretKey getSigninKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}