package com.petroholub.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;

import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

public class CassandraApp {
    private static final String KEY_SPACE_NAME = "localkeyspace";
    private static final String TABLE_NAME = "application_logs";

    private static final String APP_NAME_COLUMN = "app_name";

    private static final String ENV_COLUMN = "env";

    private static final String ID_COLUMN = "id";

    private static final String LOG_DATETIME_COLUMN = "log_datetime";

    private static final String LOG_LEVEL_COLUMN = "log_level";

    private static final String LOG_MESSAGE_COLUMN = "log_message";

    public static void main(String[] args) {
        CassandraConnector cassandraConnector = new CassandraConnector();
        CqlSession cqlSession = cassandraConnector.getSession();
        System.out.println("Available Cassandra nodes nodes : " + cqlSession.getMetadata().getNodes());
        System.out.println("Creating keyspace");

        SimpleStatement createKeyspace = SchemaBuilder.createKeyspace(KEY_SPACE_NAME)
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true)
                .build();

        cqlSession.execute(createKeyspace);

        SimpleStatement createApplicationLogs = createTable(KEY_SPACE_NAME, TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(APP_NAME_COLUMN, DataTypes.TEXT)
                .withPartitionKey(ID_COLUMN, DataTypes.UUID)
                .withColumn(ENV_COLUMN, DataTypes.TEXT)
                .withColumn(LOG_DATETIME_COLUMN, DataTypes.DATE)
                .withColumn(LOG_LEVEL_COLUMN, DataTypes.TEXT)
                .withColumn(LOG_MESSAGE_COLUMN, DataTypes.TEXT)
                .build();

        cqlSession.execute(createApplicationLogs);

        System.out.println("Creating index for app_name column");

        SimpleStatement createIndex = createIndex()
                .ifNotExists()
                .onTable(KEY_SPACE_NAME, TABLE_NAME)
                .andColumn(APP_NAME_COLUMN)
                .build();

        cqlSession.execute(createIndex);

        SimpleStatement insertLog01 = insertInto(KEY_SPACE_NAME, TABLE_NAME)
                .value(ID_COLUMN, currentTimeUuid())
                .value(APP_NAME_COLUMN, literal("app01"))
                .value(ENV_COLUMN, literal("test"))
                .value(LOG_LEVEL_COLUMN, literal("ERROR"))
                .value(LOG_MESSAGE_COLUMN, literal("Application failed"))
                .value(LOG_DATETIME_COLUMN, literal("2013-03-02"))
                .ifNotExists()
                .build();

        SimpleStatement insertLog02 = insertInto(KEY_SPACE_NAME, TABLE_NAME)
                .value(ID_COLUMN, currentTimeUuid())
                .value(APP_NAME_COLUMN, literal("app02"))
                .value(ENV_COLUMN, literal("stage"))
                .value(LOG_LEVEL_COLUMN, literal("WARN"))
                .value(LOG_MESSAGE_COLUMN, literal("Its dangerous!"))
                .value(LOG_DATETIME_COLUMN, literal("2013-02-01"))
                .ifNotExists()
                .build();

        SimpleStatement insertLog03 = insertInto(KEY_SPACE_NAME, TABLE_NAME)
                .value(ID_COLUMN, currentTimeUuid())
                .value(APP_NAME_COLUMN, literal("app02"))
                .value(ENV_COLUMN, literal("prod"))
                .value(LOG_LEVEL_COLUMN, literal("ERROR"))
                .value(LOG_MESSAGE_COLUMN, literal("Total fail!"))
                .value(LOG_DATETIME_COLUMN, literal("2013-01-30"))
                .ifNotExists()
                .build();

        SimpleStatement insertLog04 = insertInto(KEY_SPACE_NAME, TABLE_NAME)
                .value(ID_COLUMN, currentTimeUuid())
                .value(APP_NAME_COLUMN, literal("app01"))
                .value(ENV_COLUMN, literal("prod"))
                .value(LOG_LEVEL_COLUMN, literal("ERROR"))
                .value(LOG_MESSAGE_COLUMN, literal("Super epic fail!"))
                .value(LOG_DATETIME_COLUMN, literal("2013-01-30"))
                .ifNotExists()
                .build();

        System.out.println("Writing test values into database");
        cqlSession.execute(insertLog01);
        cqlSession.execute(insertLog02);
        cqlSession.execute(insertLog03);
        cqlSession.execute(insertLog04);

        System.out.println("Getting all logs for app 02...");
        SimpleStatement getAllProdLogs = selectFrom(KEY_SPACE_NAME, TABLE_NAME).all()
                .where(Relation.column(APP_NAME_COLUMN)
                        .isEqualTo(literal("app02")))
                .build();

        List<Row> queriedRows = cqlSession.execute(getAllProdLogs).all();

        queriedRows.forEach(row -> System.out.println(row.getFormattedContents()));
        cqlSession.close();
    }
}
