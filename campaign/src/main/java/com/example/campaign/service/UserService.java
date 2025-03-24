package com.example.campaign.service;


import com.example.campaign.exception.AuthenticationException;
import com.example.campaign.model.Campaign;
import com.example.campaign.model.EmeraldWallet;
import com.example.campaign.model.User;
import com.example.campaign.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    //private final CampaignService campaignService;
    private final EmeraldWalletService emeraldWalletService;

    public User getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null && authentication.getPrincipal() instanceof UserDetails)) {
            return (User) authentication.getPrincipal();
        }
        throw new AuthenticationException("User not authenticated");
    }

    public EmeraldWallet depositEmeraldsIntoUserWallet(BigDecimal amount) {
        User user = this.getUserFromSecurityContext();
        return this.emeraldWalletService.depositEmeralds(user.getEmeraldWallet(), amount);
    }

    @Transactional
    public void deleteUserAccount() { // Relying upon circular references is discouraged
//        User user = this.getUserFromSecurityContext();
//        List<Campaign> campaigns = campaignService.getAllUserCampaigns();
//        for(Campaign camp : campaigns) {
//            campaignService.deleteCampaign(camp.getId());
//        }
//        emeraldWalletService.deleteEmeraldWalletRecords(user.getEmeraldWallet());
//        userRepository.delete(user);
    }

}