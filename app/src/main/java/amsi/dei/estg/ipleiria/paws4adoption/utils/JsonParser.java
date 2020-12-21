package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
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
                Integer id =  Wrench.purifyInteger(organization.getString("id"));
                String name =  Wrench.purifyString(organization.getString("name"));
                String nif =  Wrench.purifyString(organization.getString("nif"));
                String email =  Wrench.purifyString(organization.getString("email"));
                String phone =  Wrench.purifyString(organization.getString("phone"));

                JSONObject address = organization.getJSONObject("address");
                Integer address_id =  Wrench.purifyInteger(address.getString("id"));
                String street =  Wrench.purifyString(address.getString("street"));
                String door_number =  Wrench.purifyString(address.getString("door_number"));
                String floor = Wrench.purifyString(address.getString("floor"));
                Integer postal_code =  Wrench.purifyInteger(address.getString("postal_code"));
                Integer street_code =  Wrench.purifyInteger(address.getString("street_code"));
                String city =  Wrench.purifyString(address.getString("city"));

                JSONObject district = address.getJSONObject("district");
                Integer district_id =  Wrench.purifyInteger(district.getString("id"));
                String district_name =  Wrench.purifyString(district.getString("name"));

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
            String name =  Wrench.purifyString(organization.getString("name"));
            String nif =  Wrench.purifyString(organization.getString("nif"));
            String email =  Wrench.purifyString(organization.getString("email"));
            String phone =  Wrench.purifyString(organization.getString("phone"));

            JSONObject address = organization.getJSONObject("address");
            Integer address_id =  address.getInt("id");
            String street =  Wrench.purifyString(address.getString("street"));
            String door_number =  Wrench.purifyString(address.getString("door_number"));
            String floor = Wrench.purifyString(address.getString("floor"));
            Integer postal_code =  address.getInt("postal_code");
            Integer street_code =  address.getInt("street_code");
            String city =  Wrench.purifyString(address.getString("city"));

            JSONObject district = address.getJSONObject("district");
            Integer district_id =  district.getInt("id");
            String district_name =  Wrench.purifyString(district.getString("name"));

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
            int id = result.getInt("id");
            String email = result.getString("email");
            String username = result.getString("username");
            String firstName = result.getString("firstName");
            String lastName = result.getString("lastName");
            String nif = result.getString("nif");
            String phone = result.getString("phone");

            JSONObject address = result.getJSONObject("address");
            String street = address.getString("street");
            String door_number = address.getString("door_number");
            String floor = address.getString("floor");
            String postal_code = address.getString("postal_code");
            String street_code = address.getString("street_code");
            String city = address.getString("city");

            JSONObject district = address.getJSONObject("district");
            int districtId = district.getInt("id");
            String districtName = district.getString("name");

            userProfile = new UserProfile(
                   id, email, username,
                   firstName, lastName, nif, phone,
                   street, door_number, floor, postal_code, street_code, city, districtId);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return userProfile;
    }

    public static ArrayList<Animal> toAnimals(JSONArray response){
        ArrayList<Animal> animalsList= new ArrayList<>();

        try{
            for(int i = 0; i < response.length(); i++){

                JSONObject animal = (JSONObject) response.get(i);
                Integer id =  Wrench.purifyInteger(animal.getString("id"));
                String name =  Wrench.purifyString(animal.getString("name"));
                String chipId =  Wrench.purifyString(animal.getString("chipId"));
                Integer nature_id =  Wrench.purifyInteger(animal.getString("nature_id"));
                String nature_name =  Wrench.purifyString(animal.getString("nature_name"));




               // Animal animalAux = new Animal(id, name, chipId, nature_id, nature_name);

                //animalsList.add(animal);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        return animalsList;
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
