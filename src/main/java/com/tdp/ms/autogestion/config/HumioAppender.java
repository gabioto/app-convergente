package com.tdp.ms.autogestion.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerAsyncClient;
import com.azure.messaging.eventhubs.models.CreateBatchOptions;


public class HumioAppender extends AppenderBase<ILoggingEvent> {

    private final String connectStr ="Endpoint=sb://ehub-appconvergente-humio-dev.servicebus.windows.net/;SharedAccessKeyName=develop;SharedAccessKey=kRrOZkM+HD6ABoIgNknkSHD5vhimHXz/gV0uWB15Yo4=;EntityPath=humio";
            //System.getenv("HUMIO_CONNECTION") != null ? System.getenv("HUMIO_CONNECTION") : "";
    private final String queueStatus ="true";
            //System.getenv("HUMIO_ACTIVE") != null ? System.getenv("HUMIO_ACTIVE") : "false";
    private final String applicationName ="ms-trazabilidad-autogestion";
            //System.getenv("HUMIO_APPLICATION_NAME") != null ? System.getenv("HUMIO_APPLICATION_NAME"): "";

    private EventHubProducerAsyncClient eventHubProducerClient;
    private PatternLayout patternLayout;

    private CreateBatchOptions options;
    private boolean activeSender = false;

    public HumioAppender() {
        log("Inicio HumioAppender");

        options = new CreateBatchOptions()
                .setMaximumSizeInBytes(25000);

        if (Boolean.parseBoolean(queueStatus)) {
            if ( !connectStr.isEmpty()) {
                log("Creating hub");
                patternLayout = new PatternLayout();
                patternLayout.setContext(new LoggerContext());
                patternLayout.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
                patternLayout.start();

                eventHubProducerClient = createHub();
                activeSender = true;
                log("Hub created!");
            }
        }
    }

    @Override
    protected void append(final ILoggingEvent event) {
        if(!activeSender) return;

        eventHubProducerClient.createBatch(options)
        .flatMap(batch -> {
            batch.tryAdd(new EventData(applicationName + " " + patternLayout.doLayout(event)));
            log("Enviando a event hub");
            return eventHubProducerClient.send(batch);
        }).subscribe(
                unused -> {},
                error -> log("Couldn't send logs, there's an error: " + error.getStackTrace()),
                () -> {
                    log("Send complete!");
                });
    }

    private EventHubProducerAsyncClient createHub() {
        return new EventHubClientBuilder()
                .connectionString(connectStr)
                .buildAsyncProducerClient();
    }

    private void log(String message) {
        System.out.println(message);
    }
}