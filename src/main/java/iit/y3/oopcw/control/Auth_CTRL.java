package iit.y3.oopcw.control;

import iit.y3.oopcw.aspects.EnableResultLogger;
import iit.y3.oopcw.jwt.JwtUtil;
import iit.y3.oopcw.jwt.model.AuthRequest;
import iit.y3.oopcw.jwt.model.AuthResponse;
import iit.y3.oopcw.model.User;
import iit.y3.oopcw.service.User_SRVC;
import iit.y3.oopcw.springsecurity.SpringSecurityUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class Auth_CTRL {
    private static final Logger logger = LoggerFactory.getLogger("perfLog");
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final SpringSecurityUserService userDetailsService;
    private final User_SRVC userSRVC;

    @Autowired
    public Auth_CTRL(AuthenticationManager authenticationManager
            , JwtUtil jwtTokenUtil
            , SpringSecurityUserService userDetailsService
            , PasswordEncoder passwordEncoder
            , User_SRVC userSRVC) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userSRVC = userSRVC;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @EnableResultLogger(value = "create-auth-token", description = "end-point")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthRequest authenticationRequest
            , HttpServletRequest absoluteRequest) {
        logger.info("Calling Authentication api. Username: {}, URL: {}"
                , authenticationRequest.getUsername()
                , absoluteRequest.getRequestURL());
        UserDetails userDetails = null;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername()
                    , authenticationRequest.getPassword()));
            userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            /* login attempts reset */
            userDetailsService.onAuthenticationSuccess(userDetails);
        } catch (BadCredentialsException badCredentialsException) {
            /* limit login attempts */
            userDetailsService.onAuthenticationFailure(authenticationRequest.getUsername());
        }
        try {
            assert userDetails != null;
            final String jwt = jwtTokenUtil.generateToken(userDetails);
            String sequence = userSRVC.getNextSequenceValue();
            String role = userSRVC.getUserRole_byUserName(userDetails.getUsername());
            Optional<User> logged_in_userData = userSRVC.findByUsername(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(jwt, sequence, role, logged_in_userData.get().getFirstName(), logged_in_userData.get().getLastName()));
        } catch (NullPointerException nullPointerException) {
            throw new BadCredentialsException("Your username and password do not match. Please try again !");
        }
    }
}