package mn.shop.auth.service;

public interface TokenBlacklistService {
    void addToBlacklist(String token);

    boolean isTokenBlacklisted(String token);

}
