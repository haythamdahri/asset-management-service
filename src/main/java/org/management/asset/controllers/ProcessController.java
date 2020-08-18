package org.management.asset.controllers;

import org.management.asset.bo.Asset;
import org.management.asset.bo.Process;
import org.management.asset.dto.ProcessDTO;
import org.management.asset.dto.ProcessRequestDTO;
import org.management.asset.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/")
    public ResponseEntity<Page<Process>> getProcessesPage(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.processService.getProcesses(name, page, size, direction, sort));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @PostMapping(path = "/")
    public ResponseEntity<Process> getProcessesPage(@RequestBody ProcessRequestDTO processRequest) {
        return ResponseEntity.ok(this.processService.saveProcess(processRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Process> getProcess(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(this.processService.getProcess(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}/assets")
    public ResponseEntity<Set<Asset>> getProcessAssets(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(this.processService.getProcessAssets(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable(name = "id") String id) {
        this.processService.deleteProcess(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{id}/status")
    public ResponseEntity<Process> updateProcessStatus(@PathVariable(name = "id") String id, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.processService.updateProcessStatus(id, status));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_EDIT') or hasRole('ROLE_SUPER_USER')")
    @PutMapping(path = "/{id}/classification/status")
    public ResponseEntity<Process> updateProcessClassificationStatus(@PathVariable(name = "id") String id, @RequestParam(name = "status") boolean status) {
        return ResponseEntity.ok(this.processService.updateProcessClassificationStatus(id, status));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_PROCESSES_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/custom")
    public ResponseEntity<List<ProcessDTO>> getProcesses(@RequestParam(name = "id", required = false) String excludedProcessId) {
        return ResponseEntity.ok(this.processService.getCustomProcesses(excludedProcessId));
    }






}
