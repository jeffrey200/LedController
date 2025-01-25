package at.edu.c02.ledcontroller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public interface LedController {
    void demo() throws IOException;
    JSONArray getGroupLeds() throws IOException;
    void getGroupStatus() throws IOException;
    void getStatus(int id) throws IOException;

    void turnOffAllLeds() throws IOException, InterruptedException;

    void laufLicht(String color, Integer turns) throws IOException, InterruptedException;

    void setLed(Integer id, String color) throws IOException;
    void spinningWheelEffect(int turns) throws IOException, InterruptedException;
}
