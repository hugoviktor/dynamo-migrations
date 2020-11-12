package com.migrations.dynamomigrations;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.dynamobee.Dynamobee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DynamoMigrationsApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(DynamoMigrationsApplication.class);

    @Autowired
    AmazonDynamoDB dynamoDB;

    public static void main(String[] args) {

        LOG.info("STARTING THE MIGRATIONS");
        SpringApplication.run(DynamoMigrationsApplication.class, args);
        LOG.info("MIGRATIONS FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        Dynamobee runner = new Dynamobee(dynamoDB); //DynamoDB Client: see com.amazonaws.services.dynamodbv2.AmazonDynamoDB
        runner.setChangeLogsScanPackage(
                "com.migrations.dynamomigrations.changelogs"); // package to scan for changesets

        runner.setChangelogTableName("changelogs");
        runner.execute();

    }
}
