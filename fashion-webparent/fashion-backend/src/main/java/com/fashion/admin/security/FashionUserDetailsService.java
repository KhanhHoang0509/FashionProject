package com.fashion.admin.security;


import com.fashion.admin.user.UserRepository;
import com.fashion.fashioncommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class FashionUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override//implements Interface UserDetailsService phải @Override lại phương thức loadUserByUsername, đây là phương thức kiểm tra email và password
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);
		if (user != null) {
			return new FashionUserDetails(user);
		}

		throw new UsernameNotFoundException("Could not find user with email: " + email);
	}

}
