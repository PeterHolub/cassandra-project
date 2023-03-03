package com.petroholub.cassandraspringdata.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("application_logs")
@Builder
@Data
public class ApplicationLog {
    @PrimaryKeyColumn(
            name = "app_name", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @Indexed
    private String applicationName;
    @PrimaryKeyColumn(
            name = "id", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private UUID id;
    @Column
    private String env;
    @Column("log_datetime")
    private LocalDate logDateTime;
    @Column("log_level")
    private String logLevel;
    @Column("log_message")
    private String logMessage;

}