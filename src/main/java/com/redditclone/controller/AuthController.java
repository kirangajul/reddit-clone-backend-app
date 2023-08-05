package com.redditclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redditclone.dto.AuthenticationResponse;
import com.redditclone.dto.LoginRequest;
import com.redditclone.dto.RefreshTokenRequest;
import com.redditclone.dto.RegisterRequest;
import com.redditclone.service.AuthService;
import com.redditclone.service.RefreshTokenService;

import lombok.AllArgsConstructor;

@RequestMapping("/api/auth")
@RestController
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	
	 @PostMapping("/signup")
	    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
	        authService.signup(registerRequest);
	        return new ResponseEntity<String>("User Registration Successful", HttpStatus.OK);
	    }
	 
	  @GetMapping("accountVerification/{token}")
	    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
	        authService.verifyAccount(token);
	        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
	    }
	  
	  @PostMapping("/login")
	    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
	        return authService.login(loginRequest);
	    }
	  
	  @PostMapping("/logout")
	    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
	        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
	        return new ResponseEntity<String>("Refresh Token Deleted Successfully!!", HttpStatus.OK);
	    }
	  
	  @PostMapping("/refresh/token")
	    public AuthenticationResponse refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest) {
	        return authService.refreshToken(refreshTokenRequest);
	    }
}
