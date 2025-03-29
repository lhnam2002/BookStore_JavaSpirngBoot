package Tuan3.LuuHoangNam.services;

import Tuan3.LuuHoangNam.constants.Provider;
import Tuan3.LuuHoangNam.constants.Role;
import Tuan3.LuuHoangNam.entities.User;
import Tuan3.LuuHoangNam.repositories.IRoleRepository;
import Tuan3.LuuHoangNam.repositories.IUserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder()
                .encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<Optional<User>> findByUsername(String username) throws
            UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }


    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRole(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public void saveOauthUser(String email, @NotNull String username) {
        if(userRepository.findByUsername(username).isPresent())
            return;
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(Provider.GOOGLE.value);
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }
}
