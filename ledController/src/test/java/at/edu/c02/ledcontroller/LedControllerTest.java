package at.edu.c02.ledcontroller;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class LedControllerTest {
    /**
     * This test is just here to check if tests get executed. Feel free to delete it when adding your own tests.
     * Take a look at the stack calculator tests again if you are unsure where to start.
     */
    @Test
    public void dummyTest() {
        assertEquals(1, 1);
    }

    @Test
    public void getGroupLedsTest() throws IOException {
        LedController ledController = mock(LedController.class);
        ledController.getGroupLeds();
        verify(ledController).getGroupLeds();
        verifyNoMoreInteractions(ledController);
    }

    @Test
    public void e2eSetLights() throws IOException {

        ApiService api = new ApiServiceImpl();
        api.setLight(48, true, "#0f0");

        assertEquals(
                "#0f0",
                api.getLight(48).getJSONArray("lights").getJSONObject(0).getString("color")
        );
        assertTrue(api.getLight(48).getJSONArray("lights").getJSONObject(0).getBoolean("on"));

        api.setLight(48, false);

        assertEquals(
                "#0f0",
                api.getLight(48).getJSONArray("lights").getJSONObject(0).getString("color")
        );
        assertFalse(api.getLight(48).getJSONArray("lights").getJSONObject(0).getBoolean("on"));

    }

    @Test
    public void turnOffAllLightsTest() throws IOException, InterruptedException {
        LedController ledController = mock(LedController.class);
        ledController.turnOffAllLeds();
        verify(ledController).turnOffAllLeds();
        verifyNoMoreInteractions(ledController);
    }

    @Test
    public void laufLichtTest() throws IOException, InterruptedException {
        LedController ledController = mock(LedController.class);
        ledController.laufLicht("#f00", 3);
        verify(ledController).laufLicht("#f00", 3);
        verifyNoMoreInteractions(ledController);
    }

    @Test
    public void spinningWheelEffectTest() throws IOException, InterruptedException {
        LedController ledController = mock(LedController.class);
        ledController.spinningWheelEffect( 3);
        verify(ledController).spinningWheelEffect( 3);
        verifyNoMoreInteractions(ledController);
    }
}
