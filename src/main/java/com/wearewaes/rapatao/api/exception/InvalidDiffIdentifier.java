package com.wearewaes.rapatao.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when attempts to add content in one of the sides of a diff an invalid identifier.
 * The identifier is invalid when it is null or empty.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The given identifier is invalid")
public class InvalidDiffIdentifier extends Exception {
}
