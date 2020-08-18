package org.management.asset.clients;

import org.management.asset.dto.RecaptchaResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Haytham DAHRI
 */
@Component
public class GoogleClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${google.recaptcha.url}")
    private String googleVerificationUrl;

    public RecaptchaResponseDTO verifyCaptcha(String secret, String response) {
        try {
            // Request Headers + Params
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("secret", secret);
            map.add("response", response);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            // Send Post Request
            ResponseEntity<RecaptchaResponseDTO> httpResponse = this.restTemplate.postForEntity(this.googleVerificationUrl, request, RecaptchaResponseDTO.class);
            // Check response
            if( httpResponse.getStatusCode().is2xxSuccessful() ) {
                return httpResponse.getBody();
            }
            throw new BusinessException();
        } catch(BusinessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
