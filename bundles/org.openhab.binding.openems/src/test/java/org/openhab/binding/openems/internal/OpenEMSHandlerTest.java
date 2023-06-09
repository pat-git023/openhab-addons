package org.openhab.binding.openems.internal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenEMSHandlerTest {

    private ThingHandler handler;

    private @Mock ThingHandlerCallback callbackMock;
    private @Mock Thing thingMock;

    private @Mock URLConnection connection;

    @BeforeEach
    public void setUp() {

        Mockito.mockConstruction(URL.class, (mock, context) -> {
                    when(mock.openConnection()).thenReturn(connection);
                }
        );
        handler = new OpenEMSHandler(thingMock);
        handler.setCallback(callbackMock);
    }

    @AfterEach
    public void tearDown() {
        // Free any resources, like open database connections, files etc.
        handler.dispose();
    }

    @Test
    public void handleCommand() throws IOException {
        try (InputStream response = getClass().getClassLoader().getResourceAsStream("response.json")) {
            when(connection.getInputStream()).thenReturn(response);
            // we expect the handler#initialize method to call the callbackMock during execution and
            // pass it the thingMock and a ThingStatusInfo object containing the ThingStatus of the thingMock.
            handler.initialize();

            // verify the interaction with the callbackMock.
            // Check that the ThingStatusInfo given as second parameter to the callbackMock was build with the ONLINE status:
            verify(callbackMock).statusUpdated(eq(thingMock), argThat(arg -> arg.getStatus().equals(ThingStatus.ONLINE)));
        }
    }

}

