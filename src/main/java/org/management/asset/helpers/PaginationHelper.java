package org.management.asset.helpers;

import org.management.asset.dto.PageDTO;
import org.management.asset.dto.PageableDTO;
import org.management.asset.exceptions.BusinessException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Component
public class PaginationHelper {

    public <T> PageDTO<T> buildPage(int page, int size, List<T> sourceList) {
        List<T> content = this.getPage(sourceList, page, size);
        boolean last = this.getPage(sourceList, page + 1, size).isEmpty();
        return new PageDTO<>(last, page == 0, sourceList.size(), content.size(), page, content.size(), size, content, new PageableDTO(size, page));
    }

    /**
     * returns a view (not a new list) of the sourceList for the range based on page
     * and pageSize. This method is used when receiving data from third party
     * service
     *
     * @param sourceList
     * @param page,      page number should start from 1
     * @param size
     * @return
     */
    public <T> List<T> getPage(List<T> sourceList, int page, int size) throws BusinessException {
        // Increment page by 1 for splitting
        page++;
        // Check valid page and size
        if (size <= 0 || page <= 0) {
            throw new BusinessException("invalid page size: " + size);
        }

        int fromIndex = (page - 1) * size;
        if (sourceList == null || sourceList.size() < fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + size, sourceList.size()));
    }

}
