package fr.paris.lutece.plugins.identitystore.old.common;

import fr.paris.lutece.plugins.identitystore.business.application.ClientApplication;
import fr.paris.lutece.plugins.identitystore.business.application.ClientApplicationHome;
import fr.paris.lutece.plugins.identitystore.business.identity.IdentityConstants;
import fr.paris.lutece.plugins.identitystore.business.security.SecureMode;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.util.jwt.service.JWTUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static fr.paris.lutece.plugins.identitystore.business.security.SecureMode.JWT;
import static fr.paris.lutece.plugins.identitystore.business.security.SecureMode.NONE;

public class IdentityStoreService {

    private static final List<String> EXCEPTION_APP_CODES = Arrays
            .stream( AppPropertiesService.getProperty( "identitystore.header.application.code.verif.exception", "" ).split( "," ) )
            .collect( Collectors.toList( ) );

    /**
     * private constructor
     */
    private IdentityStoreService( )
    {
    }

    /**
     * Get the application code to use.<br/>
     *
     * @param strHeaderClientCode
     *            The application code in HTTP request header
     * @param strParamClientCode
     *            The application code provided by the client
     * @return The application code to use
     */
    public static String getTrustedClientCode( final String strHeaderClientCode, final String strParamClientCode ) throws IdentityStoreException
    {
        return getTrustedClientCode( strHeaderClientCode, strParamClientCode, StringUtils.EMPTY );
    }

    /**
     * Get the application code to use.<br/>
     * If the <code>strHeaderAppCode</code> is provided, the correlation between the resulting trusted client code and this application code is verified.
     *
     * @param strHeaderClientCode
     *            The application code in HTTP request header
     * @param strParamClientCode
     *            The application code provided by the client
     * @param strHeaderAppCode
     *            The application code header provided by the API manager
     * @return The application code to use
     * @see IdentityStoreService#verifyClientAndAppCodeCorrelation
     * @throws IdentityStoreException
     *             if the correlation between the resulting trusted client code and the application code was not verified
     */
    public static String getTrustedClientCode( final String strHeaderClientCode, final String strParamClientCode, final String strHeaderAppCode )
            throws IdentityStoreException
    {
        String trustedClientCode = StringUtils.EMPTY;
        // Secure mode
        switch( getSecureMode( ) )
        {
            case JWT:
            {
                if ( StringUtils.isNotBlank( strHeaderClientCode ) )
                {
                    String strJwtClaimAppCode = AppPropertiesService.getProperty( IdentityConstants.PROPERTY_JWT_CLAIM_APP_CODE );
                    trustedClientCode = JWTUtil.getPayloadValue( strHeaderClientCode.trim( ), strJwtClaimAppCode );
                }
                break;
            }
            case NONE:
            {
                if ( StringUtils.isNotBlank( strHeaderClientCode ) )
                {
                    trustedClientCode = strHeaderClientCode.trim( );
                }
                else
                {
                    if ( StringUtils.isNotBlank( strParamClientCode ) )
                    {
                        trustedClientCode = strParamClientCode.trim( );
                    }
                }
            }
        }
        verifyClientAndAppCodeCorrelation( trustedClientCode, strHeaderAppCode );
        return trustedClientCode;
    }

    /**
     * Get the secure Mode of the identitystore
     *
     * @return the secure mode
     */
    public static SecureMode getSecureMode( )
    {
        if ( AppPropertiesService.getProperty( IdentityConstants.PROPERTY_SECURE_MODE, StringUtils.EMPTY ).equals( "jwt" ) )
        {
            return SecureMode.JWT;
        }
        return SecureMode.NONE;

    }

    /**
     * Verify if the trusted client code is part of the provided client application.<br/>
     * If the application code is not provided, the verification is skipped.
     *
     * @param strTrustedClientCode
     *            the trusted client code
     * @param strHeaderAppCode
     *            the app code
     * @throws IdentityStoreException
     *             if the validation is not passing.
     */
    private static void verifyClientAndAppCodeCorrelation( final String strTrustedClientCode, final String strHeaderAppCode ) throws IdentityStoreException
    {
        if ( StringUtils.isBlank( strHeaderAppCode ) || EXCEPTION_APP_CODES.contains( strHeaderAppCode ) )
        {
            return;
        }
        final List<ClientApplication> clientApplicationList = ClientApplicationHome.findByApplicationCode( strHeaderAppCode );
        if ( clientApplicationList.stream( ).map( ClientApplication::getClientCode ).noneMatch( clientCode -> clientCode.equals( strTrustedClientCode ) ) )
        {
            throw new IdentityStoreException( "The provided client code and application code are not correlating." );
        }
    }
}
