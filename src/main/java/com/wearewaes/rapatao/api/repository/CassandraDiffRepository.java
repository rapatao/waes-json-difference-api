package com.wearewaes.rapatao.api.repository;

import com.wearewaes.rapatao.api.domain.DiffDocument;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CassandraDiffRepository extends DiffRepository, CassandraRepository<DiffDocument, String> {

    @Override
    Optional<DiffDocument> findById(String id);

    @Override
    DiffDocument save(DiffDocument entity);

}