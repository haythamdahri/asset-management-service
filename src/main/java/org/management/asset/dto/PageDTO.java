package org.management.asset.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> implements Serializable {

    private static final long serialVersionUID = 5993847194565819514L;

    @JsonProperty("last")
    private boolean last;

    @JsonProperty("first")
    private boolean first;

    @JsonProperty("totalElements")
    private int totalElements;

    @JsonProperty("numberOfElements")
    private int numberOfElements;

    @JsonProperty("number")
    private int number;

    @JsonProperty("pageSize")
    private int pageSize;

    @JsonProperty("size")
    private int size;

    @JsonProperty("content")
    private List<T> content;

    @JsonProperty("pageable")
    private PageableDTO pageable;

}
