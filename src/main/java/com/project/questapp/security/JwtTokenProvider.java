package com.project.questapp.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${questapp.app.secret}") // this syntax used for get information from application.properties
    private String APP_SECRET; // we're going to use that secret while creating key

    @Value("${questapp.expires.in}")
    private long EXPIRES_IN;    // expire date

    public String generateJwtToken(Authentication auth){
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal(); // the user that we'are gonna authenticate.
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN); //when the time expires
        return Jwts.builder().setSubject(Long.toString(userDetails.getId())).setIssuedAt(new Date()).setExpiration(expireDate).signWith(SignatureAlgorithm.HS512,APP_SECRET ).compact();
                // setSubject --> user // in this scenario userId has been used but you can use anything you want.
        //setIssuedAt --> what time the key created
        // setexpiration --> expire date
        // signWith --> algorithm and secret key
        // compact --> create
        // signature algorithm has been giving error and error that says signature keys has at least 48 bits or digit I am not completely recognize. I quickly ran into application properties and add some char character of
        // our app_secret. If you want you can change the signature algorithm
    }
    public String generateJwtTokenByUserName(Long userId) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN); //when the time expires
        return Jwts.builder().setSubject(Long.toString(userId)).setIssuedAt(new Date())
                .setExpiration(expireDate).signWith(SignatureAlgorithm.HS512,APP_SECRET ).compact();

    }

    Long getUserIdFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        // we are saying i am using that key and take the key give me id
        return Long.parseLong(claims.getSubject());
    }

    boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token); // if we get successfully parse it then that means we are created.
            return !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
        // it's just like named if expiration before than the new Date so that means is expired.
    }



}
