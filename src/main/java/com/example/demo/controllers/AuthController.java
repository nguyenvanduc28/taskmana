package com.example.demo.controllers;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.auth.*;
import com.example.demo.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("login")
    public ResponseEntity<ResponseObject> login(
            @RequestBody @Valid AuthLoginDto authDto
            ) {

        AuthResponse authResponse = authService.authenticate(authDto);

        return ResponseEntity.ok(ResponseObject.builder()
                                .data(authResponse)
                                .message("login successfully")
                                .responseCode(HttpStatus.OK.value())
                                .build());
    }

    @PostMapping("register")
    public ResponseEntity<ResponseObject> register(
            @RequestBody @Valid AuthDto authDto
    ) {
        if (authService.checkUserExits(authDto))
            return ResponseEntity.ok(ResponseObject.builder()
                    .data("User already exists")
                    .message("User already exists")
                    .responseCode(400)
                    .build());
        AuthResponse authResponse = authService.register(authDto);

        return ResponseEntity.ok(ResponseObject.builder()
                        .data(authResponse)
                        .message("register successfully")
                        .responseCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("verify-token")
    public ResponseEntity<ResponseObject> verifyToken(
            @RequestBody @Valid VerifyTokenRequest request
    ) {
        UserInfoDto userInfoDto = authService.verifyToken(request);
        return ResponseEntity.ok(ResponseObject.builder()
                                    .responseCode(200)
                                    .data(userInfoDto)
                                    .message("token is valid")
                                    .build());
    }

    @PostMapping("update")
    public ResponseEntity<ResponseObject> updateProfile(
            @RequestBody @Valid UserInfoDto userInfoDto,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        String email = userDetails.getUsername();
        UserInfoDto userInfoDto1 = authService.updateProfile(userInfoDto, email);

        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .data(userInfoDto1)
                .message("success")
                .build());
    }

    @PostMapping("change-password")
    public ResponseEntity<ResponseObject> updateProfile(
            @RequestBody @Valid ChangePasswordDto changePasswordDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        authService.changePassword(changePasswordDto, email);

        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .data("")
                .message("success")
                .build());
    }
}
