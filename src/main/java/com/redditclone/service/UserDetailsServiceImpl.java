package com.redditclone.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.redditclone.model.User;
import com.redditclone.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userOptional = userRepository.findByUsername(username);
		 User user = userOptional
	                .orElseThrow(() -> new UsernameNotFoundException("No user " +
	                        "Found with username : " + username));
		 
		 return new org.springframework.security
	                .core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, getAuthorities("USER"));
		
	}

	   private Collection<? extends GrantedAuthority> getAuthorities(String role) {
	        return Collections.singletonList(new SimpleGrantedAuthority(role));
	    }
}
