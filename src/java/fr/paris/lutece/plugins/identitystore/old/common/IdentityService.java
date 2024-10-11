package fr.paris.lutece.plugins.identitystore.old.common;

import fr.paris.lutece.plugins.identitystore.business.contract.ServiceContract;
import fr.paris.lutece.plugins.identitystore.business.identity.IdentityHome;
import fr.paris.lutece.plugins.identitystore.cache.IdentityDtoCache;
import fr.paris.lutece.plugins.identitystore.service.contract.ServiceContractService;
import fr.paris.lutece.plugins.identitystore.service.listeners.IdentityStoreNotifyListenerService;
import fr.paris.lutece.plugins.identitystore.service.user.InternalUserService;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.DtoConverter;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AuthorType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityChangeType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchMessage;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.security.AccessLogService;
import fr.paris.lutece.portal.service.security.AccessLoggerConstants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.http.SecurityUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;

public class IdentityService {

    // Conf
    private static final String PIVOT_CERTIF_LEVEL_THRESHOLD = "identitystore.identity.attribute.update.pivot.certif.level.threshold";
    private static final String PIVOT_UNCERTIF_LEVEL_THRESHOLD = "identitystore.identity.uncertify.attribute.pivot.level.threshold";

    // EVENTS FOR ACCESS LOGGING
    public static final String CREATE_IDENTITY_EVENT_CODE = "CREATE_IDENTITY";
    public static final String UPDATE_IDENTITY_EVENT_CODE = "UPDATE_IDENTITY";
    public static final String DECERTIFY_IDENTITY_EVENT_CODE = "DECERTIFY_IDENTITY";
    public static final String GET_IDENTITY_EVENT_CODE = "GET_IDENTITY";
    public static final String SEARCH_IDENTITY_EVENT_CODE = "SEARCH_IDENTITY";
    public static final String DELETE_IDENTITY_EVENT_CODE = "DELETE_IDENTITY";
    public static final String CONSOLIDATE_IDENTITY_EVENT_CODE = "CONSOLIDATE_IDENTITY";
    public static final String MERGE_IDENTITY_EVENT_CODE = "MERGE_IDENTITY";
    public static final String CANCEL_MERGE_IDENTITY_EVENT_CODE = "CANCEL_MERGE_IDENTITY";
    public static final String CANCEL_CONSOLIDATE_IDENTITY_EVENT_CODE = "CANCEL_CONSOLIDATE_IDENTITY";
    public static final String SPECIFIC_ORIGIN = "BO";

    // PROPERTIES
    private static final String PROPERTY_DUPLICATES_IMPORT_RULES_SUSPICION = "identitystore.identity.duplicates.import.rules.suspicion";
    private static final String PROPERTY_DUPLICATES_IMPORT_RULES_STRICT = "identitystore.identity.duplicates.import.rules.strict";
    private static final String PROPERTY_DUPLICATES_CREATION_RULES = "identitystore.identity.duplicates.creation.rules";
    private static final String PROPERTY_DUPLICATES_UPDATE_RULES = "identitystore.identity.duplicates.update.rules";
    private static final String PROPERTY_DUPLICATES_CHECK_DATABASE_ACTIVATED = "identitystore.identity.duplicates.check.database";

    // SERVICES
    private final IdentityStoreNotifyListenerService _identityStoreNotifyListenerService = IdentityStoreNotifyListenerService.instance( );
    private final ServiceContractService _serviceContractService = ServiceContractService.instance( );
    private final InternalUserService _internalUserService = InternalUserService.getInstance( );

    // CACHE
    private final IdentityDtoCache _identityDtoCache = SpringContextService.getBean( "identitystore.identityDtoCache" );

    private static IdentityService _instance;

    public static IdentityService instance( )
    {
        if ( _instance == null )
        {
            _instance = new IdentityService( );
        }
        return _instance;
    }

    /**
     * Perform an identity research by customer or connection ID.
     *
     * @param customerId
     * @param connectionId
     * @param response
     * @param clientCode
     * @param author
     *            the author of the request
     * @throws IdentityAttributeNotFoundException
     * @throws ServiceContractNotFoundException
     */
    public void search(final String customerId, final String connectionId, final IdentitySearchResponse response, final String clientCode,
                       final RequestAuthor author ) throws IdentityStoreException
    {
        AccessLogService.getInstance( ).info( AccessLoggerConstants.EVENT_TYPE_READ, GET_IDENTITY_EVENT_CODE, _internalUserService.getApiUser( clientCode ),
                SecurityUtil.logForgingProtect( StringUtils.isNotBlank( customerId ) ? customerId : connectionId ), SPECIFIC_ORIGIN );

        final ServiceContract serviceContract = _serviceContractService.getActiveServiceContract( clientCode );
        if ( serviceContract == null )
        {
            throw new ServiceContractNotFoundException( "No active service contract could be found for clientCode = " + clientCode );
        }
        final IdentityDto identityDto = StringUtils.isNotBlank( customerId ) ? _identityDtoCache.getByCustomerId( customerId, serviceContract )
                : _identityDtoCache.getByConnectionId( connectionId, serviceContract );
        if ( identityDto == null )
        {
            // #345 : If the identity doesn't exist, make an extra search in the history (only for CUID search).
            // If there is a record, it means the identity has been deleted => send back a specific message
            if ( StringUtils.isNotBlank( customerId ) && !IdentityHome.findHistoryByCustomerId( customerId ).isEmpty( ) )
            {
                response.setStatus( ResponseStatusFactory.notFound( ).setMessageKey( Constants.PROPERTY_REST_ERROR_IDENTITY_DELETED ) );
            }
            else
            {
                response.setStatus( ResponseStatusFactory.notFound( ).setMessageKey( Constants.PROPERTY_REST_ERROR_NO_IDENTITY_FOUND ) );
            }
        }
        else
        {
            response.setIdentities( Collections.singletonList( identityDto ) );
            response.setStatus( ResponseStatusFactory.ok( ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );
            // #27998 : Dans le cas d'une interrogation sur un CUID/GUID rapproché, ajouter une ligne dans le bloc "Alerte" dans la réponse de l'identité consolidée
            if ((StringUtils.isNotBlank(customerId) && !identityDto.getCustomerId().equals(customerId)) ||
                    (StringUtils.isNotBlank(connectionId) && !identityDto.getConnectionId().equals(connectionId))) {
                final IdentitySearchMessage alert = new IdentitySearchMessage();
                alert.setMessage("Le CUID ou GUID demandé correspond à une identité rapprochée. Cette réponse contient l'identité consilidée.");
                response.getAlerts().add(alert);
            }
            if ( author != null )
            {
                AccessLogService.getInstance( ).info( AccessLoggerConstants.EVENT_TYPE_READ, SEARCH_IDENTITY_EVENT_CODE,
                        _internalUserService.getApiUser( author, clientCode ), SecurityUtil.logForgingProtect( identityDto.getCustomerId( ) ),
                        SPECIFIC_ORIGIN );
            }
            if ( author != null && author.getType( ).equals( AuthorType.agent ) )
            {
                /* Indexation et historique */
                _identityStoreNotifyListenerService.notifyListenersIdentityChange( IdentityChangeType.READ, DtoConverter.convertDtoToIdentity( identityDto ),
                        response.getStatus( ).getType( ).name( ), response.getStatus( ).getMessage( ), author, clientCode, new HashMap<>( ) );
            }
        }
    }
}
