package com.aps.tiktube.service;

import org.springframework.stereotype.Service;

import com.aps.tiktube.model.Access;
import com.aps.tiktube.model.Token;
import com.aps.tiktube.model.User;
import com.aps.tiktube.security.TokenManager;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class UserService {

    /**
     * Create the user if the information is valid
     * 
     * @param user
     * @return Error or Success
     */
    public String createUser(User user) {

        String message = "";
        Access<User> userAccess = new Access<>(User.class);

        boolean emailAlreadyUsed = !userAccess.where("Email", user.getEmail()).isEmpty();
        boolean userNameAlreadyUsed = !userAccess.where("UserName", user.getUserName()).isEmpty();

        userAccess.close();

        if (emailAlreadyUsed || userNameAlreadyUsed)
            message = "Email already registered or username already registered";
        if (user.getUserName() == null || user.getUserName().isEmpty())
            message = "Invalid username";
        if (user.getEmail() == null || user.getEmail().isEmpty())
            message = "Invalid email";
        if (user.getPassword() == null || user.getPassword().isEmpty())
            message = "Invalid password";

        if (!message.equals("")) {
            return message;
        }

        String encoded = DigestUtils.sha256Hex(user.getPassword());
        user.setPassword(encoded);

        user.save();

        return "SUCCESS";
    }

    public String getUser(String token) {
        Access<Token> access = new Access<>(Token.class);
        List<Token> tokens = access.where("TokenValue", token);
        access.close();

        if (tokens.isEmpty())
            return "Token not found";
        else {
            Token tokenObj = tokens.get(0);
            Access<User> userAccess = new Access<>(User.class);
            User users = userAccess.getById(tokenObj.getUserId());
            userAccess.close();
            if (users == null)
                return "User not found";
            return users.toDocument().toJson();
        }
    }


    /**
     * Get a user by its id
     * 
     * @param userId
     * @return User
     */
    public String getUserName(String userId) {
        Access<User> access = new Access<>(User.class);
        List<User> users = access.where("Id", userId);
        access.close();
        if (users.isEmpty())
            return "User not found";
        return users.get(0).getUserName();
    }

    /**
     * Delete a user
     * 
     * @param userId
     */
    public void deleteUserByUserId(String userId) {
        try {
            Access<User> access = new Access<>(User.class);
            access.deleteById(userId);
            access.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If a user is valid and creates a token associated with the user.
     * @param user
     * @return Error or Success
     */
    public String login(User user) {
        String message = "";

        String encoded = DigestUtils.sha256Hex(user.getPassword());
        Access<User> access = new Access<>(User.class);
        List<User> users = access.where(Arrays.asList("Email", "Password"),
                                Arrays.asList(user.getEmail(), encoded));

        if (!users.isEmpty()) {
            User loggedUser = users.get(0);

            TokenManager.deleteUserTokens(loggedUser.getId());

            Token token = new Token();
            token.setIsActive(true);
            token.setUserId(loggedUser.getId());
            token.setTokenValue(TokenManager.newToken());
            token.setLastTimeUsed(System.currentTimeMillis() / 1000L);
            token.save();

            message = "Logged in. Token: " + token.getId();

            access.close();

            return message;
        }
        access.close();

        return "Already have a user logged in";

    }

    /**
     * Logout a user
     * 
     * @param token
     * @return Error or Success
     */
    public String logout(String token) {
        if (TokenManager.verify(token)) {
            TokenManager.deleteToken(token);
            return "Logged out";
        }
        return "Token not found";
    }

}
