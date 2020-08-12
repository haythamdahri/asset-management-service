package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.Threat;
import org.management.asset.bo.Typology;
import org.management.asset.dao.TypologyRepository;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.ThreatRequestDTO;
import org.management.asset.dto.ThreatResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.helpers.PaginationHelper;
import org.management.asset.services.RiskAnalysisService;
import org.management.asset.services.ThreatService;
import org.management.asset.services.TypologyService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 */
@Service
public class ThreatServiceImpl implements ThreatService {

    @Autowired
    private TypologyService typologyService;

    @Autowired
    private RiskAnalysisService riskAnalysisService;

    @Autowired
    private TypologyRepository typologyRepository;

    @Autowired
    private PaginationHelper paginationHelper;

    @Override
    public List<Threat> getThreats() {
        return this.typologyService.getTypologies().stream().map(Typology::getThreats).flatMap(List::stream).collect(Collectors.toList());
    }
    @Override
    public Long getThreatsCounter() {
        return this.typologyService.getTypologies().stream().map(Typology::getThreats).flatMap(List::stream).count();
    }

    @Override
    public PageDTO<ThreatResponseDTO> getThreats(String name, int page, int size, String direction, String... sort) {
        List<ThreatResponseDTO> threatResponses = new ArrayList<>();
        List<Typology> typologies;
        // Sort by typology depending on sort
        if( sort[0].equals("typology") ) {
            // Reverse Asset
            if (direction.equals(Sort.Direction.ASC.name())) {
                typologies = this.typologyRepository.findAll(Sort.by("name").descending());
            } else {
                typologies = this.typologyRepository.findAll(Sort.by("name").ascending());
            }
        } else {
            typologies = this.typologyRepository.findAll();
        }
        typologies.forEach(typology -> {
            if (typology.getThreats() != null) {
                typology.getThreats().forEach(threat -> {
                    if (threat.getName().toLowerCase().contains(name.toLowerCase())) {
                        threatResponses.add(new ThreatResponseDTO(typology.getId(), typology.getName(), threat));
                    }
                });
            }
        });
        // Sort ThreatResponses
        threatResponses.sort((t1 ,t2) -> {
                switch(sort[0]) {
                    case "name":
                        return t1.getThreat().getName().compareTo(t2.getThreat().getName());
                    case "description":
                        return t1.getThreat().getDescription().compareTo(t2.getThreat().getDescription());
                    case "status":
                        return t2.getThreat().getStatus().compareTo(t1.getThreat().getStatus());
                    default:
                        return t1.getThreat().getIdentificationDate().compareTo(t2.getThreat().getIdentificationDate());
                }
        });
        // Reverse list if direction is DESC
        if (direction.equals(Sort.Direction.DESC.name())) {
            Collections.reverse(threatResponses);
        }
        // Pagination
        return this.paginationHelper.buildPage(page, size, threatResponses);
    }

    @Override
    public ThreatResponseDTO saveThreat(ThreatRequestDTO threatRequest) {
        try {
            final boolean threatRequestIdNotExists = StringUtils.isEmpty(threatRequest.getThreat()) ||
                    threatRequest.getThreat() == null ||
                    StringUtils.equals(threatRequest.getThreat(), "null") ||
                    StringUtils.equals(threatRequest.getThreat(), "undefined");
            threatRequest.setThreat(threatRequestIdNotExists ? null : threatRequest.getThreat());
            AtomicReference<Threat> threatAtomicReference = new AtomicReference<>(null);
            // Get typology
            Typology typology = this.typologyRepository.findById(threatRequest.getTypology()).orElseThrow(BusinessException::new);
            // Check if name is already used in same typology of threats
            if (typology.getThreats() != null && typology.getThreats().stream().anyMatch(threat ->
                    threat.getName().equalsIgnoreCase(threatRequest.getName()) && !StringUtils.equals(threat.getId(), threatRequest.getThreat()))) {
                throw new BusinessException(Constants.THREAT_NAME_ALREADY_TAKEN);
            }
            // Check if new threat
            if (threatRequestIdNotExists) {
                // Create new Threat and push it
                Threat threat = new Threat();
                threat.setName(threatRequest.getName());
                threat.setDescription(threatRequest.getDescription());
                threat.setStatus(threatRequest.isStatus());
                threat.setIdentificationDate(LocalDateTime.now());
                // Add threat to typology
                typology.addThreat(threat);
                // Save typology
                this.typologyRepository.save(typology);
                return new ThreatResponseDTO(typology.getId(), typology.getName(), threat);
            }
            // Check if typology is not changed
            if (StringUtils.equals(threatRequest.getTypology(), threatRequest.getCurrentTypology())) {
                // Update Vulnerability status
                typology.setThreats(typology.getThreats().stream().peek(threat -> {
                    if (StringUtils.equals(threatRequest.getThreat(), threat.getId())) {
                        threat.setName(threatRequest.getName());
                        threat.setDescription(threatRequest.getDescription());
                        threat.setStatus(threatRequest.isStatus());
                        threatAtomicReference.set(threat);
                    }
                }).collect(Collectors.toList()));
            } else {
                // Remove threat from old typology
                Typology currentTypology = this.typologyRepository.findById(threatRequest.getCurrentTypology()).orElseThrow(BusinessException::new);
                currentTypology.setThreats(currentTypology.getThreats().stream().filter(threat -> !StringUtils.equals(threat.getId(), threatRequest.getThreat())).collect(Collectors.toList()));
                this.typologyRepository.save(currentTypology);
                // Create new Threat and push it
                Threat threat = new Threat();
                threat.setName(threatRequest.getName());
                threat.setDescription(threatRequest.getDescription());
                threat.setStatus(threatRequest.isStatus());
                threat.setIdentificationDate(LocalDateTime.now());
                // Add threat to typology
                typology.addThreat(threat);
                this.typologyRepository.save(typology);
                return new ThreatResponseDTO(typology.getId(), typology.getName(), threat);
            }
            // Save typology if threat found
            if (threatAtomicReference.get() != null) {
                this.typologyRepository.save(typology);
                // Update RiskAnalysis threat
                this.riskAnalysisService.updateRiskAnalysisThreat(typology.getId(), threatAtomicReference.get());
                // Return response
                return new ThreatResponseDTO(typology.getId(), typology.getName(), threatAtomicReference.get());
            }
            throw new BusinessException(Constants.ERROR);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }
}
