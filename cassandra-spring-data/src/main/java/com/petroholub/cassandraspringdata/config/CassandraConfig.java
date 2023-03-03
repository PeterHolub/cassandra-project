package com.petroholub.cassandraspringdata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

import java.util.List;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {
    private static final String KEY_SPACE_NAME = "localkeyspacespring";

    @Override
    protected String getKeyspaceName() {
        return KEY_SPACE_NAME;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return List.of(
                CreateKeyspaceSpecification.createKeyspace(KEY_SPACE_NAME)
                        .withSimpleReplication(2)
                        .ifNotExists()
        );
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.petroholub.cassandraspringdata.entity"};
    }
}
