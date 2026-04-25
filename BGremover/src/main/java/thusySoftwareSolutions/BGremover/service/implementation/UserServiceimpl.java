package thusySoftwareSolutions.BGremover.service.implementation;

import java.util.Optional;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import thusySoftwareSolutions.BGremover.dto.UserDTO;
import thusySoftwareSolutions.BGremover.entity.UserEntity;
import thusySoftwareSolutions.BGremover.repository.UserRepository;
import thusySoftwareSolutions.BGremover.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        Optional<UserEntity> optionalUser = userRepository.findByClerkId(userDTO.getClerkId());
        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setPhotoUrl(userDTO.getPhotoUrl());
            if (userDTO.getCredits() != null) {
                existingUser.setCredits(userDTO.getCredits());
            }
            existingUser = userRepository.save(existingUser);
            return mapToDTO(existingUser);
        }
        UserEntity newUser = mapToEntity(userDTO);
        userRepository.save(newUser);
        return mapToDTO(newUser);
    }

    private UserDTO mapToDTO(UserEntity newUser) {
        return UserDTO.builder()
                .clerkId(newUser.getClerkId())
                .credits(newUser.getCredits())
                .email(newUser.getEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .photoUrl(newUser.getPhotoUrl())
                .build();
    }

    @Override
    public UserDTO addCredits(String clerkId, int creditsToAdd) {
        UserEntity user = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setCredits((user.getCredits() == null ? 0 : user.getCredits()) + creditsToAdd);
        userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    public UserDTO getUserByClerkId(String clerkId) {
        UserEntity user = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    private UserEntity mapToEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .clerkId(userDTO.getClerkId())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .photoUrl(userDTO.getPhotoUrl())
                .build();
    }

}
