// Copyright (c) Keith D Gregory, all rights reserved
package com.kdgregory.example.javalambda.messages;

import java.util.UUID;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestUploadNotification
{
    @Test
    public void test() throws Exception
    {
        final String expectedUsername = "nobody@example.com";
        final String expectedPhotoId  = UUID.randomUUID().toString();
        final String expectedTraceId  = "Self=1-67891234-12456789abcdef012345678;Root=1-67891233-abcdef012345678912345678"; // copied from AWS docs

        UploadNotification orig = new UploadNotification(expectedUsername, expectedPhotoId, expectedTraceId);
        assertEquals("username",    expectedUsername,   orig.username);
        assertEquals("photoId",     expectedPhotoId,    orig.photoId);
        assertEquals("username",    expectedTraceId,    orig.traceId);

        String asMessage = orig.toString();
        // FIXME assert message characters from SNS doc

        UploadNotification dest = new UploadNotification(asMessage);
        assertEquals("username",    expectedUsername,   dest.username);
        assertEquals("photoId",     expectedPhotoId,    dest.photoId);
        assertEquals("username",    expectedTraceId,    dest.traceId);
    }
}
