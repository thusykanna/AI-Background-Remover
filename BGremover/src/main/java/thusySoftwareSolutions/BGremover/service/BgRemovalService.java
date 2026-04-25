package thusySoftwareSolutions.BGremover.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import thusySoftwareSolutions.BGremover.entity.UserEntity;
import thusySoftwareSolutions.BGremover.repository.UserRepository;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BgRemovalService {

    private final UserRepository userRepository;

    @Value("${clipdrop.api-key}")
    private String clipdropApiKey;

    private static final String CLIPDROP_URL = "https://clipdrop-api.co/remove-background/v1";
    private static final MediaType MULTIPART_FORM_DATA = new MediaType("multipart", "form-data");

    public byte[] removeBackground(String clerkId, MultipartFile imageFile) throws IOException {
        UserEntity user = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getCredits() == null || user.getCredits() <= 0) {
            throw new InsufficientCreditsException("Insufficient credits. Please top up your account.");
        }

        byte[] resultBytes = callClipdrop(imageFile);

        user.setCredits(user.getCredits() - 1);
        userRepository.save(user);

        return resultBytes;
    }

    private byte[] callClipdrop(MultipartFile imageFile) throws IOException {
        ByteArrayResource imageResource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_file", imageResource);

        return RestClient.create()
                .post()
                .uri(CLIPDROP_URL)
                .header("x-api-key", clipdropApiKey)
                .contentType(Objects.requireNonNull(MULTIPART_FORM_DATA))
                .body(body)
                .retrieve()
                .body(byte[].class);
    }

    public static class InsufficientCreditsException extends RuntimeException {
        public InsufficientCreditsException(String message) {
            super(message);
        }
    }
}
