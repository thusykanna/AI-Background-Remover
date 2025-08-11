package thusySoftwareSolutions.BGremover.security;

import java.security.PublicKey;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClerkJwtAuthFilter extends OncePerRequestFilter {

    @Value("${clerk.issuer}")
    private String clerkIssuer;

    private final ClerkJwksProvider jwksProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        String authHeader = request.getHeader("Autherization");

        if (authHeader == null || !authHeader.startsWith("Bearer")){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization header is missing or invalid");
            return;

        }

        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            // Extract the kid from the JWT header
            String [] chunks = token.split("\\.");
            String headerJson = new String(Base64.getUrlDecoder().decode(chunks[0]));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode headerNode = mapper.readTree(headerJson);
            String kid = headerNode.get("kid").asText();

            // Get the public key using the kid
            PublicKey publicKey = jwksProvider.getPublicKey(kid);

            // Verify the JWT token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .setAllowedClockSkewSeconds(60) // Allow a small clock skew
                    .requireIssuer(clerkIssuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String clerkUserid = claims.getSubject();
            

        } catch (Exception e) {
            // TODO: handle exception
        }

        filterChain.doFilter(request, response);
    }
    
}
