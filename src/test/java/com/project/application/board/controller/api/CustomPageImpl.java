package com.project.application.board.controller.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class CustomPageImpl<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomPageImpl(
            @JsonProperty("content") List<T> content,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") Boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("totalElements") long totalElements,
            @JsonProperty("size") int size,
            @JsonProperty("number") int number,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") Boolean first,
            @JsonProperty("numberOfElements") int numberOfElements,
            @JsonProperty("empty") Boolean empty
            ){

        super(content, PageRequest.of(number, size), totalElements);
    }
}
