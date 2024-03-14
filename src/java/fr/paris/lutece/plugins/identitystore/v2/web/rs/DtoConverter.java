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

import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.CertificateDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.IdentityDto;
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
     * returns a identityDto initialized from provided qualifiedIdentity
     *
     * @param qualifiedIdentity
     *            business qualifiedIdentity to convert
     * @return identityDto initialized from provided qualifiedIdentity
     */
    public static IdentityDto convert( final fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto qualifiedIdentity )
    {
        IdentityDto identityDto = new IdentityDto( );
        identityDto.setConnectionId( qualifiedIdentity.getConnectionId( ) );
        identityDto.setCustomerId( qualifiedIdentity.getCustomerId( ) );

        if ( qualifiedIdentity.getAttributes( ) != null )
        {
            Map<String, fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.AttributeDto> mapAttributeDto = new HashMap<>( );

            for ( final AttributeDto attributeDto : qualifiedIdentity.getAttributes( ) )
            {
                fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.AttributeDto attrDto = new fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.AttributeDto( );
                attrDto.setKey( attributeDto.getKey( ) );
                attrDto.setValue( attributeDto.getValue( ) );
                attrDto.setType( attributeDto.getType( ) );
                attrDto.setLastUpdateApplicationCode( attributeDto.getLastUpdateClientCode( ) );
                attrDto.setLastUpdateDate( attributeDto.getLastUpdateDate( ) );
                attrDto.setStatus( null ); // TODO n'existe pas en V3

                if ( StringUtils.isNotEmpty( attributeDto.getCertifier( ) ) )
                {
                    final CertificateDto certifDto = new CertificateDto( );
                    certifDto.setCertificateExpirationDate( null ); // TODO n'existe pas en V3
                    certifDto.setCertifierCode( attributeDto.getCertifier( ) );
                    certifDto.setCertifierName( attributeDto.getCertifier( ) );
                    certifDto.setCertifierLevel( attributeDto.getCertificationLevel( ) );
                    attrDto.setCertificate( certifDto );
                }
                attrDto.setCertified( attrDto.getCertificate( ) != null );

                mapAttributeDto.put( attrDto.getKey( ), attrDto );
            }

            identityDto.setAttributes( mapAttributeDto );
        }

        return identityDto;
    }
}
