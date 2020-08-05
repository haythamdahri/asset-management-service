package org.management.asset.services.impl;

import org.management.asset.bo.Setting;
import org.management.asset.dao.SettingRepository;
import org.management.asset.dto.AnalysisOptionsResponseDTO;
import org.management.asset.dto.ClassificationResponseDTO;
import org.management.asset.dto.SettingRequestDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.mappers.RiskAnalysisMapper;
import org.management.asset.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Haytham DAHRI
 */
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private RiskAnalysisMapper riskAnalysisMapper;

    @Override
    public Setting saveSetting(Setting setting) {
        return this.settingRepository.save(setting);
    }

    @Override
    public Setting saveSetting(SettingRequestDTO settingRequest) {
        try {
            Setting setting = new Setting();
            setting.setProbabilities(IntStream.range(settingRequest.getMinProbability(), settingRequest.getMaxProbability() + 1).boxed().collect(Collectors.toList()));
            setting.setFinancialImpacts(IntStream.range(settingRequest.getMinFinancialImpact(), settingRequest.getMaxFinancialImpact() + 1).boxed().collect(Collectors.toList()));
            setting.setOperationalImpacts(IntStream.range(settingRequest.getMinOperationalImpact(), settingRequest.getMaxOperationalImpact() + 1).boxed().collect(Collectors.toList()));
            setting.setReputationalImpacts(IntStream.range(settingRequest.getMinReputationalImpact(), settingRequest.getMaxReputationalImpact() + 1).boxed().collect(Collectors.toList()));
            setting.setTargetFinancialImpacts(IntStream.range(settingRequest.getMinTargetFinancialImpact(), settingRequest.getMaxTargetFinancialImpact() + 1).boxed().collect(Collectors.toList()));
            setting.setTargetOperationalImpacts(IntStream.range(settingRequest.getMinTargetOperationalImpact(), settingRequest.getMaxTargetOperationalImpact() + 1).boxed().collect(Collectors.toList()));
            setting.setTargetReputationalImpacts(IntStream.range(settingRequest.getMinTargetReputationalImpact(), settingRequest.getMaxTargetReputationalImpact() + 1).boxed().collect(Collectors.toList()));
            setting.setTargetProbabilities(IntStream.range(settingRequest.getMinTargetProbability(), settingRequest.getMaxTargetProbability() + 1).boxed().collect(Collectors.toList()));
            setting.setAcceptableResidualRisks(IntStream.range(settingRequest.getMinAcceptableResidualRisk(), settingRequest.getMaxAcceptableResidualRisk() + 1).boxed().collect(Collectors.toList()));
            setting.setConfidentialities(IntStream.range(settingRequest.getMinConfidentiality(), settingRequest.getMaxConfidentiality() + 1).boxed().collect(Collectors.toList()));
            setting.setAvailabilities(IntStream.range(settingRequest.getMinAvailability(), settingRequest.getMaxAvailability() + 1).boxed().collect(Collectors.toList()));
            setting.setIntegrities(IntStream.range(settingRequest.getMinIntegrity(), settingRequest.getMaxIntegrity() + 1).boxed().collect(Collectors.toList()));
            setting.setTraceabilities(IntStream.range(settingRequest.getMinTraceability(), settingRequest.getMaxTraceability() + 1).boxed().collect(Collectors.toList()));
            setting.setIdentificationDate(LocalDateTime.now(ZoneId.of("UTC+1")));
            // Save and return setting
            return this.settingRepository.save(setting);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteSetting(String id) {
        this.settingRepository.deleteById(id);
        return !this.settingRepository.findById(id).isPresent();
    }

    @Override
    public Setting getSetting(String id) {
        return this.settingRepository.findById(id).orElse(null);
    }

    @Override
    public Setting getActiveSetting() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "identificationDate"));
        return this.mongoOperations.findOne(query, Setting.class);
    }

    @Override
    public ClassificationResponseDTO getActiveSettingClassification() {
        Setting setting = this.getActiveSetting();
        if (setting != null) {
            ClassificationResponseDTO classificationResponse = new ClassificationResponseDTO();
            classificationResponse.setAvailabilities(setting.getAvailabilities());
            classificationResponse.setIntegrities(setting.getIntegrities());
            classificationResponse.setConfidentialities(setting.getConfidentialities());
            classificationResponse.setTraceabilities(setting.getTraceabilities());
            return classificationResponse;
        }
        return null;
    }

    @Override
    public AnalysisOptionsResponseDTO getActiveSettingRiskAnalysisOptions() {
        Setting setting = this.getActiveSetting();
        if (setting != null) {
            return this.riskAnalysisMapper.toDTO(setting);
        }
        return null;
    }

    @Override
    public List<Setting> getSettings() {
        return this.settingRepository.findAll();
    }
}
