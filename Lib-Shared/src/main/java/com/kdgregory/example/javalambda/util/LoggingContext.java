// Copyright (c) Keith D Gregory, all rights reserved
package com.kdgregory.example.javalambda.util;

import org.slf4j.MDC;

/**
 *  A set of wrapper functions over the mapped diagnostic context, to ensure
 *  that we use the same names for the same items (no typos!).
 */
public class LoggingContext
{
    /**
     *  Clears the MDC. This should be called at the end of handler function.
     */
    public static void clear()
    {
        MDC.clear();
    }


    /**
     *  Stores a unique trace ID (initially attached to the HTTP request),
     *  which can be used to correlate messages for the same photo across
     *  all micro-services.
     */
    public static void setTraceId(String value)
    {
        MDC.put("traceId", value);
    }


    /**
     *  Stores the Lambda request ID. This is used to correlate messages
     *  for a single Lambda execution.
     */
    public static void setRequestId(String value)
    {
        MDC.put("requestId", value);
    }


    /**
     *  Stores the dispatcher action. Only called by the WebApp dispatcher.
     */
    public static void setAction(String value)
    {
        MDC.put("action", value);
    }



    /**
     *  Stores the user associated with the request.
     */
    public static void setUsername(String value)
    {
        MDC.put("username", value);
    }
}
