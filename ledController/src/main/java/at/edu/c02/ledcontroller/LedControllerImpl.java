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
    public void turnOffAllLeds() throws IOException, InterruptedException {
        JSONArray res = this.getGroupLeds();
        for(int i=0; i<res.length(); i++) {
            JSONObject obj =  res.getJSONObject(i);
            apiService.setLight(obj.getInt("id"), false);
            Thread.sleep(1000);
        }
    }

    @Override
    public void laufLicht(String color, Integer turns) throws IOException, InterruptedException {
        this.turnOffAllLeds();
        JSONArray groupLeds = this.getGroupLeds();
        for(int i=0; i<turns; i++) {
            for(int j=0;j<groupLeds.length(); j++) {
                Integer id = groupLeds.getJSONObject(j).getInt("id");
                apiService.setLight(id, true, color);
                Thread.sleep(1000);
                apiService.setLight(id, false);
            }
        }
    }
}
