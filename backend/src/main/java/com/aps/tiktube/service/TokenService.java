package com.aps.tiktube.service;

import java.util.List;

import org.bson.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.aps.tiktube.model.Access;
import com.aps.tiktube.model.Token;
import com.aps.tiktube.security.TokenManager;

@Service
@CrossOrigin(origins = { "*" })
public class TokenService {
    private TokenService() {
    }

    /**
     * 
     */
    @Scheduled(fixedRate = 600000)
    public static void deleteUnusedTokens() {
        Access<Token> tokenAccess = new Access<>(Token.class);
        List<Document> tokenList = tokenAccess.getCollectionAsList("Token");
        if (!tokenList.isEmpty()) {
            try {
                tokenList.forEach(tokenDoc -> {
                    Token token = new Token();
                    token.setFromDocument(tokenDoc);
                    Long lastTimeUsed = tokenDoc.get("LastTimeUsed") != null
                            ? Long.parseLong(tokenDoc.get("LastTimeUsed").toString())
                            : 0L;
                    Long crTime = System.currentTimeMillis() / 1000L;

                    if (crTime - lastTimeUsed >= 600)
                        TokenManager.deleteToken(token.getTokenValue());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tokenAccess.close();
    }
}
