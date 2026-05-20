package ru.otus.hw.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.exceptions.LoginAlreadyExistsException;
import ru.otus.hw.exceptions.RoleUserNotExistsInDBException;
import ru.otus.hw.models.AppUser;
import ru.otus.hw.models.Role;
import ru.otus.hw.repositories.RoleRepository;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public String save(UserDto userDto) {
        String login = userDto.getUsername();
        if (userRepository.existsByUsername(login)) {
            throw new LoginAlreadyExistsException(login);
        }
        Role userRole = roleRepository.findRoleByName("USER")
            .orElseThrow(RoleUserNotExistsInDBException::new);

        String password = passwordEncoder.encode(userDto.getPassword());
        var user = new AppUser(userDto.getId(), login, password, List.of(userRole));
        AppUser saveUser = userRepository.save(user);
        return saveUser.getUsername();
    }
}
