// Copyright (c) Keith D Gregory, all rights reserved
package com.kdgregory.example.javalambda.photomanager;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import com.kdgregory.example.javalambda.photomanager.PhotoMetadata.Sizes;


/**
 *  This is the service interface for the photo manager; it provides operations to
 *  upload photos and retrieve a list of photos.
 */
public class PhotoManager
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    private AmazonS3Client s3Client;
    private AmazonDynamoDBClient ddbClient;

    private String ddbTableName;
    private String s3BucketName;
    private String s3ImagePrefix;


    public PhotoManager(String ddbTableName, String s3BucketName, String s3ImagePrefix)
    {
        s3Client = new AmazonS3Client();
        ddbClient = new AmazonDynamoDBClient();
        this.ddbTableName = ddbTableName;
        this.s3BucketName = s3BucketName;
        this.s3ImagePrefix = s3ImagePrefix;
    }


//----------------------------------------------------------------------------
//  Public methods
//----------------------------------------------------------------------------

    /**
     *  Retrieves a list of photos belonging to the specified user.
     */
    public List<PhotoMetadata> list(String user)
    {
        List<PhotoMetadata> result = new ArrayList<PhotoMetadata>();

        Map<String,AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":username", new AttributeValue().withS(user));
        QueryRequest request = new QueryRequest()
                               .withTableName(ddbTableName)
                               .withKeyConditionExpression(PhotoMetadata.Fields.USERNAME + " = :username")
                               .withExpressionAttributeValues(expressionValues);

        boolean more = false;
        do
        {
            QueryResult response = ddbClient.query(request);
            logger.debug("retrieved {} items", response.getCount());
            for (Map<String,AttributeValue> item : response.getItems())
            {
                result.add(PhotoMetadata.fromDynamoMap(item));
            }

            // the documentation says this will be empty if there are no more records to retrieve,
            // but in reality it's null ... so I'll handle both
            Map<String,AttributeValue> lastEvaluatedKey = response.getLastEvaluatedKey();
            more = (lastEvaluatedKey != null) && (lastEvaluatedKey.size() > 0);
            request.setExclusiveStartKey(lastEvaluatedKey);
        } while (more);

        return result;
    }


    /**
     *  Uploads a photo with the given metadata.
     *
     *  @return The ID of the photo.
     */
    public boolean upload(PhotoMetadata metadata, byte[] content)
    {
        if (!metadata.isValid())
        {
            logger.warn("upload called with invalid metadata: " + metadata);
            return false;
        }

        ObjectMetadata s3Meta = new ObjectMetadata();
        s3Meta.setContentLength(content.length);
        s3Meta.setContentType(metadata.getMimetype());
        PutObjectResult s3Response = s3Client.putObject(
                                        s3BucketName,
                                        s3ImagePrefix + "/" + metadata.getId() + "/" + Sizes.FULLSIZE.name(),
                                        new ByteArrayInputStream(content),
                                        s3Meta);

        logger.debug("upload successful, etag = " + s3Response.getETag());

        ddbClient.putItem(ddbTableName, metadata.toDynamoMap());

        return true;
    }
}