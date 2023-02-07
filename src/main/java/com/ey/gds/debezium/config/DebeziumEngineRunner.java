/**
 * 
 */
package com.ey.gds.debezium.config;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;

/**
 * @author Sreedhara.Jayadevamu
 *
 */

@Component
public class DebeziumEngineRunner implements ApplicationRunner{
	
	@Autowired
	private ExecutorService singleThreadExecutor;
	
	@Autowired
	DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			singleThreadExecutor.execute(debeziumEngine); 
		}catch(RuntimeException rex) {
			System.out.println("#######");
		}
	}
}
