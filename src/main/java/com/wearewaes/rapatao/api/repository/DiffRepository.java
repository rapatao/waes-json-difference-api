package com.wearewaes.rapatao.api.repository;

import com.wearewaes.rapatao.api.domain.DiffDocument;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface DiffRepository {

    /**
     * Finds in the Cassandra database using the given identifier by a diff document.
     *
     * @param id the diff identifier
     * @return the diff document
     */
    Optional<DiffDocument> findById(String id);

    /**
     * Create or update the given diff document.
     *
     * @param entity the diff document to be created or updated
     * @return the saved diff document
     */
    DiffDocument save(DiffDocument entity);

}
