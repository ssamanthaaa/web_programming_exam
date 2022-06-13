package it.units.rest.restapi;

import it.units.rest.filter.AuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;

//import it.units.rest.filter.AuthenticationFilter;

/**
 *  set the filter applications manually and not via web.xml
 */
public class RestApplicationConfig extends ResourceConfig {
	
	public RestApplicationConfig() {
        packages( "it.units.rest.filter" );
		register( AuthenticationFilter.class );
	}
} 