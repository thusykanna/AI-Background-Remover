package thusySoftwareSolutions.BGremover.controller;

// import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import thusySoftwareSolutions.BGremover.dto.UserDTO;
import thusySoftwareSolutions.BGremover.response.RemoveBgResponse;
import thusySoftwareSolutions.BGremover.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Builder
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createOrUpdateUser(@RequestBody UserDTO userDTO, Authentication authentication) {
        RemoveBgResponse response = null;
        try {

            if (!authentication.getName().equals(userDTO.getClerkId())) {
                response = RemoveBgResponse.builder()
                    .success(false)
                    .data("User does not have permission to access the resource")
                    .statusCode(HttpStatus.FORBIDDEN)
                    .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            UserDTO user = userService.saveUser(userDTO);
            response =RemoveBgResponse.builder()
                .success(true)
                .statusCode(HttpStatus.OK)
                .data(user)
                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            response = RemoveBgResponse.builder()
                .success(false)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(exception.getMessage())
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
