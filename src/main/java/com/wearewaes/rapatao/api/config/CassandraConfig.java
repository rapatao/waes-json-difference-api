package com.wearewaes.rapatao.api.config;

import com.wearewaes.rapatao.api.ApiApp;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

/**
 * SPring Data Cassandra configuration.
 */
@Configuration
@ConfigurationProperties("spring.data.cassandra")
@Data
public class CassandraConfig extends AbstractCassandraConfiguration {

    private String keyspaceName;
    private Integer port;
    private List<String> contactPoints;

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected String getContactPoints() {
        return String.join(",", contactPoints);
    }

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{ApiApp.class.getPackage().getName()};
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification
                .createKeyspace(keyspaceName)
                .ifNotExists(true)
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication());
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

}
