package at.edu.c02.ledcontroller;

import org.json.JSONObject;

import java.io.IOException;

public interface ApiService {
    JSONObject getLights() throws IOException;
    JSONObject getLight(Integer id) throws IOException;

    JSONObject setLight(Integer id, Boolean state) throws IOException;
    JSONObject setLight(Integer id, Boolean state, String color) throws IOException;

}
