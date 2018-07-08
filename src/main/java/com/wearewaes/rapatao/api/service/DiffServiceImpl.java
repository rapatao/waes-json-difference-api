package com.wearewaes.rapatao.api.service;

import com.wearewaes.rapatao.api.domain.DiffDocument;
import com.wearewaes.rapatao.api.domain.ResponseData;
import com.wearewaes.rapatao.api.domain.ResponseType;
import com.wearewaes.rapatao.api.domain.Side;
import com.wearewaes.rapatao.api.exception.DiffIdentifierNotExistException;
import com.wearewaes.rapatao.api.exception.DiffSideIsNotDefinedException;
import com.wearewaes.rapatao.api.exception.InvalidContentException;
import com.wearewaes.rapatao.api.exception.InvalidDiffIdentifier;
import com.wearewaes.rapatao.api.exception.NoContentDefinedOneOrMoreSidesException;
import com.wearewaes.rapatao.api.repository.DiffRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@code DiffService} interface.
 * Implements the required operations within the required validations as well as the integration
 * with the database to persist and retrieve the data for future processing.
 */
@RequiredArgsConstructor
@Service
class DiffServiceImpl implements DiffService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiffService.class);

    private final DiffRepository diffRepository;

    @Override
    public DiffDocument add(String diffId, String content, Side side) throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        LOGGER.debug("Adding the {} to the {} with the content {}", side, diffId, content);

        if (diffId == null || diffId.trim().isEmpty()) {
            throw new InvalidDiffIdentifier();
        }

        if (content == null || content.trim().isEmpty() || !isContentAValidBase64(content)) {
            throw new InvalidContentException();
        }

        if (side == null) {
            throw new DiffSideIsNotDefinedException();
        }

        final DiffDocument byId = diffRepository.findById(diffId)
                .orElse(DiffDocument.builder().id(diffId).build());

        return diffRepository.save(DiffDocument.builder()
                .id(byId.getId())
                .left(Side.LEFT.equals(side) ? content : byId.getLeft())
                .right(Side.RIGHT.equals(side) ? content : byId.getRight())
                .build());
    }

    private boolean isContentAValidBase64(String content) {
        try {
            Base64.getDecoder().decode(content);
        } catch (IllegalArgumentException e) {
            LOGGER.debug("The content {} is not a valid base64", content, e);
            return false;
        }
        return true;
    }

    @Override
    public ResponseData diff(String diffId) throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        LOGGER.debug("Checking the difference for the id {}", diffId);

        final Optional<DiffDocument> byId = diffRepository.findById(diffId);

        if (!byId.isPresent()) {
            throw new DiffIdentifierNotExistException();
        }

        final DiffDocument diffDocument = byId.get();

        if (diffDocument.getLeft() == null || diffDocument.getRight() == null) {
            throw new NoContentDefinedOneOrMoreSidesException();
        }

        final byte[] left = diffDocument.getLeft().getBytes();
        final byte[] right = diffDocument.getRight().getBytes();

        if (Arrays.equals(left, right)) {
            return ResponseData.builder().type(ResponseType.EQUALS).build();
        }

        if (left.length != right.length) {
            return ResponseData.builder().type(ResponseType.DIFFERENT_SIZE).build();
        }


        final List<String> offsets = new ArrayList<>();
        for (int i = 0; i < left.length; i++) {
            if (left[i] != right[i]) {
                offsets.add(String.valueOf(i));
            }
        }

        final String message = "Offsets: " + String.join(",", offsets.toArray(new String[0])) + " - Length: " + left.length;

        return ResponseData.builder()
                .type(ResponseType.DIFFERENT_CONTENT)
                .message(message)
                .build();
    }

}
