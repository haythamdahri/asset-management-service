package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.management.asset.bo.ClassificationDICT;
import org.management.asset.bo.Process;
import org.management.asset.dao.OrganizationRepository;
import org.management.asset.dao.ProcessRepository;
import org.management.asset.dto.ProcessDTO;
import org.management.asset.dto.ProcessRequestDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.services.ProcessService;
import org.management.asset.utils.ApplicationUtils;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Haytham DAHRI
 * Implémentation du service des processus
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Process saveProcess(Process process) {
        return this.processRepository.save(process);
    }

    @Override
    public Process saveProcess(ProcessRequestDTO processRequest) {
        try {
            // Set ID to null if empty
            final boolean processRequestIdNotExists = StringUtils.isEmpty(processRequest.getId()) ||
                    processRequest.getId() == null ||
                    StringUtils.equals(processRequest.getId(), "null") ||
                    StringUtils.equals(processRequest.getId(), "undefined");
            processRequest.setId(processRequestIdNotExists ? null : processRequest.getId());
            Process process = new Process();
            if (processRequest.getId() != null) {
                process = this.processRepository.findById(processRequest.getId()).orElse(new Process());
            }
            // Check name if assigned to an other process
            if (processRequestIdNotExists) {
                if (this.processRepository.findProcessByNameAndOrganization_Id(processRequest.getName(), processRequest.getOrganization()).isPresent()) {
                    throw new BusinessException(Constants.PROCESS_NAME_ALREADY_TAKEN);
                }
            } else {
                if (!StringUtils.equals(process.getName(), processRequest.getName()) && this.processRepository.findProcessByNameAndOrganization_Id(processRequest.getName(), processRequest.getOrganization()).isPresent()) {
                    throw new BusinessException(Constants.PROCESS_NAME_ALREADY_TAKEN);
                }
            }
            // Check parent process
            if (StringUtils.isNotEmpty(processRequest.getParentProcess())) {
                Process parentProcess = this.processRepository.findById(processRequest.getParentProcess()).orElseThrow(BusinessException::new);
                if (StringUtils.equals(parentProcess.getId(), processRequest.getId())) {
                    throw new BusinessException(Constants.PARENT_PROCESS_IS_ALREADY_ASSIGNED_TO_SAME_PROCESS);
                }
            }
            process.setName(processRequest.getName());
            // Set data
            process.setDescription(processRequest.getDescription());
            process.setStatus(processRequest.isStatus());
            // Set organization
            process.setOrganization(this.organizationRepository.findById(processRequest.getOrganization()).orElseThrow(BusinessException::new));
            // Set parent process
            if (!StringUtils.isEmpty(processRequest.getParentProcess())) {
                process.setParentProcess(this.processRepository.findById(processRequest.getParentProcess()).orElse(null));
            }
            // Set classificationDICT
            ClassificationDICT classification = new ClassificationDICT(processRequest.getConfidentiality(), processRequest.getAvailability(), processRequest.getIntegrity(), processRequest.getTraceability(), processRequest.isClassificationStatus());
            if (process.getClassification() == null) {
                classification.setIdentificationDate(LocalDateTime.now(ZoneId.of("UTC+1")));
            } else {
                classification.setIdentificationDate(process.getClassification().getIdentificationDate());
            }
            process.setClassification(classification);
            // Return classification
            return this.processRepository.save(process);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public Process updateProcessStatus(String id, boolean status) {
        Process process = this.processRepository.findById(id).orElseThrow(BusinessException::new);
        process.setStatus(status);
        return this.processRepository.save(process);
    }

    @Override
    public Process updateProcessClassificationStatus(String id, boolean status) {
        Process process = this.processRepository.findById(id).orElseThrow(BusinessException::new);
        if (process.getClassification() != null) {
            process.getClassification().setStatus(status);
        }
        return this.processRepository.save(process);
    }

    @Override
    public boolean deleteProcess(String id) {
        this.processRepository.deleteById(id);
        return !this.processRepository.findById(id).isPresent();
    }

    @Override
    public Process getProcess(String id) {
        Process process = this.processRepository.findById(id).orElse(null);
        if (process != null && process.getClassification() != null) {
            process.getClassification().postConstruct();
        }
        return process;
    }

    @Override
    public List<Process> getOrganizationProcesses(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("organization.$id").is(new ObjectId(id)));
        return this.mongoOperations.find(query, Process.class);
    }

    @Override
    public List<Process> getProcesses() {
        return this.processRepository.findAll();
    }

    @Override
    public List<ProcessDTO> getCustomProcesses(String excludedProcessId) {
        if (excludedProcessId != null) {
            return this.processRepository.findCustomProcessesAndExclude(excludedProcessId);
        }
        return this.processRepository.findCustomProcesses();
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
