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
package fr.paris.lutece.plugins.identitystore.v1.web.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.business.application.ClientApplicationHome;
import fr.paris.lutece.plugins.identitystore.service.identity.IdentityService;
import fr.paris.lutece.plugins.identitystore.v1.web.rs.DtoConverter;
import fr.paris.lutece.plugins.identitystore.v1.web.rs.IdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

/**
 * This class represents a get request for IdentityStoreRestServive
 */
public class IdentityStoreGetRequest extends IdentityStoreRequest
{

    private final String _strConnectionId;
    private final String _strCustomerId;
    private final String _strClientAppCode;
    private final ObjectMapper _objectMapper;

    /**
     * Constructor of IdentityStoreGetRequest
     *
     * @param strConnectionId
     *            the connectionId
     * @param strCustomerId
     *            the customerId
     * @param strClientAppCode
     *            the client app Code
     * @param objectMapper
     *            for json transformation
     */
    public IdentityStoreGetRequest( String strConnectionId, String strCustomerId, String strClientAppCode, ObjectMapper objectMapper )
    {
        super( );
        this._strConnectionId = strConnectionId;
        this._strCustomerId = strCustomerId;
        this._strClientAppCode = strClientAppCode;
        this._objectMapper = objectMapper;
    }

    /**
     * Valid the get request
     *
     */
    @Override
    protected void validRequest( )
    {
        IdentityRequestValidator.instance( ).checkIdentity( _strConnectionId, _strCustomerId );
        IdentityRequestValidator.instance( ).checkClientApplication( _strClientAppCode );
    }

    /**
     * get the identity
     *
     * @throws IdentityStoreException
     *             if there is an exception during the treatment
     */
    @Override
    protected String doSpecificRequest( ) throws IdentityStoreException
    {
        final IdentityDto result = IdentityService.instance( ).search( _strCustomerId, _strConnectionId,
                ClientApplicationHome.selectActiveServiceContract( _strClientAppCode ).stream( ).findFirst( ).orElse( null ), null );
        if ( result != null )
        {
            try
            {
                return _objectMapper.writeValueAsString( DtoConverter.convert( result ) );
            }
            catch( final JsonProcessingException e )
            {
                throw new IdentityStoreException( ERROR_JSON_MAPPING, e );
            }

        }
        return null;
    }

}
