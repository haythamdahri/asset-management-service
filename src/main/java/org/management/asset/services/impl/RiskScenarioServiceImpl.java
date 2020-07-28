package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.RiskScenario;
import org.management.asset.bo.Typology;
import org.management.asset.dao.TypologyRepository;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskScenarioRequestDTO;
import org.management.asset.dto.RiskScenarioResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.helpers.PaginationHelper;
import org.management.asset.services.RiskAnalysisService;
import org.management.asset.services.RiskScenarioService;
import org.management.asset.services.TypologyService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 */
@Service
public class RiskScenarioServiceImpl implements RiskScenarioService {

    @Autowired
    private TypologyService typologyService;

    @Autowired
    private RiskAnalysisService riskAnalysisService;

    @Autowired
    private TypologyRepository typologyRepository;

    @Autowired
    private PaginationHelper paginationHelper;

    @Override
    public List<RiskScenario> getRiskScenarios() {
        return this.typologyService.getTypologies().stream().map(Typology::getRiskScenarios).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public PageDTO<RiskScenarioResponseDTO> getRiskScenarios(String name, int page, int size) {
        List<RiskScenarioResponseDTO> riskScenarioResponses = new ArrayList<>();
        this.typologyService.getTypologies().forEach(typology -> {
            if (typology.getThreats() != null) {
                typology.getRiskScenarios().forEach(riskScenario -> {
                    if (riskScenario.getName().toLowerCase().contains(name.toLowerCase())) {
                        riskScenarioResponses.add(new RiskScenarioResponseDTO(typology.getId(), typology.getName(), riskScenario));
                    }
                });
            }
        });
        // Pagination
        return this.paginationHelper.buildPage(page, size, riskScenarioResponses);
    }

    @Override
    public RiskScenarioResponseDTO saveRiskScenario(RiskScenarioRequestDTO riskScenarioRequest) {
        try {
            final boolean riskScenarioRequestIdNotExists = StringUtils.isEmpty(riskScenarioRequest.getRiskScenario()) ||
                    riskScenarioRequest.getRiskScenario() == null ||
                    StringUtils.equals(riskScenarioRequest.getRiskScenario(), "null") ||
                    StringUtils.equals(riskScenarioRequest.getRiskScenario(), "undefined");
            riskScenarioRequest.setRiskScenario(riskScenarioRequestIdNotExists ? null : riskScenarioRequest.getRiskScenario());
            AtomicReference<RiskScenario> riskScenarioAtomicReference = new AtomicReference<>(null);
            // Get typology
            Typology typology = this.typologyRepository.findById(riskScenarioRequest.getTypology()).orElseThrow(BusinessException::new);
            // Check if name is already used in same typology of threats
            if (typology.getThreats() != null && typology.getRiskScenarios().stream().anyMatch(riskScenario ->
                    riskScenario.getName().equalsIgnoreCase(riskScenarioRequest.getName()) && !StringUtils.equals(riskScenario.getId(), riskScenarioRequest.getRiskScenario()))) {
                throw new BusinessException(Constants.RISK_SCENARIO_NAME_ALREADY_TAKEN);
            }
            // Check if new RiskScenario
            if (riskScenarioRequestIdNotExists) {
                // Create new RiskScenario and push it
                RiskScenario riskScenario = new RiskScenario();
                riskScenario.setName(riskScenarioRequest.getName());
                riskScenario.setDescription(riskScenarioRequest.getDescription());
                riskScenario.setStatus(riskScenarioRequest.isStatus());
                riskScenario.setIdentificationDate(LocalDateTime.now());
                // Add RiskScenario to typology
                typology.addRiskScenario(riskScenario);
                // Save typology
                this.typologyRepository.save(typology);
                return new RiskScenarioResponseDTO(typology.getId(), typology.getName(), riskScenario);
            }
            // Check if typology is not changed
            if (StringUtils.equals(riskScenarioRequest.getTypology(), riskScenarioRequest.getCurrentTypology())) {
                // Update RiskScenario status
                typology.setRiskScenarios(typology.getRiskScenarios().stream().peek(riskScenario -> {
                    if (StringUtils.equals(riskScenarioRequest.getRiskScenario(), riskScenario.getId())) {
                        riskScenario.setName(riskScenarioRequest.getName());
                        riskScenario.setDescription(riskScenarioRequest.getDescription());
                        riskScenario.setStatus(riskScenarioRequest.isStatus());
                        riskScenarioAtomicReference.set(riskScenario);
                    }
                }).collect(Collectors.toList()));
            } else {
                // Remove threat from old typology
                Typology currentTypology = this.typologyRepository.findById(riskScenarioRequest.getCurrentTypology()).orElseThrow(BusinessException::new);
                currentTypology.setThreats(currentTypology.getThreats().stream().filter(threat -> !StringUtils.equals(threat.getId(), riskScenarioRequest.getRiskScenario())).collect(Collectors.toList()));
                this.typologyRepository.save(currentTypology);
                // Create new RiskScenario and push it
                RiskScenario riskScenario = new RiskScenario();
                riskScenario.setName(riskScenarioRequest.getName());
                riskScenario.setDescription(riskScenarioRequest.getDescription());
                riskScenario.setStatus(riskScenarioRequest.isStatus());
                riskScenario.setIdentificationDate(LocalDateTime.now());
                // Add RiskScenario to typology
                typology.addRiskScenario(riskScenario);
                this.typologyRepository.save(typology);
                return new RiskScenarioResponseDTO(typology.getId(), typology.getName(), riskScenario);
            }
            // Save typology if threat found
            if (riskScenarioAtomicReference.get() != null) {
                this.typologyRepository.save(typology);
                // Update RiskAnalysis threat
                this.riskAnalysisService.updateRiskAnalysisRiskScenario(typology.getId(), riskScenarioAtomicReference.get());
                // Return response
                return new RiskScenarioResponseDTO(typology.getId(), typology.getName(), riskScenarioAtomicReference.get());
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
