package com.petroholub.cassandraspringdata;

import com.petroholub.cassandraspringdata.entity.ApplicationLog;
import com.petroholub.cassandraspringdata.repository.ApplicationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootApplication()
@EnableCassandraRepositories
public class CassandraSpringDataApplication implements CommandLineRunner {
    @Autowired
    private ApplicationLogRepository applicationLogRepository;

    public static void main(String[] args) {
        SpringApplication.run(CassandraSpringDataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.sleep(1000);
        System.out.println("Inserting logs to ApplicationLog repository....");

        ApplicationLog applicationLog01 = ApplicationLog.builder()
                .id(UUID.randomUUID())
                .applicationName("app01")
                .env("test")
                .logLevel("ERROR")
                .logMessage("Application failed")
                .logDateTime(LocalDate.now())
                .build();

        ApplicationLog applicationLog02 = ApplicationLog.builder()
                .id(UUID.randomUUID())
                .applicationName("app02")
                .env("stage")
                .logLevel("WARN")
                .logMessage("Its dangerous!")
                .logDateTime(LocalDate.now())
                .build();

        ApplicationLog applicationLog03 = ApplicationLog.builder()
                .id(UUID.randomUUID())
                .applicationName("app02")
                .env("prod")
                .logLevel("ERROR")
                .logMessage("Total fail!")
                .logDateTime(LocalDate.now())
                .build();

        applicationLogRepository.saveAll(List.of(applicationLog01, applicationLog02, applicationLog03));

        System.out.println("Getting all app02 logs...");
        List<ApplicationLog> applicationLogs =
                applicationLogRepository.findApplicationLogsByApplicationName("app02");

        applicationLogs.forEach(System.out::println);

        System.out.println("Shutting down the application");
        System.exit(0);
    }
}
