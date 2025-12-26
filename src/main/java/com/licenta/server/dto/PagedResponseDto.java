package com.licenta.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PagedResponseDto<T>{
    private int page;
    private List<T> results;
    private int totalPages;
}
