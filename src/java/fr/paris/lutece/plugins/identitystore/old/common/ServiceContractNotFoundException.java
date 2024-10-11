package fr.paris.lutece.plugins.identitystore.old.common;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppLogService;

public class ServiceContractNotFoundException extends IdentityStoreException
{

    /**
     * Constructor
     *
     * @param strMessage
     *            The message
     */
    public ServiceContractNotFoundException( String strMessage )
    {
        super( strMessage );
        AppLogService.error( strMessage );
    }
}
