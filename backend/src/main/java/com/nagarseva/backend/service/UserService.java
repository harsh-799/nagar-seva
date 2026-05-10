package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.*;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.repository.WardRepository;
import com.nagarseva.backend.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private WardRepository wardRepository;

    private Ward getWardOrThrow(int wardId) {
        return wardRepository.findById(wardId).orElseThrow(
                () -> new InvalidWardException("No Ward Exists by this Id")
        );
    }

    public PasswordUpdationResponse updateUserPassword(PasswordUpdationRequest passwordUpdationRequest) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User updatedUser = userRepository.findByEmail(user.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found")
        );

        if (!user.isDefaultPassword()) {
            if (passwordUpdationRequest.getOldPassword() == null || passwordUpdationRequest.getOldPassword().isEmpty())
                throw new InvalidPasswordException("Kindly Enter the old password");

            boolean passwordMatches = passwordEncoder.matches(passwordUpdationRequest.getOldPassword(), updatedUser.getPassword());

            if (!passwordMatches)
                throw new InvalidPasswordException("Entered password does not match the current password. Please try again.");
        }

        updatedUser.setPassword(passwordEncoder.encode(passwordUpdationRequest.getNewPassword()));
        updatedUser.setDefaultPassword(false);

        userRepository.save(updatedUser);

        PasswordUpdationResponse response = new PasswordUpdationResponse();
        response.setSuccess(true);
        response.setMessage("Password Updated Successfully");

        return response;
    }

    public RegisterUserResponse addNewUser(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new UserAlreadyExistsException("Username Already Taken.");
        }

        if (registerUserRequest.getRole().equals(Role.CITIZEN)) {
            throw new InvalidUserCreationException("Action not allowed: Admins are not permitted to create Citizen accounts");
        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setFullName(registerUserRequest.getFullName());
        user.setRole(registerUserRequest.getRole());
        user.setDefaultPassword(true);
        user.setActive(true);

        if (registerUserRequest.getRole().equals(Role.OFFICER) && registerUserRequest.getDepartment() == null)
            throw new MissingDepartmentException("Department must be provided while creating a OFFICER.");

        if (registerUserRequest.getRole().equals(Role.OFFICER)) {
            user.setDepartment(registerUserRequest.getDepartment());
        }

        User savedUser = userRepository.save(user);

        RegisterUserResponse response = new RegisterUserResponse();
        response.setSuccess(true);
        response.setEmail(savedUser.getEmail());
        response.setMessage("Account Created Successfully.");
        response.setRole(registerUserRequest.getRole());

        if (!passwordEncoder.matches(registerUserRequest.getPassword(), savedUser.getPassword())) {
            throw new InvalidPasswordException("Password mismatch with Stored one");
        }

        emailService.sendOfficialRegistrationSuccessEmail(savedUser.getFullName(), savedUser.getEmail(), savedUser.getRole(), registerUserRequest.getPassword());

        return response;
    }

    @Transactional
    public PasswordUpdationResponse resetUserPassword(PasswordResetRequest passwordResetRequest) {
        User user = userRepository.findByEmail(passwordResetRequest.getEmail()).orElseThrow(
                () -> new UserNotFoundException("No User Found with this email")
        );

        LocalDateTime current = LocalDateTime.now();

        if (user.getResetOtpVerifiedUntil() == null) {
            throw new OTPNotGeneratedException("No OTP has been generated. Please generate one first.");
        }

        if (current.isAfter(user.getResetOtpVerifiedUntil())) {
            throw new ResetPasswordWindowExceededException("Password reset window exceeded. Please generate a new OTP");
        }

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));

        user.setResetOtp(null);
        user.setResetOtpExpiry(null);
        user.setResetOtpVerifiedUntil(null);

        User updatedUser = userRepository.save(user);

        PasswordUpdationResponse response = new PasswordUpdationResponse();
        response.setSuccess(true);
        response.setMessage("Password changed successfully");

        emailService.sendPasswordChangedConfirmationEmail(updatedUser.getFullName(), updatedUser.getEmail(), current);
        return response;
    }
}
