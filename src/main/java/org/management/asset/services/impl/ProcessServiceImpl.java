package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.Process;
import org.management.asset.dao.ProcessRepository;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.services.ProcessService;
import org.management.asset.utils.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 * Implémentation du service des processus
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Override
    public Process saveProcess(Process process) {
        return this.processRepository.save(process);
    }

    @Override
    public Process updateProcessStatus(String id, boolean status) {
        Process process = this.processRepository.findById(id).orElseThrow(BusinessException::new);
        process.setStatus(status);
        return this.processRepository.save(process);
    }

    @Override
    public boolean deleteProcess(String id) {
        this.processRepository.deleteById(id);
        return !this.processRepository.findById(id).isPresent();
    }

    @Override
    public Process getProcess(String id) {
        return this.processRepository.findById(id).orElse(null);
    }

    @Override
    public List<Process> getProcesses() {
        return this.processRepository.findAll();
    }

    @Override
    public Long getProcessesCounter() {
        return this.processRepository.countProcesses();
    }

    @Override
    public Page<Process> getProcesses(int page, int size) {
        return this.processRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }

    @Override
    public Page<Process> getProcesses(String name, int page, int size) {
        if (StringUtils.isEmpty(name)) {
            return this.getProcesses(page, size);
        } else {
            return this.processRepository.findByNameContainingIgnoreCase(ApplicationUtils.escapeSpecialRegexChars(name.toLowerCase().trim()), PageRequest.of(page, size, Sort.Direction.ASC, "id"));
        }
    }
}
