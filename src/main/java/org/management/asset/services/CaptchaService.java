package org.management.asset.services;

import org.management.asset.dto.RecaptchaResponseDTO;

/**
 * @author Haytham DAHRI
 */
public interface CaptchaService {

    RecaptchaResponseDTO verifyCaptcha(String humanKey);

}
