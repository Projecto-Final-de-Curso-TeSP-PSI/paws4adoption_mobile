package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that implements several parsing methods related to Json format
 */
public class JsonParser {


    /**
     * Method that receives a string in Json format, and parses to object
     * @param response
     * @returns the token
     */
    public static String parserJsonLogin (String response){
        String token = null;
        try{
            JSONObject login = new JSONObject(response);
            if(login.getBoolean("success")){
                token = login.getString("token");
            }
        }catch (JSONException e){
            e.printStackTrace();;
        }
        return token;
    }

}
