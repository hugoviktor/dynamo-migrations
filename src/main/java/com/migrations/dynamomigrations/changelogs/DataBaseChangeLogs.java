package com.migrations.dynamomigrations.changelogs;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.*;
import com.github.dynamobee.changeset.ChangeLog;
import com.github.dynamobee.changeset.ChangeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ChangeLog
public class DataBaseChangeLogs {

    @ChangeSet(order = "001", id = "someChangeId", author = "VictorHugo")
    public void createTable(AmazonDynamoDB amazonDynamoDB) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));

        List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName("accounts")
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(6L));

        CreateTableResult result = amazonDynamoDB.createTable(request);
        // task implementation
    }

    @ChangeSet(order = "002", id = "121120201043", author = "VictorHugo")
    public void addIndex(AmazonDynamoDB amazonDynamoDB) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Customer").withAttributeType("N"));
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
                .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(6L);

        GlobalSecondaryIndexUpdate indexUpdate = new GlobalSecondaryIndexUpdate()
                .withCreate(new CreateGlobalSecondaryIndexAction().withIndexName("customer-idx")
                        .withKeySchema(new KeySchemaElement().withAttributeName("Customer").withKeyType(KeyType.HASH))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                        .withProvisionedThroughput(provisionedThroughput)
                );

        UpdateTableRequest updateTableRequest = new UpdateTableRequest()
                .withTableName("accounts")
                .withAttributeDefinitions(attributeDefinitions)
                .withGlobalSecondaryIndexUpdates(indexUpdate);

        amazonDynamoDB.updateTable(updateTableRequest);
    }

    @ChangeSet(order = "003", id = "121120201130", author = "VictorHugo")
    public void addElement(AmazonDynamoDB amazonDynamoDB) {
        HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();

        // Add all content to the table
        itemValues.put("Id", new AttributeValue().withN("123456"));
        itemValues.put("Customer", new AttributeValue().withN("1143123285"));

        PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName("accounts")
                .withItem(itemValues);


        amazonDynamoDB.putItem(putItemRequest);
    }

    @ChangeSet(order = "004", id = "121120201137", author = "VictorHugo")
    public void addElementForXReason(AmazonDynamoDB amazonDynamoDB) {
        HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();

        // Add all content to the table
        itemValues.put("Id", new AttributeValue().withN("363636"));
        itemValues.put("Customer", new AttributeValue().withN("2233669988"));
        itemValues.put("Currency", new AttributeValue().withS("USD"));

        PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName("accounts")
                .withItem(itemValues);
        amazonDynamoDB.putItem(putItemRequest);
    }


    @ChangeSet(order = "004", id = "121120201149", author = "VictorHugo")
    public void updateElement(AmazonDynamoDB amazonDynamoDB) {
        HashMap<String, AttributeValue> itemKey = new HashMap<String, AttributeValue>();
        itemKey.put("Id", new AttributeValue().withN("363636"));
        HashMap<String, AttributeValueUpdate> updatedValues =
                new HashMap<String, AttributeValueUpdate>();
        // Update the column specified by name with updatedVal
        updatedValues.put("Currency", new AttributeValueUpdate().withValue(new AttributeValue().withS("COP")).withAction(AttributeAction.PUT));
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName("accounts")
                .withKey(itemKey)
                .withAttributeUpdates(updatedValues);
        amazonDynamoDB.updateItem(request);
    }


}
