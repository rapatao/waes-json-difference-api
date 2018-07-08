package com.wearewaes.rapatao.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A simple object that encapsulates the input JSON from the provided endpoints.
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestData {

    /**
     * The base64 encoded string
     */
    private String value;

}
