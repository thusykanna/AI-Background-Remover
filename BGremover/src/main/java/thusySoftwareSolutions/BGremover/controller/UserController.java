package thusySoftwareSolutions.BGremover.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
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
    public RemoveBgResponse createOrUpdateUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userService.saveUser(userDTO);
        try {
            return RemoveBgResponse.builder()
                .success(true)
                .statusCode(HttpStatus.CREATED)
                .data(user)
                .build();
        } catch (Exception exception) {
            return RemoveBgResponse.builder()
                .success(false)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(exception.getMessage())
                .build();
        }
    }
}
