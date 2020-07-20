package org.management.asset.services.impl;

import org.management.asset.bo.Typology;
import org.management.asset.dao.TypologyRepository;
import org.management.asset.services.TypologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public boolean deleteTypology(String id) {
        this.typologyRepository.deleteById(id);
        return !this.typologyRepository.findById(id).isPresent();
    }

    @Override
    public Typology getTypology(String id) {
        return this.typologyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Typology> getTypologies() {
        return this.typologyRepository.findAll();
    }

    @Override
    public Page<Typology> getTypologies(int page, int size) {
        return this.typologyRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }
}
