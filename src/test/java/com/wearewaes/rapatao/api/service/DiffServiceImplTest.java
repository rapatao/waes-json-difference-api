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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DiffServiceImplTest {

    private DiffService diffService;
    private DiffRepository diffRepository;

    @Before
    public void setUp() {
        diffRepository = mock(DiffRepository.class);
        diffService = new DiffServiceImpl(diffRepository);
    }

    @Test(expected = InvalidDiffIdentifier.class)
    public void shouldRejectWhenDiffIdIsNull() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        diffService.add(null, null, null);
    }

    @Test(expected = InvalidDiffIdentifier.class)
    public void shouldFailWhenDiffIdIsEmpty() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        diffService.add("", null, null);
    }

    @Test(expected = InvalidContentException.class)
    public void shouldRejectWhenBase64StringIsNull() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        diffService.add(UUID.randomUUID().toString(), null, null);
    }

    @Test(expected = InvalidContentException.class)
    public void shouldRejectWhenBase64StringIsEmpty() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        diffService.add(UUID.randomUUID().toString(), "", null);
    }

    @Test(expected = DiffSideIsNotDefinedException.class)
    public void shouldRejectWhenDiffSideIsNotSpecified() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        diffService.add(UUID.randomUUID().toString(), "asdf", null);
    }

    @Test
    public void shouldCreateNewWithTheLeftSideContent() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(eq(diffId))).thenReturn(Optional.empty());

        diffService.add(diffId, "asdf", Side.LEFT);

        verify(diffRepository).findById(eq(diffId));
        verify(diffRepository).save(ArgumentMatchers.argThat(a -> {
            assertEquals(diffId, a.getId());
            assertEquals("asdf", a.getLeft());
            assertNull(a.getRight());
            return true;
        }));
    }

    @Test
    public void shouldCreateNewWithTheRightSideContent() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(eq(diffId))).thenReturn(Optional.empty());

        diffService.add(diffId, "asdf", Side.RIGHT);

        verify(diffRepository).findById(eq(diffId));
        verify(diffRepository).save(ArgumentMatchers.argThat(a -> {
            assertEquals(diffId, a.getId());
            assertEquals("asdf", a.getRight());
            assertNull(a.getLeft());
            return true;
        }));
    }

    @Test
    public void shouldUpdateLeftSideWhenFileIdExistsAndTheSideIsAlreadyDefined() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(eq(diffId))).thenReturn(Optional.of(
                DiffDocument.builder()
                        .id(diffId)
                        .left("asdf")
                        .right("asdf")
                        .build()));

        diffService.add(diffId, "fdsa", Side.LEFT);

        verify(diffRepository).findById(eq(diffId));
        verify(diffRepository).save(ArgumentMatchers.argThat(a -> {
            assertEquals(diffId, a.getId());
            assertEquals("fdsa", a.getLeft());
            assertEquals("asdf", a.getRight());
            return true;
        }));
    }

    @Test
    public void shouldUpdateRightSideWhenFileIdExistsAndTheSideIsAlreadyDefined() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(eq(diffId))).thenReturn(Optional.of(
                DiffDocument.builder()
                        .id(diffId)
                        .left("asdf")
                        .right("asdf")
                        .build()));

        diffService.add(diffId, "fdsa", Side.RIGHT);

        verify(diffRepository).findById(eq(diffId));
        verify(diffRepository).save(ArgumentMatchers.argThat(a -> {
            assertEquals(diffId, a.getId());
            assertEquals("asdf", a.getLeft());
            assertEquals("fdsa", a.getRight());
            return true;
        }));
    }

    @Test
    public void shouldAddLeftSideAndKeepTheRightSideContent() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(eq(diffId))).thenReturn(Optional.of(
                DiffDocument.builder()
                        .id(diffId)
                        .right("asdf")
                        .build()));

        diffService.add(diffId, "asdf", Side.LEFT);

        verify(diffRepository).findById(eq(diffId));
        verify(diffRepository).save(ArgumentMatchers.argThat(a -> {
            assertEquals(diffId, a.getId());
            assertEquals("asdf", a.getLeft());
            assertEquals("asdf", a.getRight());
            return true;
        }));
    }

    @Test
    public void shouldAddRightSideAndKeepTheLeftSideContent() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(eq(diffId))).thenReturn(Optional.of(
                DiffDocument.builder()
                        .id(diffId)
                        .left("fdsa")
                        .build()));

        diffService.add(diffId, "fdsa", Side.RIGHT);

        verify(diffRepository).findById(eq(diffId));
        verify(diffRepository).save(ArgumentMatchers.argThat(a -> {
            assertEquals(diffId, a.getId());
            assertEquals("fdsa", a.getLeft());
            assertEquals("fdsa", a.getRight());
            return true;
        }));
    }

    @Test(expected = InvalidContentException.class)
    public void shouldNotAcceptContentWithInvalidBase64Scheme() throws InvalidContentException, InvalidDiffIdentifier, DiffSideIsNotDefinedException {
        diffService.add(UUID.randomUUID().toString(), "%4asdx!", Side.LEFT);
    }

    @Test(expected = DiffIdentifierNotExistException.class)
    public void shouldFailWhenIdDoesNotExist() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        when(diffRepository.findById(anyString())).thenReturn(Optional.empty());
        diffService.diff(UUID.randomUUID().toString());
    }

    @Test(expected = NoContentDefinedOneOrMoreSidesException.class)
    public void shouldFailWhenBothSideIsMissing() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        final String diffId = UUID.randomUUID().toString();
        when(diffRepository.findById(diffId))
                .thenReturn(Optional.of(DiffDocument.builder()
                        .id(diffId)
                        .build()));

        diffService.diff(diffId);
    }

    @Test(expected = NoContentDefinedOneOrMoreSidesException.class)
    public void shouldFailWhenLeftSideIsMissing() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        final String diffId = UUID.randomUUID().toString();
        when(diffRepository.findById(diffId))
                .thenReturn(Optional.of(DiffDocument.builder()
                        .id(diffId)
                        .right("asdf")
                        .build()));

        diffService.diff(diffId);
    }

    @Test(expected = NoContentDefinedOneOrMoreSidesException.class)
    public void shouldFailWhenRightSideIsMissing() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        final String diffId = UUID.randomUUID().toString();
        when(diffRepository.findById(diffId))
                .thenReturn(Optional.of(DiffDocument.builder()
                        .id(diffId)
                        .left("asdf")
                        .build()));

        diffService.diff(diffId);
    }

    @Test
    public void shouldReturnEqualsWhenBothSideAreEquals() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        final String diffId = UUID.randomUUID().toString();
        when(diffRepository.findById(anyString()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("asdf").right("asdf").build()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("").right("").build()));

        assertEquals(ResponseType.EQUALS, diffService.diff(diffId).getType());
        assertEquals(ResponseType.EQUALS, diffService.diff(diffId).getType());
    }

    @Test
    public void shouldReturnDifferentSize() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(anyString()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("a").right("aa").build()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("a").right("").build()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("ab").right("abc").build()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("ICAgICA=").right("     ").build()));

        for (int i = 0; i < 4; i++) {
            assertEquals(ResponseType.DIFFERENT_SIZE, diffService.diff(diffId).getType());
        }
    }

    @Test
    public void shouldReturnDifferentWhenTheSizeAreEqualsButTheContentIsDifferent() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(anyString()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("a").right("b").build()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("abc").right("cba").build()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("ICAgICA=").right("        ").build()));

        for (int i = 0; i < 3; i++) {
            final ResponseData diff = diffService.diff(diffId);
            assertEquals(ResponseType.DIFFERENT_CONTENT, diff.getType());
        }
    }

    @Test
    public void shouldReturnDifferentContentWithOffsetsAndDataLength() throws DiffIdentifierNotExistException, NoContentDefinedOneOrMoreSidesException {
        final String diffId = UUID.randomUUID().toString();

        when(diffRepository.findById(anyString()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("YWFhYWFhYWE=").right("ICAgICAgICA=").build()))
                .thenReturn(Optional.of(DiffDocument.builder().id(diffId).left("ICAgICAgICa=").right("ICAgICAgICA=").build()));

        final ResponseData diff1 = diffService.diff(diffId);
        assertEquals(ResponseType.DIFFERENT_CONTENT, diff1.getType());
        assertEquals("Offsets: 0,1,2,3,4,5,6,7,8,9,10 - Length: 12", diff1.getMessage());

        final ResponseData diff2 = diffService.diff(diffId);
        assertEquals(ResponseType.DIFFERENT_CONTENT, diff2.getType());
        assertEquals("Offsets: 10 - Length: 12", diff2.getMessage());
    }

}
