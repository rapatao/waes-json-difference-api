package com.wearewaes.rapatao.api.domain;

/**
 * Enum that defines the possible response type.
 */
public enum ResponseType {

    /**
     * Represents the equality of the both sides of the diff document.
     */
    EQUALS,

    /**
     * Represents the different of sizes between the sides of the diff document.
     */
    DIFFERENT_SIZE,

    /**
     * Represents the different of content between the sides of the diff document.
     */
    DIFFERENT_CONTENT

}
