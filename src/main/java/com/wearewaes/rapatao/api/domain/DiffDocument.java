package com.wearewaes.rapatao.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * An entity object that represents the diff document with both sides used in the comparison.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table("diff_document")
public class DiffDocument {

    /**
     * The diff document indentifier
     */
    @PrimaryKey
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String id;

    /**
     * The base64 encoded content for the left side of the diff document
     */
    private String left;

    /**
     * The base64 encoded content for the right side of the diff document
     */
    private String right;

}
