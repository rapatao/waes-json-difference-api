package com.wearewaes.rapatao.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Encapsulates the response data that describes the response type and a message.
 */
@Getter
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ResponseData {

    /**
     * The response type
     * <p>
     * {@see ResponseType}
     */
    private final ResponseType type;

    /**
     * The response message
     */
    private final String message;

}


