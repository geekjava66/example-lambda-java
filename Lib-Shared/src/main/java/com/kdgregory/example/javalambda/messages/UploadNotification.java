// Copyright (c) Keith D Gregory, all rights reserved
package com.kdgregory.example.javalambda.messages;


/**
 *  Used to notify back-end services that a new photo has been uploaded.
 *  This is a simple structure that will be accessed directly by the app.
 */
public class UploadNotification
{
    public String username;
    public String photoId;
    public String traceId;


    /**
     *  Creates an instance from the three components.
     */
    public UploadNotification(String username, String photoId, String traceId)
    {
        this.username = username;
        this.photoId = photoId;
        this.traceId = traceId;
    }


    /**
     *  Creates an instance from a string representation (normally extracted
     *  from an SNS message).
     */
    public UploadNotification(String message)
    {
        String[] parts = message.split("\n");
        username = parts[0];
        photoId = parts[1];
        traceId = parts[2];
    }


    /**
     *  Converts an instance to a string (normally preparatory to sending as
     *  an SNS message).
     */
    @Override
    public String toString()
    {
        return username + "\n" + photoId + "\n" + traceId;
    }
}
