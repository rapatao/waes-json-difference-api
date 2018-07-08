package com.wearewaes.rapatao.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when attempts to add content but the side is not specified.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The content side is not defined")
public class DiffSideIsNotDefinedException extends Exception {
}
