/**
 * 
 */
package com.ey.gds.debezium.service;

/**
 * @author Sreedhara.Jayadevamu
 *
 */
public interface ApplicationContextService {
	
	public void shutdownApplication();
	
	public void resumeDebeziumEngine();
	
}
