package moheng.auth.domain;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import moheng.auth.exception.InvalidTokenException;
import moheng.auth.exception.NoExistMemberTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecretKey secretKey;
    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;

    public JwtTokenProvider(
            final RefreshTokenRepository refreshTokenRepository,
            @Value("${security.jwt.token.secret_key}") final String secretKey,
            @Value("${security.jwt.token.expire_length.access_token}") final long accessTokenValidityInSeconds,
            @Value("${security.jwt.token.expire_length.refresh_token}") final long refreshTokenValidityInSeconds) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
    }

    public MemberToken createMemberToken(final long memberId) {
        String accessToken = createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);
        return new MemberToken(accessToken, refreshToken);
    }

    public String createToken(final String payload, final long tokenValidityInSeconds) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(final long memberId) {
        return createToken(String.valueOf(memberId), accessTokenValidityInSeconds);
    }

    public String createRefreshToken(final long memberId) {
        if(!refreshTokenRepository.existsById(memberId)) {
            String newRefreshToken = createToken(String.valueOf(memberId), refreshTokenValidityInSeconds);
            refreshTokenRepository.save(memberId, newRefreshToken);
            return newRefreshToken;
        }
        return createToken(String.valueOf(memberId), refreshTokenValidityInSeconds);
    }

    public String getPayload(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("변조되었거나 만료된 토큰 입니다.");
        }
    }

    public String generateRenewalAccessToken(String refreshToken) {
        validateToken(refreshToken);
        Long memberId = Long.valueOf(getMemberId(refreshToken));

        if(!refreshTokenRepository.existsById(memberId)) {
            throw new NoExistMemberTokenException("존재하지 않는 유저의 토큰입니다.");
        }

        return createAccessToken(memberId);
    }

    public String getMemberId(String token) {
        validateToken(token);

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}


