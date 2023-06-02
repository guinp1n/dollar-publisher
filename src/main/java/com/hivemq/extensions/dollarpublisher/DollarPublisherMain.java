package com.hivemq.extensions.dollarpublisher;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.packets.general.Qos;
import com.hivemq.extension.sdk.api.parameter.*;
import com.hivemq.extension.sdk.api.services.ManagedExtensionExecutorService;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.builder.Builders;
import com.hivemq.extension.sdk.api.services.builder.PublishBuilder;
import com.hivemq.extension.sdk.api.services.publish.PublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This extension publishes a message to topic that starts with "$".
 *
 * @author Dasha Samkova
 * @since 4.15
 */
public class DollarPublisherMain implements ExtensionMain {

    private static final @NotNull Logger log = LoggerFactory.getLogger(DollarPublisherMain.class);

    @Override
    public void extensionStart(
            final @NotNull ExtensionStartInput extensionStartInput,
            final @NotNull ExtensionStartOutput extensionStartOutput) {

        try {
            final PublishBuilder publishBuilder = Builders.publish();
            final ByteBuffer payload = ByteBuffer.wrap("message".getBytes());
            // Build the publish
            publishBuilder.topic("$events/are/here").qos(Qos.AT_LEAST_ONCE).payload(payload);
            // Access the Publish Service
            final PublishService publishService = Services.publishService();

            final ManagedExtensionExecutorService executorService = Services.extensionExecutorService();

            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    log.info("1 minute passed. Will try to publish.");

                    final CompletableFuture<Void> future = publishService.publish(publishBuilder.build());

                    future.whenComplete((aVoid, throwable) -> {
                        if(throwable == null) {
                            log.info("Publish sent successfully.");
                        } else {
                            //please use more sophisticated logging
                            throwable.printStackTrace();
                        }
                    });
                }
            }, 1, 1, TimeUnit.MINUTES);

            final ExtensionInformation extensionInformation = extensionStartInput.getExtensionInformation();
            log.info("Started " + extensionInformation.getName() + ":" + extensionInformation.getVersion());

        } catch (final Exception e) {
            log.error("Exception thrown at extension start: ", e);
        }
    }

    private byte @NotNull [] getRandomBytes(final int size) {
        final byte[] bytes = new byte[size];
        final Random random = new Random();
        random.nextBytes(bytes);
        return bytes;
    }

    @Override
    public void extensionStop(
            final @NotNull ExtensionStopInput extensionStopInput,
            final @NotNull ExtensionStopOutput extensionStopOutput) {

        final ExtensionInformation extensionInformation = extensionStopInput.getExtensionInformation();
        log.info("Stopped " + extensionInformation.getName() + ":" + extensionInformation.getVersion());
    }




}