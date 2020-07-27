package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.RiskScenario;
import org.management.asset.bo.Threat;
import org.management.asset.bo.Typology;
import org.management.asset.bo.Vulnerability;
import org.management.asset.dao.TypologyRepository;
import org.management.asset.dto.ThreatResponseDTO;
import org.management.asset.dto.TypologyRequestDTO;
import org.management.asset.dto.TypologyResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.services.TypologyService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 * Implémentation du service des typologies
 */
@Service
public class TypologyServiceImpl implements TypologyService {

    @Autowired
    private TypologyRepository typologyRepository;

    @Override
    public Typology saveTypology(Typology typology) {
        return this.typologyRepository.save(typology);
    }

    @Override
    public Typology saveTypology(TypologyRequestDTO typologyRequest) {
        try {
            // Set ID to null if empty
            final boolean typologyRequestIdNotExists = StringUtils.isEmpty(typologyRequest.getId()) ||
                    typologyRequest.getId() == null ||
                    StringUtils.equals(typologyRequest.getId(), "null") ||
                    StringUtils.equals(typologyRequest.getId(), "undefined");
            typologyRequest.setId(typologyRequestIdNotExists ? null : typologyRequest.getId());
            Typology typology = new Typology();
            if( !typologyRequestIdNotExists ) {
                typology = this.typologyRepository.findById(typologyRequest.getId()).orElse(new Typology());
            }
            if( (typologyRequestIdNotExists && this.typologyRepository.findByName(typologyRequest.getName()).isPresent()) || (
                    !typologyRequestIdNotExists && !StringUtils.equals(typology.getName(), typologyRequest.getName()) && this.typologyRepository.findByName(typologyRequest.getName()).isPresent()
                    ) ) {
                throw new BusinessException(Constants.TYPOLOGY_NAME_ALREADY_TAKEN);
            }
            // set name and description
            typology.setName(typologyRequest.getName());
            typology.setDescription(typologyRequest.getDescription());
            // Save typology and return data
            return this.typologyRepository.save(typology);
        } catch(BusinessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @Override
    public boolean deleteTypology(String id) {
        this.typologyRepository.deleteById(id);
        return !this.typologyRepository.findById(id).isPresent();
    }

    @Override
    public Typology getTypology(String id) {
        return this.typologyRepository.findById(id).orElse(null);
    }

    @Override
    public Threat updateTypologyThreatStatus(String typologyId, String threatId, boolean status) {
        try {
            AtomicReference<Threat> threat = new AtomicReference<>(null);
            // Get typology
            Typology typology = this.typologyRepository.findById(typologyId).orElseThrow(BusinessException::new);
            // Update threat status
            typology.setThreats(typology.getThreats().stream().peek(localThreat -> {
                if( StringUtils.equals(threatId, localThreat.getId()) ) {
                    localThreat.setStatus(status);
                    threat.set(localThreat);
                }
            }).collect(Collectors.toList()));
            // Save typology if threat found
            if( threat.get() != null ) {
                this.typologyRepository.save(typology);
                return threat.get();
            }
            // Throw BusinessException if not threat found
            throw new BusinessException(Constants.NO_THREAT_FOUND);
        } catch(BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public ThreatResponseDTO getTypologyThreatStatus(String typologyId, String threatId) {
        try {
            // Get typology
            Typology typology = this.typologyRepository.findById(typologyId).orElseThrow(BusinessException::new);
            // Update threat status
            Optional<Threat> optionalThreat = typology.getThreats().stream().filter(threat -> threat.getId().equals(threatId)).findFirst();
            if( optionalThreat.isPresent() ) {
                return new ThreatResponseDTO(typologyId, typology.getName(), optionalThreat.get());
            }
            // Throw BusinessException if not threat found
            throw new BusinessException(Constants.NO_THREAT_FOUND);
        } catch(BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public Vulnerability updateTypologyVulnerabilityStatus(String typologyId, String vulnerabilityId, boolean status) {
        try {
            AtomicReference<Vulnerability> vulnerability = new AtomicReference<>(null);
            // Get typology
            Typology typology = this.typologyRepository.findById(typologyId).orElseThrow(BusinessException::new);
            // Update Vulnerability status
            typology.setVulnerabilities(typology.getVulnerabilities().stream().peek(localVulnerabilityId -> {
                if( StringUtils.equals(vulnerabilityId, localVulnerabilityId.getId()) ) {
                    localVulnerabilityId.setStatus(status);
                    vulnerability.set(localVulnerabilityId);
                }
            }).collect(Collectors.toList()));
            // Save typology if threat found
            if( vulnerability.get() != null ) {
                this.typologyRepository.save(typology);
                return vulnerability.get();
            }
            // Throw BusinessException if not threat found
            throw new BusinessException(Constants.NO_VULENRABILITY_FOUND);
        } catch(BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public RiskScenario updateTypologyRiskScenarioStatus(String typologyId, String riskScenarioId, boolean status) {
        try {
            AtomicReference<RiskScenario> riskScenario = new AtomicReference<>(null);
            // Get typology
            Typology typology = this.typologyRepository.findById(typologyId).orElseThrow(BusinessException::new);
            // Update Vulnerability status
            typology.setRiskScenarios(typology.getRiskScenarios().stream().peek(localRiskScenario -> {
                if( StringUtils.equals(riskScenarioId, localRiskScenario.getId()) ) {
                    localRiskScenario.setStatus(status);
                    riskScenario.set(localRiskScenario);
                }
            }).collect(Collectors.toList()));
            // Save typology if threat found
            if( riskScenario.get() != null ) {
                this.typologyRepository.save(typology);
                return riskScenario.get();
            }
            // Throw BusinessException if not threat found
            throw new BusinessException(Constants.NO_RISK_SCENARIO_FOUND);
        } catch(BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteTypologyThreatStatus(String typologyId, String threatId) {
        try {
            // Get typology
            Typology typology = this.typologyRepository.findById(typologyId).orElseThrow(BusinessException::new);
            typology.setThreats(typology.getThreats().stream().filter(threat -> !threat.getId().equals(threatId)).collect(Collectors.toList()));
            this.typologyRepository.save(typology);
            return true;
        } catch(BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public List<Typology> getTypologies() {
        return this.typologyRepository.findAll();
    }

    @Override
    public List<TypologyResponseDTO> getCustomTypologies() {
        return this.typologyRepository.findCustomTypologies();
    }

    @Override
    public Page<Typology> getTypologies(int page, int size) {
        return this.typologyRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }

    @Override
    public Page<Typology> getTypologies(String name, int page, int size) {
        if (StringUtils.isEmpty(name)) {
            return this.getTypologies(page, size);
        }
        return this.typologyRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }
}
