package com.project.questapp.services;

import com.project.questapp.entities.RefreshToken;
import com.project.questapp.entities.User;
import com.project.questapp.repos.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID; // generates random unique ids

@Service
public class RefreshTokenService {

    @Value("${refresh.token.expires.in}") // added to the application properties file
    Long expireSeconds;

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    public boolean isRefreshExpired(RefreshToken token){ // is expired --> true
        return token.getExpiryDate().before(new Date());
    }
    public String createRefreshToken(User user){
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if(token == null){
            token = new RefreshToken();
            token.setUser(user);
        }
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public RefreshToken getByUser(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}
