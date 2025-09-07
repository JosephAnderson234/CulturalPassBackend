package com.culturalpass.culturalpass.Event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDto<T> {

    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
}
