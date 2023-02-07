/**
 * 
 */
package com.ey.gds.debezium.config;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import com.ey.gds.debezium.KafkaServices.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ey.gds.debezium.service.ApplicationContextService;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;

/**
 * @author Sreedhara.Jayadevamu
 *
 *Set below mentioned properties in mysql/mariadb server property file (./data/my.ini)
 * 
 * binlog_format=ROW
   sync_binlog=1
   log_bin=ON
 *
 */

@Configuration
public class MySQLDebeziumEngine {

//	@Autowired
//	ProducerService producerService;
	
	@Value("${engine.name}")
	private String engineName;

	@Value("${engine.offset.storage}")
	private String engineOffsetStorage;

	@Value("${engine.offset.storage.file.filename}")
	private String engineOffsetStorageFileName;

	@Value("${engine.offset.flush.interval.ms}")
	private String engineOffsetFlushInterval;

	@Value("${connector.database.hostname}")
	private String connDbHostName;

	@Value("${connector.database.port}")
	private String connDbPort;

	@Value("${connector.database.server.id}")
	private String connDbServerId;

	@Value("${connector.database.server.name}")
	private String connDbServerName;

	@Value("${connector.database.include.list}")
	private String connDbIncludeList;

	@Value("${connector.database.history}")
	private String connDbHistory;

	@Value("${connector.database.history.file.filename}")
	private String connDbHistoryFileName;

	@Value("${connector.database.history.skip.unparseable.ddl}")
	private String isSkipUnparseableDDL;

	@Value("${connector.database.history.store.only.monitored.tables.ddl}")
	private String isStoreOnlyMoniteredTblDDL;

	@Value("${connector.include.schema.changes}")
	private String isIncludeSchemaChanges;

	@Value("${connector.event.processing.failure.handling.mode}")
	private String eventProcessFailureHandlingMode;

	@Value("${connector.snapshot.mode}")
	private String snapshotMode;

	@Value("${source.database.username}")
	private String dbUserName;

	@Value("${source.database.password}")
	private String dbPassword;

	@Value("${connector.max.batch.size}")
	private String connectorMaxBatchSize;


	@Value("${engine.connector.class}")
	private String engineConnectorClass;



	@Autowired
	private ApplicationContextService applicationContextService;
	
	@Bean(name = "debeziumEngine")
	public DebeziumEngine<ChangeEvent<String, String>> debeziumEngine() {

		final Properties props = new Properties();
		props.setProperty("name", engineName);
		props.setProperty("connector.class", engineConnectorClass);
		props.setProperty("offset.storage", engineOffsetStorage);
		props.setProperty("offset.storage.file.filename", engineOffsetStorageFileName);
		props.setProperty("offset.flush.interval.ms", engineOffsetFlushInterval);
//		/* begin connector properties */
		props.setProperty("database.hostname", connDbHostName);
		props.setProperty("database.port", connDbPort);
		props.setProperty("database.user", dbUserName);
		props.setProperty("database.password", dbPassword);
		props.setProperty("database.server.id", connDbServerId);

		props.setProperty("database.server.name", connDbServerName);
		props.setProperty("database.include.list", connDbIncludeList);
		props.setProperty("database.history", connDbHistory);
		props.setProperty("database.history.file.filename", connDbHistoryFileName);

		props.setProperty("max.batch.size", connectorMaxBatchSize);

		props.setProperty("database.history.skip.unparseable.ddl", isSkipUnparseableDDL);
		props.setProperty("database.history.store.only.monitored.tables.ddl", isStoreOnlyMoniteredTblDDL);
		props.setProperty("event.processing.failure.handling.mode", eventProcessFailureHandlingMode);
		props.setProperty("include.schema.changes", isIncludeSchemaChanges);

		return DebeziumEngine.create(io.debezium.engine.format.Json.class).using(props).notifying(record -> {
			try {
				System.out.println("Value : "+record.value());
//				producerService.sendMessage(record.value());

				//throw new Exception("Intended exception");
			}catch(RuntimeException rex){
				System.out.println("Runtime exceptiom in Debezium engine");
				applicationContextService.shutdownApplication();
			}catch(Exception ex) {
				System.out.println("Exceptiom in Debezium engine");
				applicationContextService.shutdownApplication();
			}
		}).using(new DebeziumEngine.ConnectorCallback() {
			@Override
			public void connectorStopped() {
				System.out.println("Connectors stopped");
				applicationContextService.resumeDebeziumEngine();
			}
			@Override
			public void taskStopped() {
				System.out.println("Task stopped");
			}
		}).build();
			
		}
	
	@Bean(name = "singleThreadExecutor")
	public ExecutorService singleThreadExecutor() {
		return Executors.newSingleThreadExecutor();
	}
}
