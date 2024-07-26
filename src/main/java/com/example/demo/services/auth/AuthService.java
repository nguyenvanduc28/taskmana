package com.example.demo.services.auth;

import com.example.demo.dto.auth.*;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnAuthorizedException;
import com.example.demo.models.UserEntity;
import com.example.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;
    private ModelMapper modelMapper = new ModelMapper();
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(AuthLoginDto authDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDto.getEmail(),
                            authDto.getPassword()
                    )
            );
            String token = jwtService.generateToken(authentication.getName());
            UserEntity userEntity = userRepository.findByEmail(authDto.getEmail());
            if (userEntity == null) throw new NotFoundException("Profile not found");
            ModelMapper modelMapper = new ModelMapper();
            return AuthResponse.builder()
                    .userInfoDto(modelMapper.map(userEntity, UserInfoDto.class))
                    .token(token)
                    .build();
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    public AuthResponse register(AuthDto authDto) {
        UserEntity userCheck = userRepository.findByEmail(authDto.getEmail());
        if (userCheck != null) throw new RuntimeException("User da ton tai");

        UserEntity userEntity = new UserEntity();
        userEntity.setName(authDto.getName());
        userEntity.setEmail(authDto.getEmail());
        userEntity.setDob(authDto.getDob());
        userEntity.setPassword(passwordEncoder.encode(authDto.getPassword()));
        userRepository.save(userEntity);
        var jwtToken = jwtService.generateToken(userEntity.getEmail());
        ModelMapper modelMapper = new ModelMapper();
        return AuthResponse.builder()
                .userInfoDto(modelMapper.map(userEntity, UserInfoDto.class))
                .token(jwtToken)
                .build();
    }
    public boolean checkUserExits(AuthDto authDto) {
        UserEntity userCheck = userRepository.findByEmail(authDto.getEmail());
        if (userCheck != null) return true;
        return false;
    }

    public UserInfoDto verifyToken(VerifyTokenRequest request) {
        String email = jwtService.extractUsername(request.getToken());
        if (!jwtService.isTokenValid(request.getToken(), email)) {
            throw new UnAuthorizedException();
        }

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw  new NotFoundException("profile not found");

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userEntity, UserInfoDto.class);
    }

    public UserInfoDto updateProfile(UserInfoDto userInfoDto, String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        user.setName(userInfoDto.getName());
        user.setDob(userInfoDto.getDob());

        UserEntity userEntity = userRepository.save(user);
        return modelMapper.map(userEntity, UserInfoDto.class);
    }
    public boolean changePassword(ChangePasswordDto changePasswordDto, String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        changePasswordDto.getCurrentPassword()
                )
        );

        if (!authentication.isAuthenticated()) throw new RuntimeException("Password incorrect");;

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword()))
            throw new RuntimeException("Confirmation password does not match");

        user.setPassword(passwordEncoder.encode(changePasswordDto.getConfirmPassword()));
        UserEntity userEntity = userRepository.save(user);
        return true;
    }
}
