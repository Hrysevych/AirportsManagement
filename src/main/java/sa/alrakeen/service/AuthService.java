package sa.alrakeen.service;


import io.jsonwebtoken.Jwts;
import jakarta.inject.Singleton;
import sa.alrakeen.entity.User;

import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class AuthService {

//    private final SecretKey key = Jwts.SIG.HS256.key().build();

//    private final String USER = "USER";

    private final Map<User, SecretKey> keys = new HashMap<>();
//    private final List<String> roles = Collections.singletonList(USER);

    public String getToken(User user) {
        SecretKey key;
        if (keys.containsKey(user)) {
            key = keys.get(user);
        } else {
            key = Jwts.SIG.HS256.key().random(new SecureRandom()).build();
            keys.put(user, key);
        }
        return Jwts.builder()
                .subject(user.getName() + user.getEmail())
//                .claim("roles", roles)
                .signWith(key)
                .compact();
    }

    public boolean verifyToken(String jws, User user) {
        SecretKey key;
        if (keys.containsKey(user)) {
            key = keys.get(user);
        } else {
            return false;
        }
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(jws).getPayload().getSubject().equals(user.getName() + user.getEmail());
    }


    public void invalidateToken(User user) {
        if (keys.containsKey(user)) {
            try {
                Optional<SecretKey> secretKey = Optional.ofNullable(keys.remove(user));
                if (secretKey.isPresent()) {
                    secretKey.get().destroy();
                }
            } catch (DestroyFailedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
