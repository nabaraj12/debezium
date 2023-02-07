/**
 * 
 */
package com.ey.gds.debezium.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;

/**
 * @author JAYAS021
 *
 */
@Service
public class ApplicationContextServiceImpl implements ApplicationContextService,ApplicationContextAware{
	
	private ConfigurableApplicationContext context;
	
	private static final Logger LOGGER = LogManager.getLogger(ApplicationContextServiceImpl.class);
	
	@Override
	public void shutdownApplication() {
		if(context == null) {
			LOGGER.info("No context to shutdown");
		}else {
			ExecutorService singleThreadExecutor=(ExecutorService) context.getBean("singleThreadExecutor");
			DebeziumEngine<ChangeEvent<String, String>> debeziumEngine = (DebeziumEngine<ChangeEvent<String, String>>) context.getBean("debeziumEngine");
			try{
				debeziumEngine.close();
				context.close();
				if(!singleThreadExecutor.isShutdown()) {
					singleThreadExecutor.shutdown();
					while(!singleThreadExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
						LOGGER.info("Waiting another 5 seconds for the embedded engine to shut down");
					}
				}
			}catch (InterruptedException e) {
				LOGGER.error("Error while shutting down Debezium executir service : "+ ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Error while shutting down Debezium engine : "+ ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(applicationContext instanceof ConfigurableApplicationContext) {
			context = (ConfigurableApplicationContext) applicationContext;
		}
	}



	/* (non-Javadoc)
	 * @see com.ey.gds.debezium.service.ApplicationContextService#resumeDebeziumEngine()
	 */
	@Override
	public void resumeDebeziumEngine() {
		if(context == null || !context.isActive()) {
			LOGGER.info("No context to shutdown");
		}else {
			ExecutorService singleThreadExecutor=(ExecutorService) context.getBean("singleThreadExecutor");
			DebeziumEngine<ChangeEvent<String, String>> debeziumEngine = (DebeziumEngine<ChangeEvent<String, String>>) context.getBean("debeziumEngine");
			
			if(!singleThreadExecutor.isShutdown()) {
				System.out.println("Executer service is available");
			}
		}
	}

}
