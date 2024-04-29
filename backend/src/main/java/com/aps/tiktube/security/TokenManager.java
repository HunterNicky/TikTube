package com.aps.tiktube.security;

import java.util.List;
import java.util.UUID;
import java.security.SecureRandom;
import java.util.Base64;

import com.aps.tiktube.model.Access;
import com.aps.tiktube.model.Token;
import com.aps.tiktube.model.User;
import com.aps.tiktube.model.video.Comments;
import com.aps.tiktube.model.video.Like;

public class TokenManager {
    private static final String TOKENVALUE = "TokenValue";

    private TokenManager() {
    }

    public static String newToken() {
        return generateNewToken();
    }

    public static boolean verify(String token) {
        Access<Token> access = new Access<>(Token.class);
        List<Token> tokens = access.where(TOKENVALUE, token);
        access.close();
        return !tokens.isEmpty();
    }

    public static User getUser(String token) {
        Access<Token> access = new Access<>(Token.class);
        Token tk = access.first(TOKENVALUE, token);
        access.close();

        Access<User> useraccess = new Access<>(User.class);
        User user = useraccess.getById(tk.getUserId());
        useraccess.close();

        return user;
    }

    public static void deleteUserTokens(String userId) {
        Access<Token> tokenAccess = new Access<>(Token.class);
        List<Token> userTokenList = tokenAccess.where("UserId", userId);
        for (Token token : userTokenList) {
            tokenAccess.deleteById(token.getId());
        }
    }

    public static void deleteToken(String tokenValue) {
        Access<Token> tokenAccess = new Access<>(Token.class);
        List<Token> tokenList = tokenAccess.where(TOKENVALUE, tokenValue);
        if (!tokenList.isEmpty())
            tokenAccess.deleteById(tokenList.get(0).getId());
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateNewToken() {
        String uuid = UUID.randomUUID().toString();

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String randomPart = base64Encoder.encodeToString(randomBytes);

        String token = uuid + randomPart;
        return "token_" + token;
    }

    public static void updateTokenLastTimeUsed(String tokenValue) {
        if (tokenValue.equals("unregistered"))
            return;
        Access<Token> tokenAccess = new Access<>(Token.class);
        Token token = tokenAccess.where(TOKENVALUE, tokenValue).get(0);
        token.setLastTimeUsed(System.currentTimeMillis() / 1000L);
        token.save();
        tokenAccess.close();
    }

    public static Token getTokenById(String tokenId) {
        Access<Token> tokenAccess = new Access<>(Token.class);
        Token token = tokenAccess.getById(tokenId);
        tokenAccess.close();
        return token;
    }

    public static Boolean verifyTokenAccess(String token, String id, String type) {

        String tokenUserId = TokenManager.getUser(token).getId();
        String userId = "";

        try {
            switch (type) {
                case "Views":
                    Access<com.aps.tiktube.model.video.Views> viewsAccess = new Access<>(
                            com.aps.tiktube.model.video.Views.class);
                    userId = viewsAccess.getById(id).getUserId();
                    viewsAccess.close();
                    break;
                case "Like":
                    Access<Like> likeAccess = new Access<>(
                            Like.class);
                    userId = likeAccess.getById(id).getUserId();
                    likeAccess.close();
                    break;
                case "Comment":
                    Access<Comments> commentAccess = new Access<>(
                            Comments.class);
                    userId = commentAccess.getById(id).getUserId();
                    commentAccess.close();
                    break;
                case "Video":
                    Access<com.aps.tiktube.model.video.Video> videoAccess = new Access<>(
                            com.aps.tiktube.model.video.Video.class);
                    userId = videoAccess.getById(id).getUserId();
                    videoAccess.close();
                    break;
                case "User":
                    userId = id;
                    break;
                default:
                    return false;
            }
        } catch (Exception e) {
            if (!e.getClass().equals(IllegalArgumentException.class))
                e.printStackTrace();
        }

        return tokenUserId.equals(userId);
    }

}
