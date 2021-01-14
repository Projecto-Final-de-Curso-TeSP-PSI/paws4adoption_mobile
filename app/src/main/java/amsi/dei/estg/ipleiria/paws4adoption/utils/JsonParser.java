package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.Attribute;
import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;

/**
 * Class that implements several parsing methods related to Json format
 */
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;

public class JsonParser {

    /**
     * Get's a Json response wih all organizations and parses to an organization object's list
     * @param organizations
     * @return all organizations array list
     */
    public static ArrayList<Organization> toOrganizations(JSONArray organizations){
        ArrayList<Organization> organizationsList= new ArrayList<>();

        try{
            for(int i = 0; i < organizations.length(); i++){

                JSONObject organization = (JSONObject) organizations.get(i);
                Integer id =  organization.getInt("id");
                String name =  organization.getString("name");
                String nif =  organization.getString("nif");
                String email =  organization.getString("email");
                String phone =  organization.getString("phone");

                JSONObject address = organization.getJSONObject("address");
                Integer address_id =  address.getInt("id");
                String street =  address.getString("street");
                String door_number =  address.getString("door_number");
                String floor = address.getString("floor");
                Integer postal_code =  address.getInt("postal_code");
                Integer street_code =  address.getInt("street_code");
                String city =  address.getString("city");

                JSONObject district = address.getJSONObject("district");
                Integer district_id =  district.getInt("id");
                String district_name =  district.getString("name");

                Organization auxOrg = new Organization(id, name, nif, email, phone, address_id, street, door_number, floor, postal_code, street_code, city, district_id, district_name);

                organizationsList.add(auxOrg);
            }
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return organizationsList;
    }

    /**
     * Get a Json response and parses to an organization object
     * @param organization
     * @return an organization object
     */
    public static Organization toOrganization(JSONObject organization){
        Organization auxOrg = null;

        try{
            int id =  organization.getInt("id");
            String name =  organization.getString("name");
            String nif =  organization.getString("nif");
            String email =  organization.getString("email");
            String phone =  organization.getString("phone");

            JSONObject address = organization.getJSONObject("address");
            Integer address_id =  address.getInt("id");
            String street =  address.getString("street");
            String door_number =  address.getString("door_number");
            String floor = address.getString("floor");
            Integer postal_code =  address.getInt("postal_code");
            Integer street_code =  address.getInt("street_code");
            String city =  address.getString("city");

            JSONObject district = address.getJSONObject("district");
            Integer district_id =  district.getInt("id");
            String district_name =  district.getString("name");

            auxOrg = new Organization(id, name, nif, email, phone, address_id, street, door_number, floor, postal_code, street_code, city, district_id, district_name);

        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return auxOrg;
    }

    /**
     * Get's a Json response wih all animals and parses to an animal's object's list
     * @param animals
     * @return
     */
    public static ArrayList<Animal> toAnimals(JSONArray animals){
        ArrayList<Animal> animalsList = new ArrayList<>();
        try{
            for(int i = 0; i < animals.length(); i++){

                JSONObject animal = (JSONObject) animals.get(i);

                Integer id = animal.getInt("id");
                String name = animal.getString("name");
                String chipId = animal.getString("chipId");

                JSONObject nature = animal.getJSONObject("nature");
                Integer nature_id = nature.getInt("id");
                String nature_name = nature.getString("name");
                Integer nature_parent_id = nature.getInt("parent_nature_id");
                String nature_parent_name = nature.getString("nameByParentId");

                JSONObject fur_length = animal.getJSONObject("furLength");
                Integer fur_length_id = fur_length.getInt("id");
                String fur_length_name = fur_length.getString("fur_length");

                JSONObject fur_color = animal.getJSONObject("furColor");
                Integer fur_color_id = fur_color.getInt("id");
                String fur_color_name = fur_color.getString("fur_color");

                JSONObject size = animal.getJSONObject("size");
                Integer size_id = size.getInt("id");
                String size_name = size.getString("size");

                String sex = animal.getString("sex");
                String description = animal.getString("description");
                String createAt = animal.getString("createdAt");
                String photo = animal.getString("photo");

                String type = animal.getString("type");

                Integer is_fat = null;
                String missingFound_date = null;
                Integer foundAnimal_location_id = null;
                String foundAnimal_street = null;
                String foundAnimal_city = null;
                Integer foundAnimal_district_id = null;
                String foundAnimal_district_name = null;
                Integer publisher_id = null;
                String publisher_name = null;
                String organization_name = null;
                Integer organization_id = null;
                Integer organization_nif = null;
                String organization_email = null;
                Integer organization_address_id = null;
                String organization_street = null;
                String organization_door_number = null;
                String organization_floor = null;
                String organization_city = null;
                Integer organization_postal_code = null;
                Integer organization_street_code = null;
                Integer organization_district_id = null;
                String organization_district_name = null;

                switch(Objects.requireNonNull(type)){
                    case "adoptionAnimal":
                        JSONObject adoptionAnimal = animal.getJSONObject("adoptionAnimal");
                        is_fat = adoptionAnimal.getInt("is_on_fat");


                        JSONObject associatedUser = adoptionAnimal.getJSONObject("associatedUser");
                        publisher_id = associatedUser.getInt("id");
                        publisher_name = associatedUser.getString("fullName");

                        JSONObject organization = adoptionAnimal.getJSONObject("organization");
                        organization_name = organization.getString("name");
                        organization_id = organization.getInt("id");
                        organization_nif = organization.getInt("nif");
                        organization_email = organization.getString("email");

                        JSONObject address = organization.getJSONObject("address");
                        organization_address_id = address.getInt("id");
                        organization_street = address.getString("street");
                        organization_door_number = address.getString("door_number");
                        organization_floor = address.getString("floor");
                        organization_city = address.getString("city");
                        organization_postal_code = address.getInt("postal_code");
                        organization_street_code = address.getInt("street_code");

                        JSONObject district = address.getJSONObject("district");
                        organization_district_id = district.getInt("id");
                        organization_district_name = district.getString("name");

                        break;
                    case "missingAnimal":
                        JSONObject missingAnimal = animal.getJSONObject("missingAnimal");
                        missingFound_date = missingAnimal.getString("missing_date");

                        JSONObject owner = missingAnimal.getJSONObject("owner");
                        publisher_id = owner.getInt("id");
                        publisher_name = owner.getString("fullName");
                        break;

                    case "foundAnimal":
                        JSONObject foundAnimal = animal.getJSONObject("foundAnimal");
                        missingFound_date = foundAnimal.getString("found_date");

                        JSONObject location = foundAnimal.getJSONObject("location");
                        foundAnimal_location_id = location.getInt("id");
                        foundAnimal_street = location.getString("street");
                        foundAnimal_city = location.getString("city");

                        JSONObject districtFound = location.getJSONObject("district");
                        foundAnimal_district_id = districtFound.getInt("id");
                        foundAnimal_district_name = districtFound.getString("name");

                        JSONObject user = foundAnimal.getJSONObject("user");
                        publisher_id = user.getInt("id");
                        publisher_name = user.getString("fullName");
                        break;
                }

                Animal auxAnimal = new Animal(id, name, chipId, nature_id, nature_name, nature_parent_id, nature_parent_name, fur_length_id, fur_length_name, fur_color_id, fur_color_name, size_id, size_name, sex, description, createAt, photo, type, publisher_id, publisher_name, is_fat, missingFound_date, foundAnimal_location_id, foundAnimal_street, foundAnimal_city, foundAnimal_district_id, foundAnimal_district_name, organization_id, organization_name, organization_nif, organization_email, organization_address_id, organization_street, organization_door_number, organization_floor, organization_city, organization_postal_code, organization_street_code, organization_district_id, organization_district_name);
                animalsList.add(auxAnimal);
            }
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return animalsList;

    }

    /**
     *
     * @param animal
     * @return
     */
    public static Animal toAnimal(JSONObject animal){
        Animal auxAnimal = null;
        try{
                Integer id = animal.getInt("id");
                String name = animal.getString("name");
                String chipId = animal.getString("chipId");

                JSONObject nature = animal.getJSONObject("nature");
                Integer nature_id = nature.getInt("id");
                String nature_name = nature.getString("name");
                Integer nature_parent_id = nature.getInt("parent_nature_id");
                String nature_parent_name = nature.getString("nameByParentId");

                JSONObject fur_length = animal.getJSONObject("fur_length");
                Integer fur_length_id = fur_length.getInt("id");
                String fur_length_name = fur_length.getString("fur_length");

                JSONObject fur_color = animal.getJSONObject("fur_color");
                Integer fur_color_id = fur_color.getInt("id");
                String fur_color_name = fur_color.getString("fur_color");

                JSONObject size = animal.getJSONObject("size");
                Integer size_id = size.getInt("id");
                String size_name = size.getString("size");

                String sex = animal.getString("sex");
                String description = animal.getString("description");
                String createAt = animal.getString("createdAt");
                String photo = animal.getString("photo");

                String type = animal.getString("type");

                Integer is_fat = null;
                String missingFound_date = null;
                Integer foundAnimal_location_id = null;
                String foundAnimal_street = null;
                String foundAnimal_city = null;
                Integer foundAnimal_district_id = null;
                String foundAnimal_district_name = null;
                Integer publisher_id = null;
                String publisher_name = null;
                String organization_name = null;
                Integer organization_id = null;
                Integer organization_nif = null;
                String organization_email = null;
                Integer organization_address_id = null;
                String organization_street = null;
                String organization_door_number = null;
                String organization_floor = null;
                String organization_city = null;
                Integer organization_postal_code = null;
                Integer organization_street_code = null;
                Integer organization_district_id = null;
                String organization_district_name = null;

                switch(Objects.requireNonNull(type)){
                    case "adoptionAnimal":
                        JSONObject adoptionAnimal = animal.getJSONObject("adoptionAnimal");
                        is_fat = adoptionAnimal.getInt("is_on_fat");


                        JSONObject associatedUser = adoptionAnimal.getJSONObject("associatedUser");
                        publisher_id = associatedUser.getInt("id");
                        publisher_name = associatedUser.getString("fullName");

                        JSONObject organization = adoptionAnimal.getJSONObject("organization");
                        organization_name = organization.getString("name");
                        organization_id = organization.getInt("id");
                        organization_nif = organization.getInt("nif");
                        organization_email = organization.getString("email");

                        JSONObject address = organization.getJSONObject("address");
                        organization_address_id = address.getInt("id");
                        organization_street = address.getString("street");
                        organization_door_number = address.getString("door_number");
                        organization_floor = address.getString("floor");
                        organization_city = address.getString("city");
                        organization_postal_code = address.getInt("postal_code");
                        organization_street_code = address.getInt("street_code");

                        JSONObject district = address.getJSONObject("district");
                        organization_district_id = district.getInt("id");
                        organization_district_name = district.getString("name");

                        break;
                    case "missingAnimal":
                        JSONObject missingAnimal = animal.getJSONObject("missingAnimal");
                        missingFound_date = missingAnimal.getString("missing_date");

                        JSONObject owner = missingAnimal.getJSONObject("owner");
                        publisher_id = owner.getInt("id");
                        publisher_name = owner.getString("fullName");
                        break;

                    case "foundAnimal":
                        JSONObject foundAnimal = animal.getJSONObject("foundAnimal");
                        missingFound_date = foundAnimal.getString("found_date");

                        JSONObject location = foundAnimal.getJSONObject("location");
                        foundAnimal_location_id = location.getInt("id");
                        foundAnimal_street = location.getString("street");
                        foundAnimal_city = location.getString("city");

                        JSONObject districtFound = location.getJSONObject("district");
                        foundAnimal_district_id = districtFound.getInt("id");
                        foundAnimal_district_name = districtFound.getString("name");

                        JSONObject user = foundAnimal.getJSONObject("user");
                        publisher_id = user.getInt("id");
                        publisher_name = user.getString("fullName");
                        break;
                }

                auxAnimal = new Animal(id, name, chipId, nature_id, nature_name, nature_parent_id, nature_parent_name, fur_length_id, fur_length_name, fur_color_id, fur_color_name, size_id, size_name, sex, description, createAt, photo, type, publisher_id, publisher_name, is_fat, missingFound_date, foundAnimal_location_id, foundAnimal_street, foundAnimal_city, foundAnimal_district_id, foundAnimal_district_name, organization_id, organization_name, organization_nif, organization_email, organization_address_id, organization_street, organization_door_number, organization_floor, organization_city, organization_postal_code, organization_street_code, organization_district_id, organization_district_name);

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                return null;
            }

        return auxAnimal;
    }

    /**
     * Get's a Json response with all attributes and parses to an attributes object's list
     * @param response received from json
     * @param attSymLink name of the attribute over the json response
     * @return attributes list
     */
    public static ArrayList<Attribute> toAttributes(JSONArray response, String attSymLink){
        ArrayList<Attribute> attributesList= new ArrayList<>();
        try{
            for(int i = 0; i < response.length(); i++){
                JSONObject attributes = (JSONObject) response.get(i);
                Integer id =  attributes.getInt("id");
                String name =  attributes.getString(attSymLink);
                Attribute auxAttribute = new Attribute(id, name);
                attributesList.add(auxAttribute);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return attributesList;
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
    public static UserProfile parserJsonUserProfile(JSONObject response){
        UserProfile userProfile = null;
        try{
            int id = response.getInt("id");
            String email = response.getString("email");
            String username = response.getString("username");
            String firstName = response.getString("firstName");
            String lastName = response.getString("lastName");
            String nif = response.getString("nif");
            String phone = response.getString("phone");

            JSONObject address = response.getJSONObject("address");
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
                   email, username,
                   firstName, lastName, nif, phone,
                   street, door_number, floor, postal_code, street_code, city, districtId);

            userProfile.setId(id);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return userProfile;
    }
}
