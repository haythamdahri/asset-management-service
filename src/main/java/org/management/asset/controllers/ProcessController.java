package org.management.asset.controllers;

import org.management.asset.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/processes")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @GetMapping(path = "/counter")
    public ResponseEntity<Long> countProcesses() {
        return ResponseEntity.ok(this.processService.getProcessesCounter());
    }

}
