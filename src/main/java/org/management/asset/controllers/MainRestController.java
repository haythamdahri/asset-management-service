package org.management.asset.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/main")
public class MainRestController {

    @GetMapping(path = "/", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("<User><Name>Haytham</Name><Age>22</Age></User>");
    }

}
