package thusySoftwareSolutions.BGremover.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import thusySoftwareSolutions.BGremover.response.RemoveBgResponse;
import thusySoftwareSolutions.BGremover.service.BgRemovalService;
import thusySoftwareSolutions.BGremover.service.BgRemovalService.InsufficientCreditsException;

import java.util.Base64;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final BgRemovalService bgRemovalService;

    @PostMapping("/remove-bg")
    public ResponseEntity<?> removeBg(
            @RequestParam("image") MultipartFile imageFile,
            Authentication authentication) {

        RemoveBgResponse response;
        try {
            byte[] resultBytes = bgRemovalService.removeBackground(
                    authentication.getName(), imageFile);

            String base64Result = Base64.getEncoder().encodeToString(resultBytes);
            response = RemoveBgResponse.builder()
                    .success(true)
                    .statusCode(HttpStatus.OK)
                    .data(base64Result)
                    .build();
            return ResponseEntity.ok(response);

        } catch (InsufficientCreditsException e) {
            response = RemoveBgResponse.builder()
                    .success(false)
                    .statusCode(HttpStatus.PAYMENT_REQUIRED)
                    .data(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);

        } catch (Exception e) {
            response = RemoveBgResponse.builder()
                    .success(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
