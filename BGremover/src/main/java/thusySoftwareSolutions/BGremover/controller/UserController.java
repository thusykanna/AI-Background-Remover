package thusySoftwareSolutions.BGremover.controller;

// import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import thusySoftwareSolutions.BGremover.dto.UserDTO;
import thusySoftwareSolutions.BGremover.response.RemoveBgResponse;
import thusySoftwareSolutions.BGremover.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        RemoveBgResponse response;
        try {
            UserDTO user = userService.getUserByClerkId(authentication.getName());
            response = RemoveBgResponse.builder()
                    .success(true)
                    .statusCode(HttpStatus.OK)
                    .data(user)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            response = RemoveBgResponse.builder()
                    .success(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/credits")
    public ResponseEntity<?> addCredits(
            @RequestParam String planId,
            Authentication authentication) {
        RemoveBgResponse response;
        try {
            int creditsToAdd = switch (planId) {
                case "Basic"   -> 100;
                case "Premium" -> 250;
                case "Ultimate"-> 1000;
                default -> throw new IllegalArgumentException("Unknown plan: " + planId);
            };
            UserDTO user = userService.addCredits(authentication.getName(), creditsToAdd);
            response = RemoveBgResponse.builder()
                    .success(true)
                    .statusCode(HttpStatus.OK)
                    .data(user)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response = RemoveBgResponse.builder()
                    .success(false)
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .data(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response = RemoveBgResponse.builder()
                    .success(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

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
