package com.anish.book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	MyUserRepository reposi;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> user = reposi.findByUsername(username);
		if(user.isPresent()) {
			return User.builder().username(user.get().getUsername())
					.password(user.get().getPassword())
					.roles(getRoles(user.get()))
					.build();
			
		}else {
			 throw new UsernameNotFoundException(username);
		}
		
	}

	private String[] getRoles(MyUser myUser) {
		if(myUser.getRole()==null) {
			return new 	String[] {"USER"};
		}else {
			return myUser.getRole().split(",");
		}
		
	}
	

}
