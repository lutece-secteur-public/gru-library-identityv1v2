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
package fr.paris.lutece.plugins.identitystore.v2.web.rs.swagger;

/**
 * Rest Constants
 */
public final class Constants
{
    public static final String API_PATH = "identitystore/api";
    public static final String VERSION_PATH = "/v{" + Constants.VERSION + "}";
    public static final String ID_PATH = "/{" + Constants.ID + "}";
    public static final String FILE_NAME_PATH = "/{" + Constants.FILE_NAME + ": .*\\.*}";
    public static final String FILE_NAME = "filename";
    public static final String VERSION = "version";
    public static final String ID = "id";
    public static final String SWAGGER_DIRECTORY_PATH = "/plugins/";
    public static final String SWAGGER_PATH = "/swagger";
    public static final String SWAGGER_VERSION_PATH = "/v";
    public static final String SWAGGER_REST_PATH = "rest/";
    public static final String SWAGGER_JSON = "/swagger.json";
    public static final String EMPTY_OBJECT = "{}";
    public static final String ERROR_NOT_FOUND_VERSION = "Version not found";
    public static final String ERROR_NOT_FOUND_RESOURCE = "Resource not found";
    public static final String ERROR_BAD_REQUEST_EMPTY_PARAMETER = "Empty parameter";

    public static final String SERVICECONTRACT_PATH = "/servicecontracts";
    public static final String SERVICECONTRACT_ATTRIBUTE_NAME = "name";
    public static final String SERVICECONTRACT_ATTRIBUTE_APPLICATION_CODE = "application_code";
    public static final String SERVICECONTRACT_ATTRIBUTE_ORGANIZATIONAL_ENTITY = "organizational_entity";
    public static final String SERVICECONTRACT_ATTRIBUTE_RESPONSIBLE_NAME = "responsible_name";
    public static final String SERVICECONTRACT_ATTRIBUTE_CONTACT_NAME = "contact_name";
    public static final String SERVICECONTRACT_ATTRIBUTE_SERVICE_TYPE = "service_type";
    public static final String SERVICECONTRACT_ATTRIBUTE_AUTHORIZED_READ = "authorized_read";
    public static final String SERVICECONTRACT_ATTRIBUTE_AUTHORIZED_DELETION = "authorized_deletion";
    public static final String SERVICECONTRACT_ATTRIBUTE_AUTHORIZED_SEARCH = "authorized_search";
    public static final String SERVICECONTRACT_ATTRIBUTE_AUTHORIZED_IMPORT = "authorized_import";
    public static final String SERVICECONTRACT_ATTRIBUTE_AUTHORIZED_EXPORT = "authorized_export";

    public static final String REFATTRIBUTECERTIFICATIONLEVEL_PATH = "/refattributecertificationlevels";

    public static final String REFATTRIBUTECERTIFICATIONPROCESSUS_PATH = "/refattributecertificationprocessuss";

    public static final String REFCERTIFICATIONLEVEL_PATH = "/refcertificationlevels";
    public static final String REFCERTIFICATIONLEVEL_ATTRIBUTE_NAME = "name";
    public static final String REFCERTIFICATIONLEVEL_ATTRIBUTE_DESCRIPTION = "description";
    public static final String REFCERTIFICATIONLEVEL_ATTRIBUTE_LEVEL = "level";

    /**
     * private constructor
     */
    private Constants( )
    {

    }
}
