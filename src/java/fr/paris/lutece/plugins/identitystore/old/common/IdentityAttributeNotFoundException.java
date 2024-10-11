package fr.paris.lutece.plugins.identitystore.old.common;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppLogService;

public class IdentityAttributeNotFoundException extends IdentityStoreException
{

    /**
     * Constructor
     *
     * @param strMessage
     *            The message
     */
    public IdentityAttributeNotFoundException( String strMessage )
    {
        super( strMessage );
        AppLogService.error( strMessage );
    }
}
