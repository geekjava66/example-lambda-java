// Copyright (c) Keith D Gregory, all rights reserved
package com.kdgregory.example.javalambda.webapp;

import java.util.Map;

import com.kdgregory.example.javalambda.webapp.util.Tokens;


/**
 *  Holds data extracted from the original request. It will be passed to each of
 *  the service methods, and may have information added along the way.
 */
public class Request
{
    public enum HttpMethod { GET, POST }

    private String user;
    private HttpMethod method;
    private String action;
    private String amzTraceId;
    private Tokens tokens;
    private Map<String,Object> body;


    /**
     *  Base constructor, used to store the information provided in the proxy request.
     *  Note that user is not one of the parameters; it will be added by the auth code.
     *
     *  @param  method      The HTTP method used to invoke the request. Passed as a string
     *                      because that's what we get from the inbound request. Managed
     *                      as an enum because that's easier.
     *  @param  action      The request's action.
     *  @param  amzTraceId  Unique identifier inserted by load balancer, used to track
     *                      request through micro-services.
     *  @param  tokens      Authentication tokens provided with the request (name => value).
     *  @param  body        The request body; may be an empty map.
     */
    public Request(String method, String action, String amzTraceId, Tokens tokens, Map<String,Object> body)
    {
        this.method = method.toLowerCase().equals("get") ? HttpMethod.GET : HttpMethod.POST;
        this.action = action;
        this.amzTraceId = amzTraceId;
        this.tokens = tokens;
        this.body = body;
    }


//----------------------------------------------------------------------------
//  Getters
//----------------------------------------------------------------------------

    public HttpMethod getMethod()
    {
        return method;
    }


    public String getAction()
    {
        return action;
    }


    public String getAmzTraceId()
    {
        return amzTraceId;
    }


    public Tokens getTokens()
    {
        return tokens;
    }


    public Map<String,Object> getBody()
    {
        return body;
    }


    public String getUser()
    {
        return user;
    }


//----------------------------------------------------------------------------
//  Setters -- not all fields may be set
//----------------------------------------------------------------------------

    public void setTokens(Tokens value)
    {
        this.tokens = value;
    }


    public void setUser(String value)
    {
        user = value;
    }
}
