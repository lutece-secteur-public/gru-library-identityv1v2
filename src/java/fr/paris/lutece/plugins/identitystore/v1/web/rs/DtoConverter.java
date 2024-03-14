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
package fr.paris.lutece.plugins.identitystore.v1.web.rs;

import fr.paris.lutece.plugins.identitystore.v1.web.rs.dto.CertificateDto;
import fr.paris.lutece.plugins.identitystore.v1.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeDto;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * class to help managing rest feature
 *
 */
public final class DtoConverter
{
    /**
     * private constructor
     */
    private DtoConverter( )
    {
    }

    /**
     * Convert an v3Identity from V2 version to V1 version
     * 
     * @param v3Identity
     * @return identityDtoOldVersion from package v1
     */
    public static IdentityDto convert( final fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto v3Identity )
    {
        if ( v3Identity != null )
        {
            final IdentityDto v1Identity = new IdentityDto( );
            v1Identity.setConnectionId( v3Identity.getConnectionId( ) );
            v1Identity.setCustomerId( ( v3Identity.getCustomerId( ) ) );

            final Map<String, fr.paris.lutece.plugins.identitystore.v1.web.rs.dto.AttributeDto> newMapAttributeOldVersion = new HashMap<>( );

            for ( final AttributeDto attributeDto : v3Identity.getAttributes( ) )
            {
                newMapAttributeOldVersion.put( attributeDto.getKey( ), convertToAttributeDtoOldVersion( attributeDto ) );
            }

            v1Identity.setAttributes( newMapAttributeOldVersion );

            return v1Identity;
        }

        return null;
    }

    /**
     * Convert an AttributesDto from V2 version to V1 version
     * 
     * @param attributeDto
     * @return certifiedAttribute from package v1
     */
    public static fr.paris.lutece.plugins.identitystore.v1.web.rs.dto.AttributeDto convertToAttributeDtoOldVersion( final AttributeDto attributeDto )
    {
        final fr.paris.lutece.plugins.identitystore.v1.web.rs.dto.AttributeDto attributeDtoOldVersion = new fr.paris.lutece.plugins.identitystore.v1.web.rs.dto.AttributeDto( );

        if ( attributeDto != null )
        {
            attributeDtoOldVersion.setCertificate( convertToCertificateDtoOldVersion( attributeDto ) );
            attributeDtoOldVersion.setCertified( attributeDtoOldVersion.getCertificate( ) != null );
            attributeDtoOldVersion.setKey( attributeDto.getKey( ) );
            attributeDtoOldVersion.setType( attributeDto.getType( ) );
            attributeDtoOldVersion.setValue( attributeDto.getValue( ) );

            return attributeDtoOldVersion;
        }
        else
        {
            return null;
        }
    }

    public static CertificateDto convertToCertificateDtoOldVersion( final AttributeDto attributeDto )
    {
        final CertificateDto certificateDtoOldVersion = new CertificateDto( );

        if ( attributeDto != null && StringUtils.isNotEmpty( attributeDto.getCertifier( ) ) )
        {
            certificateDtoOldVersion.setCertificateExpirationDate( null ); // TODO n'existe pas en V3
            certificateDtoOldVersion.setCertifierCode( attributeDto.getCertifier( ) );
            certificateDtoOldVersion.setCertifierLevel( attributeDto.getCertificationLevel( ) );
            certificateDtoOldVersion.setCertifierName( attributeDto.getCertifier( ) );

            return certificateDtoOldVersion;
        }

        return null;
    }
}
