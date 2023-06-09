package org.openhab.binding.openems.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openhab.core.thing.Thing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

@ExtendWith(MockitoExtension.class)
public class JsonParseTest {

    @Mock
    private Thing thing;

    @Test
    public void testParse() throws FileNotFoundException, URISyntaxException {
        OpenEMSHandler handler = new OpenEMSHandler(thing);

        File f = new File(this.getClass().getClassLoader().getResource("jsonResult.json").toURI());
        InputStream stream = new FileInputStream(f);
        OpenEMSData[] result = handler.parseDataFromStream(stream);

        Assertions.assertNotNull(result);

    }

     @Test
    public void testKamel(){
         OpenEMSHandler handler = new OpenEMSHandler(thing);
         var result = handler.parseAddressToChannelId("_sum/EssActiveChargeEnergy");
         Assertions.assertNotNull(result);
         Assertions.assertEquals("grid-sell-active-energy", result);
    }
}
