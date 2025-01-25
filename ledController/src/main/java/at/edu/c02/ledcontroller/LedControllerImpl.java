package at.edu.c02.ledcontroller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

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

    @Override
    public void setLed(Integer id, String color) throws IOException {
        apiService.setLight(id, true, color);
    }

    @Override
    public void spinningWheelEffect(int turns) throws IOException, InterruptedException
    {
        for(int i=0;i<turns; i++) {
            JSONArray groupLeds = this.getGroupLeds();
            String[] colors = new String[groupLeds.length()];
            Boolean[] onOffs = new Boolean[groupLeds.length()];
            for(int j=0; j<groupLeds.length(); j++) {
                String color = groupLeds.getJSONObject(j).getString("color");
                colors[j] = color;
                onOffs[j] = groupLeds.getJSONObject(j).getBoolean("on");
            }
            String[] newColors = new String[colors.length];
            Boolean[] newOnOffs = new Boolean[onOffs.length];
            for(int j=0; j<colors.length-1; j++) {
                newColors[j] = colors[j+1];
                newOnOffs[j] = onOffs[j+1];
            }
            newColors[colors.length-1] = colors[colors.length-1];
            newOnOffs[colors.length-1] = onOffs[colors.length-1];
            for(int j=0;j<groupLeds.length(); j++) {
                Integer id = groupLeds.getJSONObject(j).getInt("id");
                apiService.setLight(id, newOnOffs[j], newColors[j]);
            }
            Thread.sleep(1000);
        }
    }
}
