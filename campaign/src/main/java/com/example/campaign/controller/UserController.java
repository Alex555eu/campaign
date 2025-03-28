package com.example.campaign.controller;


import com.example.campaign.model.EmeraldWallet;
import com.example.campaign.model.User;
import com.example.campaign.service.EmeraldWalletService;
import com.example.campaign.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@AllArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "")
    public ResponseEntity<String> getUserSelf() throws JsonProcessingException {
        User user = userService.getUserFromSecurityContext();
        if (user != null) {
            String body = objectMapper.writeValueAsString(user);
            return ResponseEntity.ok().body(body);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/recharge-emeralds")
    public ResponseEntity<EmeraldWallet> depositEmeraldsIntoUserWallet(@RequestParam BigDecimal amount) {
        EmeraldWallet wallet = this.userService.depositEmeraldsIntoUserWallet(amount);
        return ResponseEntity.ok(wallet);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUserAccount() {
        userService.deleteUserAccount();
        return ResponseEntity.ok().build();
    }

}