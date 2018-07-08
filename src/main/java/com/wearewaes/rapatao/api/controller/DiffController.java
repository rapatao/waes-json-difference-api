package com.wearewaes.rapatao.api.controller;

import com.wearewaes.rapatao.api.domain.DiffDocument;
import com.wearewaes.rapatao.api.domain.RequestData;
import com.wearewaes.rapatao.api.domain.ResponseData;
import com.wearewaes.rapatao.api.domain.Side;
import com.wearewaes.rapatao.api.exception.DiffIdentifierNotExistException;
import com.wearewaes.rapatao.api.exception.DiffSideIsNotDefinedException;
import com.wearewaes.rapatao.api.exception.InvalidContentException;
import com.wearewaes.rapatao.api.exception.InvalidDiffIdentifier;
import com.wearewaes.rapatao.api.exception.NoContentDefinedOneOrMoreSidesException;
import com.wearewaes.rapatao.api.service.DiffService;
import com.wearewaes.rapatao.api.util.NavigationLinkBuilder;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Declares the endpoints that are used to add the content for both sides of a
 * diff document and a method to check the differences between the sides.
 */
@Api(tags = "diff", description = "Provides the endpoints to identify the differences of two base64 encoded JSON.")
@Controller
@RequestMapping("/diff/{id}")
@RequiredArgsConstructor
public class DiffController {

    private final DiffService diffService;

    /**
     * Provides an endpoint to add the left side of a diff document.
     *
     * @param diffId      the diff document identifier.
     * @param requestData an object that encapsulates the JSON with the base64 encoded content.
     * @return a representative object of the diff document.
     * @throws InvalidContentException       when the content is empty or invalid.
     * @throws InvalidDiffIdentifier         when the identifier is empty.
     * @throws DiffSideIsNotDefinedException when the side is not specified.
     */
    @PutMapping("/left")
    @NavigationLinkBuilder
    public HttpEntity<DiffDocument> addLeft(@PathVariable("id") String diffId,
                                            @RequestBody RequestData requestData)
            throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {

        return ResponseEntity.ok(diffService.add(diffId, requestData.getValue(), Side.LEFT));
    }

    /**
     * Provides an endpoint to add the right side of a diff document.
     *
     * @param diffId      the diff document identifier.
     * @param requestData an object that encapsulates the JSON with the base64 encoded content.
     * @return a representative object of the diff document.
     * @throws InvalidContentException       when the content is empty or invalid.
     * @throws InvalidDiffIdentifier         when the identifier is empty.
     * @throws DiffSideIsNotDefinedException when the side is not specified.
     */
    @PutMapping("/right")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @NavigationLinkBuilder
    public HttpEntity<DiffDocument> addRight(@PathVariable("id") String diffId,
                                             @RequestBody RequestData requestData)
            throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {

        return ResponseEntity.ok(diffService.add(diffId, requestData.getValue(), Side.RIGHT));
    }

    /**
     * Provides an endpoint to verify the differences between the sides of a diff document.
     *
     * @param diffId the diff document identifier
     * @return an object that describes the differences between the sides of the diff document.
     * @throws DiffIdentifierNotExistException         when the provided identifier does not represent a diff document.
     * @throws NoContentDefinedOneOrMoreSidesException when one of the sides of the diff document does not contain data.
     */
    @GetMapping
    @NavigationLinkBuilder
    public HttpEntity<ResponseData> difference(@PathVariable("id") String diffId)
            throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {

        return ResponseEntity.ok(diffService.diff(diffId));
    }

}
