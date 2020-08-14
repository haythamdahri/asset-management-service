package org.management.asset.services.impl;

import org.management.asset.clients.GoogleClient;
import org.management.asset.dto.RecaptchaResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.services.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Haytham DAHRI
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Value("${google.recaptcha.secret-key}")
    private String googleSecretKey;

    @Autowired
    private GoogleClient googleClient;

    @Override
    public RecaptchaResponseDTO verifyCaptcha(String humanKey) {
        try {
            return this.googleClient.verifyCaptcha(this.googleSecretKey, humanKey);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex);
        }
    }
}
