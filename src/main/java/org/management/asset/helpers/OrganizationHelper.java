package org.management.asset.helpers;

import org.management.asset.utils.Constants;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 */
@Component
public class OrganizationHelper {

    /**
     * Split string into list
     *
     * @param input
     * @return
     */
    public List<String> extractList(String input) {
        try {
            return Stream.of(input.split(Constants.STRING_SEPARATOR)).collect(Collectors.toList());
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

}
