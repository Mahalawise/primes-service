package edu.iu.mamawise.primesservice.service;

import edu.iu.mamawise.primesservice.model.Customer;
import edu.iu.mamawise.primesservice.repository.AuthenticationDBRepository;
import edu.iu.mamawise.primesservice.repository.AuthenticationFileRepository;
import edu.iu.mamawise.primesservice.repository.IAuthenticationRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@Service("authenticationService")
public abstract class AuthenticationService implements IAuthenticationService, UserDetailsService {

	private IAuthenticationRepository authenticationRepository;

	public AuthenticationService(IAuthenticationRepository authenticationRepository) {
		this.authenticationRepository = authenticationRepository;
	}

	@Override
	public boolean register(Customer customer) throws IOException {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		String passwordEncoded = bc.encode(customer.getPassword());
		customer.setPassword(passwordEncoded);
		return authenticationRepository.save(customer);
	}
 @RestController
    @CrossOrigin({"http://127.0.0.1:5500"})
    public class AuthenticationController {
        private final IAuthenticationService authenticationService;
        private final AuthenticationManager authenticationManager;
        private TokenService tokenService;
        public AuthenticationController(AuthenticationManager authenticationManager, IAuthenticationService authenticationService, TokenService tokenService){
            this.authenticationManager = authenticationManager;
            this.authenticationService = authenticationService;
        }

        @PostMapping("/register")
        public boolean register(@RequestBody Customer customer){
            try{
                return authenticationService.register(customer);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        @PostMapping("/login")
        public String login(@RequestBody Customer customer){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword()));
            return tokenService.generateToken(authentication);
        }
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Customer customer = authenticationRepository.findByUsername(username);
			if (customer == null) {
				throw new UsernameNotFoundException("");
			}
			return User.withUsername(username).password(customer.getPassword()).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}