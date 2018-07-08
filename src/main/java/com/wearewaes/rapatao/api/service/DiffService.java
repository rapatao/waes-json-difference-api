package com.wearewaes.rapatao.api.service;

import com.wearewaes.rapatao.api.domain.DiffDocument;
import com.wearewaes.rapatao.api.domain.ResponseData;
import com.wearewaes.rapatao.api.domain.Side;
import com.wearewaes.rapatao.api.exception.DiffIdentifierNotExistException;
import com.wearewaes.rapatao.api.exception.DiffSideIsNotDefinedException;
import com.wearewaes.rapatao.api.exception.InvalidContentException;
import com.wearewaes.rapatao.api.exception.InvalidDiffIdentifier;
import com.wearewaes.rapatao.api.exception.NoContentDefinedOneOrMoreSidesException;

/**
 * The {@code DiffService} interface provides the required methods to support the
 * addition of the data in a diff document as well as the difference checker method
 * that examine both sides and returns differences information between the sides.
 */
public interface DiffService {

    /**
     * Adds the specified side for the diff document with the encoded Base64 content in the repository.
     *
     * @param diffId  the difference identifier.
     * @param content the base64 encoded content.
     * @param side    the json side of the matcher.
     * @throws InvalidContentException When the based 64 contains an invalid JSON.
     */
    DiffDocument add(String diffId, String content, Side side) throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException;

    /**
     * Get the both sides using the diff identifier and check the differences between the sides.
     *
     * @param diffId the difference identifier.
     * @return The difference response information.
     * @throws DiffIdentifierNotExistException         When the given identifier does not correspond to any diff data.
     * @throws NoContentDefinedOneOrMoreSidesException When the diff does not contains data on one of the sides.
     */
    ResponseData diff(String diffId) throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException;
}
