package thusySoftwareSolutions.BGremover.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveBgResponse {
    

    private boolean success;
    private  HttpStatus statusCode;
    private Object data;
}
