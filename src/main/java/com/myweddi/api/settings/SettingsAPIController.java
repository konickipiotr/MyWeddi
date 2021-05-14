package com.myweddi.api.settings;

import com.myweddi.user.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsAPIController {

    private SettingsService settingsService;
    private RemoveAccountService removeAccountService;

    @Autowired
    public SettingsAPIController(SettingsService settingsService, RemoveAccountService removeAccountService) {
        this.settingsService = settingsService;
        this.removeAccountService = removeAccountService;
    }

    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody Map<String, String> body, Principal principal){
        String newPassword = body.get("newPassword");
        return settingsService.changePassword(newPassword, principal.getName());
    }

    @PostMapping("/updateguest")
    public ResponseEntity<Void> updateGuest(@RequestBody Guest guest){
        return settingsService.updateGuest(guest);
    }

    @PostMapping("/removeguest")
    public ResponseEntity<Void> removeGuestAccount(@RequestBody Map<String, Long> body, Principal principal){
        Long userid = body.get("userid");
        return this.removeAccountService.removeGuestAccount(userid, principal.getName());
    }

    @PostMapping("/removewedding")
    public ResponseEntity<Void> removeHostWeddingAccount(@RequestBody Map<String, Long> body, Principal principal){
        Long userid = body.get("userid");
        Long weddingid = body.get("weddingid");
        return this.removeAccountService.removeHostWeddingAccount(userid, weddingid, principal.getName());
    }
}
