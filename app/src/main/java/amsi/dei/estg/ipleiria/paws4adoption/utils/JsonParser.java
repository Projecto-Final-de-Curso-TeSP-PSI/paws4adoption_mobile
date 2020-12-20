package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;

/**
 * Class that implements several parsing methods related to Json format
 */
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;

public class JsonParser {

    /**
     * Get's a Json response wih all organizations and parses to an organization object
     * @param response
     * @return all organizations array list
     */
    public static ArrayList<Organization> toOrganizations(JSONArray response){
        ArrayList<Organization> organizationsList= new ArrayList<>();

        try{
            for(int i = 0; i < response.length(); i++){

                JSONObject organization = (JSONObject) response.get(i);
                int id =  organization.getInt("id");
                String name =  Wrench.purify(organization.getString("name"));
                String nif =  Wrench.purify(organization.getString("nif"));
                String email =  Wrench.purify(organization.getString("email"));
                String phone =  Wrench.purify(organization.getString("phone"));

                JSONObject address = organization.getJSONObject("address");
                String address_id =  Wrench.purify(address.getString("id"));
                String street =  Wrench.purify(address.getString("street"));
                String door_number =  Wrench.purify(address.getString("door_number"));
                String floor = Wrench.purify(address.getString("floor"));
                String postal_code =  Wrench.purify(address.getString("postal_code"));
                String street_code =  Wrench.purify(address.getString("street_code"));
                String city =  Wrench.purify(address.getString("city"));

                JSONObject district = address.getJSONObject("district");
                String district_id =  Wrench.purify(district.getString("id"));
                String district_name =  Wrench.purify(district.getString("name"));

                Organization auxOrg = new Organization(id, name, nif, email, phone, address_id, street, door_number, floor, postal_code, street_code, city, district_id, district_name);

                organizationsList.add(auxOrg);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        return organizationsList;
    }

    /**
     * Get a Json response and parses to an organization object
     * @param response
     * @return an organization object
     */
    public static Organization toOrganization(String response){
        Organization auxOrg = null;

        try{

            JSONObject organization = new JSONObject(response);
            int id =  organization.getInt("id");
            String name =  Wrench.purify(organization.getString("name"));
            String nif =  Wrench.purify(organization.getString("nif"));
            String email =  Wrench.purify(organization.getString("email"));
            String phone =  Wrench.purify(organization.getString("phone"));

            JSONObject address = organization.getJSONObject("address");
            String address_id =  Wrench.purify(address.getString("id"));
            String street =  Wrench.purify(address.getString("street"));
            String door_number =  Wrench.purify(address.getString("door_number"));
            String floor = Wrench.purify(address.getString("floor"));
            String postal_code =  Wrench.purify(address.getString("postal_code"));
            String street_code =  Wrench.purify(address.getString("street_code"));
            String city =  Wrench.purify(address.getString("city"));

            JSONObject district = address.getJSONObject("district");
            String district_id =  Wrench.purify(district.getString("id"));
            String district_name =  Wrench.purify(district.getString("name"));

            auxOrg = new Organization(id, name, nif, email, phone, address_id, street, door_number, floor, postal_code, street_code, city, district_id, district_name);

        } catch(JSONException e){
            e.printStackTrace();
        }

        return auxOrg;
    }

    /**
     * Method that receives the login response in Json format, and parses to object
     * @param response
     * @returns the token received on the JSON object response
     */
    public static String parserJsonLogin (String response){
        String token = null;
        try{
            JSONObject login = new JSONObject(response);
            if(login.getBoolean("success")){
                token = login.getString("token");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return token;
    }

    /**
     * Method that receives a string in Json format, and parses to UserProfile object
     * @param response
     * @return UserProfile
     */
    public static UserProfile parserJsonUserProfile(String response){
        UserProfile userProfile = null;
        try{
            JSONObject result = new JSONObject(response);
            if(result.getBoolean("success")){
                userProfile = new UserProfile(
                        result.getInt("id"),
                        result.getString("email"),
                        result.getString("username"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("nif"),
                        result.getString("phone"),
                        result.getString("street"),
                        result.getString("door_number"),
                        result.getString("floor"),
                        result.getString("postal_code"),
                        result.getString("street_code"),
                        result.getString("city"),
                        result.getInt("address.district.id")
                );
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return userProfile;
    }

    /**
     * Method that checks if the mobile equipments has internet connection
     * @param context
     * @return Bool
     */
    public static boolean isConnectionInternet(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
