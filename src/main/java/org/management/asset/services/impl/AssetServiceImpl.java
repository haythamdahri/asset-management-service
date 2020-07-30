package org.management.asset.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.*;
import org.management.asset.dao.*;
import org.management.asset.dto.AssetDTO;
import org.management.asset.dto.AssetRequestDTO;
import org.management.asset.dto.RiskAnalysisResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.services.AssetService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 * Implémentation du service des actifs
 */
@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypologyRepository typologyRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Override
    public Asset saveAsset(Asset asset) {
        return this.assetRepository.save(asset);
    }

    @Override
    public Asset saveAsset(AssetRequestDTO assetRequest) {
        try {
            // Set ID to null if empty
            final boolean assetRequestIdNotExists = StringUtils.isEmpty(assetRequest.getId()) ||
                    assetRequest.getId() == null ||
                    StringUtils.equals(assetRequest.getId(), "null") ||
                    StringUtils.equals(assetRequest.getId(), "undefined");
            assetRequest.setId(assetRequestIdNotExists ? null : assetRequest.getId());
            Asset asset;
            if (assetRequestIdNotExists) {
                asset = new Asset();
            } else {
                asset = this.assetRepository.findById(assetRequest.getId()).orElseThrow(BusinessException::new);
            }
            // Set data
            asset.setName(assetRequest.getName());
            asset.setDescription(assetRequest.getDescription());
            asset.setStatus(assetRequest.isStatus());
            if (assetRequestIdNotExists) {
                asset.setIdentificationDate(LocalDateTime.now(ZoneId.of("UTC+1")));
            }
            // Update asset image
            if (assetRequestIdNotExists || assetRequest.isUpdateImage()) {
                this.updateAssetImage(assetRequest.getFile(), asset);
            }
            // Update classification
            if (asset.getClassification() == null) {
                asset.setClassification(new ClassificationDICT(LocalDateTime.now(ZoneId.of("UTC+1"))));
            }
            asset.getClassification().setStatus(assetRequest.isClassificationStatus());
            asset.getClassification().setAvailability(assetRequest.getAvailability());
            asset.getClassification().setConfidentiality(assetRequest.getConfidentiality());
            asset.getClassification().setIntegrity(assetRequest.getIntegrity());
            asset.getClassification().setTraceability(assetRequest.getTraceability());
            // Set owner
            if (StringUtils.isNotEmpty(assetRequest.getOwner())) {
                asset.setOwner(this.userRepository.findById(assetRequest.getOwner()).orElseThrow(BusinessException::new));
            }
            // Set Typology
            if (StringUtils.isNotEmpty(assetRequest.getTypology())) {
                asset.setTypology(this.typologyRepository.findById(assetRequest.getTypology()).orElseThrow(BusinessException::new));
            }
            // Set Location
            if (StringUtils.isNotEmpty(assetRequest.getTypology())) {
                asset.setLocation(this.locationRepository.findById(assetRequest.getLocation()).orElseThrow(BusinessException::new));
            }// Set Process
            if (StringUtils.isNotEmpty(assetRequest.getProcess())) {
                asset.setProcess(this.processRepository.findById(assetRequest.getProcess()).orElseThrow(BusinessException::new));
            }
            // Save asset and return
            return this.assetRepository.save(asset);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    private void updateAssetImage(MultipartFile file, Asset asset) throws IOException {
        if (file == null || file.isEmpty() || !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BusinessException(Constants.INVALID_ASSET_IMAGE);
        } else {
            // Update asset image file and link it with current user
            AssetFile image = new AssetFile();
            image.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
            image.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
            image.setFile(file.getBytes());
            image.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())).toString());
            asset.setImage(image);
        }
    }

    @Override
    public boolean deleteAsset(String id) {
        this.assetRepository.deleteById(id);
        return this.assetRepository.findById(id).isPresent();
    }

    @Override
    public boolean deleteAssetRiskAnalysis(String assetId, String riskAnalysisId) {
        try {
            // Get asset
            Asset asset = this.assetRepository.findById(assetId).orElseThrow(BusinessException::new);
            asset.setRiskAnalyzes(asset.getRiskAnalyzes().stream().filter(riskAnalysis -> !riskAnalysis.getId().equals(riskAnalysisId)).collect(Collectors.toSet()));
            this.assetRepository.save(asset);
            return true;
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public Asset getAsset(String id) {
        Asset asset = this.assetRepository.findById(id).orElse(null);
        if (asset != null) {
            if (asset.getRiskAnalyzes() != null) {
                asset.setRiskAnalyzes(asset.getRiskAnalyzes().stream().peek(riskAnalysis -> {
                    riskAnalysis.calculateGeneratedValues(asset);
                }).collect(Collectors.toSet()));
            }
            if (asset.getTypology() != null) {
                asset.getClassification().postConstruct();
            }
        }
        return asset;
    }

    @Override
    public RiskAnalysisResponseDTO getAssetRiskAnalysis(String assetId, String riskAnalysisId) {
        try {
            // Get asset
            Asset asset = this.assetRepository.findById(assetId).orElseThrow(BusinessException::new);
            // Get RiskAnalysis
            Optional<RiskAnalysis> optionalRiskAnalysis = asset.getRiskAnalyzes().stream().filter(riskAnalysis -> riskAnalysis.getId().equals(riskAnalysisId)).findFirst();
            return optionalRiskAnalysis.map(riskAnalysis -> {
                riskAnalysis.calculateGeneratedValues(asset);
                return new RiskAnalysisResponseDTO(assetId, asset.getName(), riskAnalysis);
            }).orElse(null);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public RiskAnalysis updateAssetRiskAnalysisStatus(String assetId, String riskAnalysisId, boolean status) {
        try {
            AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
            // Get Asset
            Asset asset = this.assetRepository.findById(assetId).orElseThrow(BusinessException::new);
            // Update RiskAnalysis status
            if (asset.getRiskAnalyzes() != null) {
                asset.setRiskAnalyzes(asset.getRiskAnalyzes().stream().peek(riskAnalysis -> {
                    if (StringUtils.equals(riskAnalysisId, riskAnalysis.getId())) {
                        riskAnalysis.setStatus(status);
                        riskAnalysisAtomicReference.set(riskAnalysis);
                    }
                }).collect(Collectors.toSet()));
            }
            // Save Asset if threat found
            if (riskAnalysisAtomicReference.get() != null) {
                this.assetRepository.save(asset);
                return riskAnalysisAtomicReference.get();
            }
            // Throw BusinessException if not threat found
            throw new BusinessException(Constants.NO_RISK_SCENARIO_FOUND);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public Asset updateAssetStatus(String id, boolean status) {
        Asset asset = this.assetRepository.findById(id).orElseThrow(BusinessException::new);
        asset.setStatus(status);
        return this.assetRepository.save(asset);
    }

    @Override
    public List<Asset> getAssets() {
        return this.assetRepository.findAll();
    }

    @Override
    public Long getAssetsCounter() {
        return this.assetRepository.countAssets();
    }

    @Override
    public Page<Asset> getAssets(int page, int size) {
        return this.assetRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }

    @Override
    public Page<Asset> getAssets(String name, int page, int size) {
        return this.assetRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }

    @Override
    public List<AssetDTO> getCustomAssets() {
        return this.assetRepository.findCustomAssets();
    }

    @Override
    public Asset updateAssetClassificationStatus(String id, boolean status) {
        Asset asset = this.assetRepository.findById(id).orElseThrow(BusinessException::new);
        if (asset.getClassification() != null) {
            asset.getClassification().setStatus(status);
        }
        return this.assetRepository.save(asset);
    }
}
