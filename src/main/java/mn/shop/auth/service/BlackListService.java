package mn.shop.auth.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class BlackListService {

    private final JwtUtils jwtUtils;

    private Set<String> tokenBlacklist = new HashSet<>();

    public BlackListService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public void removeExpiredTokens() {

        Iterator<String> iterator = tokenBlacklist.iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            if (jwtUtils.isTokenExpired(token)) {
                iterator.remove();
            }
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void scheduleTokenCleanup() {
        removeExpiredTokens();
    }

}
