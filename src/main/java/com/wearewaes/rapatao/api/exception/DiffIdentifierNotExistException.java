package com.wearewaes.rapatao.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when attempts to calculate the difference between the sides of a diff,
 * but the provided identifier does not represent any diff configuration.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The given identifier does not exist")
public class DiffIdentifierNotExistException extends Exception {
}
