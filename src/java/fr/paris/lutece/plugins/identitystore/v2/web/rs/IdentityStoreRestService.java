/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identitystore.v2.web.rs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.paris.lutece.plugins.identitystore.service.IdentityStoreService;
import fr.paris.lutece.plugins.identitystore.v2.web.request.IdentityStoreGetRequest;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.identitystore.web.exception.ResourceNotFoundException;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST service for channel resource
 *
 */
@Path( RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.VERSION_PATH_V2 + Constants.IDENTITY_PATH )
public final class IdentityStoreRestService
{
    private static final String ERROR_NO_IDENTITY_FOUND = "No identity found";
    private static final String ERROR_NO_IDENTITY_TO_UPDATE = "no identity to update";
    private static final String ERROR_DURING_TREATMENT = "An error occurred during the treatment.";
    private final ObjectMapper _objectMapper;

    /**
     * private constructor
     */
    public IdentityStoreRestService( )
    {
        _objectMapper = new ObjectMapper( );
        _objectMapper.enable( SerializationFeature.INDENT_OUTPUT );
        _objectMapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
        _objectMapper.enable( DeserializationFeature.UNWRAP_ROOT_VALUE );
        _objectMapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
    }

    /**
     * Gives Identity from a connectionId or customerID either connectionId or customerId must be provided if connectionId AND customerId are provided, they
     * must be consistent otherwise an AppException is thrown
     *
     * @param strConnectionId
     *            connection ID
     * @param strCustomerId
     *            customerID
     * @param strHeaderClientAppCode
     *            client code
     * @param strQueryClientAppCode
     *            client code, will be removed, use Header parameter instead
     * @return the identity
     */
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response getIdentity( @QueryParam( Constants.PARAM_ID_CONNECTION ) String strConnectionId,
            @QueryParam( Constants.PARAM_ID_CUSTOMER ) String strCustomerId, @HeaderParam( Constants.PARAM_CLIENT_CODE ) String strHeaderClientAppCode,
            @QueryParam( Constants.PARAM_CLIENT_CODE ) String strQueryClientAppCode ) throws IdentityStoreException
    {
        String strClientAppCode = IdentityStoreService.getTrustedClientCode( strHeaderClientAppCode, strQueryClientAppCode );
        try
        {
            IdentityStoreGetRequest identityStoreRequest = new IdentityStoreGetRequest( strConnectionId, strCustomerId, strClientAppCode, _objectMapper );

            return Response.ok( identityStoreRequest.doRequest( ) ).build( );
        }
        catch( Exception exception )
        {
            return getErrorResponse( exception );
        }
    }

    /**
     * build error response from exception
     *
     * @param exception
     *            the exception
     * @return ResponseDto from exception
     */
    private Response getErrorResponse( Exception exception )
    {
        // For security purpose, send a generic message
        String strMessage;
        Response.StatusType status;

        AppLogService.debug( "IdentityStoreRestService getErrorResponse : " + exception.getMessage( ) );

        if ( exception instanceof ResourceNotFoundException )
        {
            strMessage = ERROR_NO_IDENTITY_FOUND;
            status = Response.Status.NOT_FOUND;
        }
        else
        {
            strMessage = ERROR_DURING_TREATMENT;
            status = Response.Status.BAD_REQUEST;
        }

        return buildResponse( strMessage, status );
    }

    /**
     * Builds a {@code Response} object from the specified message and status
     * 
     * @param strMessage
     *            the message
     * @param status
     *            the status
     * @return the {@code Response} object
     */
    private Response buildResponse( String strMessage, Response.StatusType status )
    {
        try
        {
            ResponseDto response = new ResponseDto( );
            response.setStatus( status.toString( ) );
            response.setMessage( strMessage );

            return Response.status( status ).type( MediaType.APPLICATION_JSON ).entity( _objectMapper.writeValueAsString( response ) ).build( );
        }
        catch( JsonProcessingException jpe )
        {
            return Response.status( status ).type( MediaType.TEXT_PLAIN ).entity( strMessage ).build( );
        }
    }
}
