package moheng.auth.domain.token;

public class MemberToken {
    private final String accessToken;
    private final String refreshToken;

    public MemberToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}