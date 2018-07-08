package com.wearewaes.rapatao.api.repository;

import com.wearewaes.rapatao.api.domain.DiffDocument;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiffRepository extends CassandraRepository<DiffDocument, String> {

    /**
     * Finds in the Cassandra database using the given identifier by a diff document.
     *
     * @param id the diff identifier
     * @return the diff document
     */
    @Override
    Optional<DiffDocument> findById(String id);

    /**
     * Create or update the given diff document.
     *
     * @param entity the diff document to be created or updated
     * @return the saved diff document
     */
    @Override
    DiffDocument save(DiffDocument entity);

}
