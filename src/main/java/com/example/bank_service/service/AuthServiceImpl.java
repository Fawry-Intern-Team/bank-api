package com.example.bank_service.service;

import com.example.bank_service.component.CustomUserDetails;
import com.example.bank_service.dto.AuthRequest;
import com.example.bank_service.dto.AuthResponse;
import com.example.bank_service.dto.RegisterRequest;
import com.example.bank_service.dto.RegisterResponse;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Role;
import com.example.bank_service.entity.User;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.RoleRepository;
import com.example.bank_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            AuthResponse response = new AuthResponse(jwtService.generateToken((CustomUserDetails) userService.loadUserByUsername(request.getUsername())));
            return response;
        }
        throw new RuntimeException("invalid credentials");
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        Account account = Account.builder()
                .cardNumber(request.getCardNumber())
                .balance(BigDecimal.ZERO)
                .cardName(request.getCardName())
                .email(request.getEmail())
                .build();
        Account savedAccount = accountRepository.save(account);
        Role role = roleRepository.findByName("USER");
        User user = User.builder()
                .account(savedAccount)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(role))
                .build();
        userRepository.save(user);
        return RegisterResponse.builder()
                .cardName(savedAccount.getCardName())
                .cardNumber(savedAccount.getCardNumber())
                .username(user.getUsername())
                .email(savedAccount.getEmail())
                .build();
    }
}
