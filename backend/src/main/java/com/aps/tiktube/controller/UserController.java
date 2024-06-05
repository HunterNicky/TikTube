package com.aps.tiktube.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aps.tiktube.model.Token;
import com.aps.tiktube.model.User;
import com.aps.tiktube.security.TokenManager;
import com.aps.tiktube.service.UserService;

@RestController
public class UserController {
    static final String ACCESCONTROLALLOW = "Access-Control-Allow-Origin";

    @GetMapping("/getuser/{token}")
    public String getMethodName(@PathVariable("token") String token) {
        try {
            UserService userService = new UserService();
            return userService.getUser(token);
        } catch (Exception e) {
            e.printStackTrace();
            return "User not found.";
        }
    }

    @GetMapping("/getusername/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") String id) {
        try {
            String userName = new UserService().getUserName(id);
            if (userName.isEmpty()) {
                return ResponseEntity.badRequest()
                        .header(ACCESCONTROLALLOW)
                        .body("User not found.");
            }
            return ResponseEntity.ok()
                    .header(ACCESCONTROLALLOW)
                    .body(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .header(ACCESCONTROLALLOW)
                    .body("_ERROR_15");
        }
    }

    @PostMapping("/createaccount")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        String message;
        try {
            message = new UserService().createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .header(ACCESCONTROLALLOW)
                    .body("Error creating account");
        }

        if (!message.equals("SUCCESS"))
            return ResponseEntity.badRequest()
                    .header(ACCESCONTROLALLOW)
                    .body(message);

        return ResponseEntity.ok()
                .header(ACCESCONTROLALLOW)
                .body(message);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        try {
            UserService userService = new UserService();
            String message = userService.login(user);
            if (!message.contains("Logged in.")) {
                return ResponseEntity.badRequest()
                        .header(ACCESCONTROLALLOW)
                        .body(message);
            }
            Token token = TokenManager.getTokenById(message.substring(message.indexOf("Token: ") + 7));
            return ResponseEntity.ok()
                    .header(ACCESCONTROLALLOW)
                    .body(token.getTokenValue());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .header(ACCESCONTROLALLOW)
                    .body("_ERROR_15");
        }

    }

}
