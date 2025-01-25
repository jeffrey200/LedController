package at.edu.c02.ledcontroller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class handles the actual logic
 */
public class LedControllerImpl implements LedController {
    private final ApiService apiService;

    public LedControllerImpl(ApiService apiService)
    {
        this.apiService = apiService;
    }

    @Override
    public void demo() throws IOException
    {
        // Call `getLights`, the response is a json object in the form `{ "lights": [ { ... }, { ... } ] }`
        JSONObject response = apiService.getLights();
        // get the "lights" array from the response
        JSONArray lights = response.getJSONArray("lights");
        // read the first json object of the lights array
        JSONObject firstLight = lights.getJSONObject(0);
        // read int and string properties of the light
        System.out.println("First light id is: " + firstLight.getInt("id"));
        System.out.println("First light color is: " + firstLight.getString("color"));
    }

    @Override
    public JSONArray getGroupLeds() throws IOException {
        JSONObject res = apiService.getLights();
        JSONArray all = res.getJSONArray("lights");
        JSONArray groupdLeds = new JSONArray();
        for(int i=0; i<all.length(); i++) {
            JSONObject obj = all.getJSONObject(i);
            if(obj.getJSONObject("groupByGroup").getString("name").equals("G"))
                groupdLeds.put(obj);
        }
        return groupdLeds;
    }

    @Override
    public void getGroupStatus() throws IOException
    {
        JSONArray groupLeds = getGroupLeds();
        for(int i=0; i<groupLeds.length(); i++) {
            int id = groupLeds.getJSONObject(i).getInt("id");
            String color = groupLeds.getJSONObject(i).getString("color");
            String onOff = "off";
            if(groupLeds.getJSONObject(i).getBoolean("on")) {
                onOff = "on";
            }
            System.out.println("LED " + id + " is currently " + onOff + ". Color: " + color);
        }
    }

    @Override
    public void getStatus(int Id) throws IOException
    {
        JSONArray groupLeds = getGroupLeds();
        for(int i=0; i<groupLeds.length(); i++) {
            int id = groupLeds.getJSONObject(i).getInt("id");
            if(id != Id) {
                continue;
            }
            String color = groupLeds.getJSONObject(i).getString("color");
            String onOff = "off";
            if(groupLeds.getJSONObject(i).getBoolean("on")) {
                onOff = "on";
            }
            System.out.println("LED " + id + " is currently " + onOff + ". Color: " + color);
        }
    }
}
