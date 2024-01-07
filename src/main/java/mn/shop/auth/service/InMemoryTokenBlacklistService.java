package mn.shop.auth.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

    private Set<String> tokenBlacklist = new HashSet<>();

    @Override
    public void addToBlacklist(String token) {
        tokenBlacklist.add(token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
