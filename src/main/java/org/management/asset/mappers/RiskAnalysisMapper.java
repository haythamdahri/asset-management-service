package org.management.asset.mappers;

import org.management.asset.bo.Setting;
import org.management.asset.dto.AnalysisOptionsResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Haytham DAHRI
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RiskAnalysisMapper {

    AnalysisOptionsResponseDTO toDTO(Setting setting);

}
