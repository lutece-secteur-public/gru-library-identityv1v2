package fr.paris.lutece.plugins.identitystore.old.common;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

public class IdentityNotFoundException extends IdentityStoreException {
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     *
     * @param strError
     */
    public IdentityNotFoundException( String strError )
    {
        super( strError );
    }

    /**
     * @param strError
     *            error message
     * @param error
     *            error exception
     */
    public IdentityNotFoundException( String strError, Exception error )
    {
        super( strError, error );
    }
}
