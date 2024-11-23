package iit.y3.oopcw.springsecurity;

import iit.y3.oopcw.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.net.ssl.SSLSession;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SpringSecurityUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 2970590651296665733L;
    private final User user;

    public SpringSecurityUserDetails(final User user) {
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<SimpleGrantedAuthority>();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !LocalDate.now().equals(this.user.getExpiryDate()) && !LocalDate.now().isAfter(this.user.getExpiryDate());
    }

    @Override
    public boolean isEnabled() {
        return this.user.isAccountNonLocked();
    }

    public User getUser() {
        return this.user;
    }
}