package com.petroholub.cassandraspringdata.repository;

import com.petroholub.cassandraspringdata.entity.ApplicationLog;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationLogRepository extends CassandraRepository<ApplicationLog, String> {
    List<ApplicationLog> findApplicationLogsByApplicationName(String applicationName);
}