package org.openhab.binding.openems.internal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerCallback;
import org.openhab.core.types.RefreshType;

@ExtendWith(MockitoExtension.class)
public class OpenEMSHandlerTest {

    private ThingHandler handler;

    @Mock
    private ThingHandlerCallback callbackMock;

    @Mock
    private Thing thingMock;

    @Mock
    private URLConnection connection;

    @Mock
    private ChannelUID channelUID;

    @BeforeEach
    public void setUp() {

        reset(callbackMock);
        handler = new OpenEMSHandler(thingMock);
        handler.setCallback(callbackMock);
    }

    @AfterEach
    public void tearDown() {
        // Free any resources, like open database connections, files etc.
        handler.dispose();
    }

    @Test
    public void initialize_setStatusTo_UNKNOWN() throws IOException {
        lenient().when(thingMock.getConfiguration()).thenReturn(new OpenEMSConfiguration());
        try (InputStream response = getClass().getClassLoader()
                .getResourceAsStream("org.openhab.binding.openems.internal/response.json")) {
            lenient().when(connection.getInputStream()).thenReturn(response);
            handler.initialize();

            verify(callbackMock).statusUpdated(eq(thingMock),
                    argThat(arg -> arg.getStatus().equals(ThingStatus.UNKNOWN)));
        }
    }

    @Test
    public void test() throws Exception {
        clearInvocations(callbackMock);
        Mockito.mockConstruction(URL.class, (mock, context) -> {
            lenient().when(mock.openConnection()).thenReturn(connection);
        });
        when(channelUID.getId()).thenReturn("sum-GridActivePower");
        when(callbackMock.isChannelLinked(channelUID)).thenReturn(true);

        try (InputStream response = new FileInputStream("src/test/resources/response.json")) {
            lenient().when(connection.getInputStream()).thenReturn(response);
            handler.handleCommand(channelUID, RefreshType.REFRESH);

            verify(callbackMock).statusUpdated(eq(thingMock),
                    argThat(arg -> arg.getStatus().equals(ThingStatus.ONLINE)));
            verify(callbackMock).stateUpdated(eq(channelUID), argThat(arg -> arg.toString().equals("yeah")));
        }
        Assertions.assertTrue(true);
    }
}
