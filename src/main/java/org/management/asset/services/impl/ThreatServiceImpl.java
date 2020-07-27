package org.management.asset.services.impl;

import org.management.asset.bo.Threat;
import org.management.asset.bo.Typology;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.ThreatResponseDTO;
import org.management.asset.helpers.PaginationHelper;
import org.management.asset.services.ThreatService;
import org.management.asset.services.TypologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 */
@Service
public class ThreatServiceImpl implements ThreatService {

    @Autowired
    private TypologyService typologyService;

    @Autowired
    private PaginationHelper paginationHelper;

    @Override
    public List<Threat> getThreats() {
        return this.typologyService.getTypologies().stream().map(Typology::getThreats).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public PageDTO<ThreatResponseDTO> getThreats(String name, int page, int size) {
        List<ThreatResponseDTO> threatResponses = new ArrayList<>();
        this.typologyService.getTypologies().forEach(typology -> {
            if( typology.getThreats() != null ) {
                typology.getThreats().forEach(threat -> {
                    if( threat.getName().toLowerCase().contains(name.toLowerCase()) ) {
                        threatResponses.add(new ThreatResponseDTO(typology.getId(), typology.getName(), threat));
                    }
                });
            }
        });
        // Pagination
        return this.paginationHelper.buildPage(page, size, threatResponses);
    }
}
