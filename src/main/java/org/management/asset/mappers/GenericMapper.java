package org.management.asset.mappers;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface GenericMapper<S, T> {

    /**
     * Converts given model to dto.
     */
    T toDto(S source);

    /**
     * Converts given dto to model.
     */
    S toModel(T target);

    /**
     * Converts given model list to dto list.
     */
    List<T> toDto(List<S> sourceList);

    /**
     * Converts given dto list to model list.
     */
    List<S> toModel(List<T> targetList);

}
