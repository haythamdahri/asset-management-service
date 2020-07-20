package org.management.asset.services;

import org.management.asset.bo.Typology;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 * Spécification du service des typlogies
 */
public interface TypologyService {

    Typology saveTypology(Typology typology);

    boolean deleteTypology(String id);

    Typology getTypology(String id);

    List<Typology> getTypologies();

    Page<Typology> getTypologies(int page, int size);

}
