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
     * Get a Json response and parses to an animal object
     * @param response
     * @return
     */
    public static ArrayList<Animal> toAnimals(JSONArray response){
        ArrayList<Animal> animalsList= new ArrayList<>();

        try{
            for(int i = 0; i < response.length(); i++){
                JSONObject animal = (JSONObject) response.get(i);

                Integer id = Wrench.purifyInteger(animal.getString("id"));
                String name = Wrench.purifyString(animal.getString("name"));
                String chipId = Wrench.purifyString(animal.getString("chipId"));

                JSONObject nature = animal.getJSONObject("nature");
                Integer nature_id = Wrench.purifyInteger(nature.getString("id"));
                String nature_name = Wrench.purifyString(nature.getString("name"));
                Integer nature_parent_id = Wrench.purifyInteger(animal.getString("parent_nature_id"));
                String nature_parent_name = Wrench.purifyString(animal.getString("parent_nature_name"));

                JSONObject fur_length = animal.getJSONObject("fur_length");
                Integer fur_length_id = Wrench.purifyInteger(fur_length.getString("id"));
                String fur_length_name = Wrench.purifyString(fur_length.getString("fur_length"));

                JSONObject fur_color = animal.getJSONObject("fur_color");
                Integer fur_color_id = Wrench.purifyInteger(fur_color.getString("fur_color_id"));
                String fur_color_name = Wrench.purifyString(fur_color.getString("fur_color"));

                JSONObject size = animal.getJSONObject("missingAnimal");
                Integer size_id = Wrench.purifyInteger(size.getString("id"));
                String size_name = Wrench.purifyString(size.getString("size"));

                String sex = Wrench.purifyString(animal.getString("sex"));
                String description = Wrench.purifyString(animal.getString("description"));
                String createAt = Wrench.purifyString(animal.getString("createAt"));
                Integer photo = Wrench.purifyInteger(animal.getString("photo"));

                String type = Wrench.purifyString(animal.getString("type"));

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
                        is_fat = Wrench.purifyInteger(animal.getString("is_on_fat"));
                        missingFound_date = Wrench.purifyString(animal.getString("missing_date"));

                        JSONObject associatedUser = adoptionAnimal.getJSONObject("associatedUser");
                        publisher_id = Wrench.purifyInteger(associatedUser.getString("id"));
                        publisher_name = Wrench.purifyString(associatedUser.getString("name"));

                        JSONObject organization = adoptionAnimal.getJSONObject("organization");
                        organization_name = Wrench.purifyString(animal.getString("organization_name"));
                        organization_id = Wrench.purifyInteger(animal.getString("organization_id"));
                        organization_nif = Wrench.purifyInteger(animal.getString("organization_nif"));
                        organization_email = Wrench.purifyString(animal.getString("organization_email"));
                        organization_address_id = Wrench.purifyInteger(animal.getString("organization_address_id"));
                        organization_street = Wrench.purifyString(animal.getString("organization_street"));
                        organization_door_number = Wrench.purifyString(animal.getString("organization_door_number"));
                        organization_floor = Wrench.purifyString(animal.getString("organization_floor"));
                        organization_city = Wrench.purifyString(animal.getString("organization_city"));
                        organization_postal_code = Wrench.purifyInteger(animal.getString("organization_postal_code"));
                        organization_street_code = Wrench.purifyInteger(animal.getString("organization_street_code"));
                        organization_district_id = Wrench.purifyInteger(animal.getString("organization_district_id"));
                        organization_district_name = Wrench.purifyString(animal.getString("organziation_district_name"));

                        break;
                    case "missingAnimal":
                        JSONObject missingAnimal = animal.getJSONObject("missingAnimal");

                        JSONObject owner = missingAnimal.getJSONObject("owner");
                        publisher_id = Wrench.purifyInteger(owner.getString("id"));
                        publisher_name = Wrench.purifyString(owner.getString("name"));
                        break;

                    case "foundAnimal":
                        JSONObject foundAnimal = animal.getJSONObject("foundAnimal");
                        missingFound_date = Wrench.purifyString(animal.getString("missingFound_date"));
                        foundAnimal_location_id = Wrench.purifyInteger(animal.getString("foundAnimal_location_id"));
                        foundAnimal_street = Wrench.purifyString(animal.getString("foundAnimal_street"));
                        foundAnimal_city = Wrench.purifyString(animal.getString("foundAnimal_city"));
                        foundAnimal_district_id = Wrench.purifyInteger(animal.getString("foundAnimal_district_id"));
                        foundAnimal_district_name = Wrench.purifyString(animal.getString("foundAnimal_district_name"));

                        JSONObject user = foundAnimal.getJSONObject("user");
                        publisher_id = Wrench.purifyInteger(user.getString("id"));
                        publisher_name = Wrench.purifyString(user.getString("name"));
                        break;
                }

                Animal auxAnimal = new Animal(id, name, chipId, nature_id, nature_name, nature_parent_id, nature_parent_name, fur_length_id, fur_length_name, fur_color_id, fur_color_name, size_id, size_name, sex, description, createAt, photo, type, publisher_id, publisher_name, is_fat, missingFound_date, foundAnimal_location_id, foundAnimal_street, foundAnimal_city, foundAnimal_district_id, foundAnimal_district_name, organization_id, organization_name, organization_nif, organization_email, organization_address_id, organization_street, organization_door_number, organization_floor, organization_city, organization_postal_code, organization_street_code, organization_district_id, organization_district_name);
                animalsList.add(auxAnimal);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        return animalsList;
    }

    /**
     * Get a Json response and parses to an found animal object
     * @param response
     * @return
     */
    public static Animal toAdoptionAnimal(String response){
        Animal auxAnimal = null;
        try{
            JSONObject animal = new JSONObject(response);

            Integer id = Wrench.purifyInteger(animal.getString("id"));
            String name = Wrench.purifyString(animal.getString("name"));
            String chipId = Wrench.purifyString(animal.getString("chipId"));

            JSONObject nature = animal.getJSONObject("nature");
            Integer nature_id = Wrench.purifyInteger(nature.getString("id"));
            String nature_name = Wrench.purifyString(nature.getString("name"));
            Integer nature_parent_id = Wrench.purifyInteger(animal.getString("parent_nature_id"));
            String nature_parent_name = Wrench.purifyString(animal.getString("parent_nature_name"));

            JSONObject fur_length = animal.getJSONObject("fur_length");
            Integer fur_length_id = Wrench.purifyInteger(fur_length.getString("id"));
            String fur_length_name = Wrench.purifyString(fur_length.getString("fur_length"));

            JSONObject fur_color = animal.getJSONObject("fur_color");
            Integer fur_color_id = Wrench.purifyInteger(fur_color.getString("fur_color_id"));
            String fur_color_name = Wrench.purifyString(fur_color.getString("fur_color"));

            JSONObject size = animal.getJSONObject("missingAnimal");
            Integer size_id = Wrench.purifyInteger(size.getString("id"));
            String size_name = Wrench.purifyString(size.getString("size"));

            String sex = Wrench.purifyString(animal.getString("sex"));
            String description = Wrench.purifyString(animal.getString("description"));
            String createAt = Wrench.purifyString(animal.getString("createAt"));
            Integer photo = Wrench.purifyInteger(animal.getString("photo"));

            String type = Wrench.purifyString(animal.getString("type"));

            Integer foundAnimal_location_id = null;
            String foundAnimal_street = null;
            String foundAnimal_city = null;
            Integer foundAnimal_district_id = null;
            String foundAnimal_district_name = null;

            JSONObject adoptionAnimal = animal.getJSONObject("adoptionAnimal");
            Integer is_fat = Wrench.purifyInteger(animal.getString("is_on_fat"));
            String missingFound_date = Wrench.purifyString(animal.getString("missing_date"));

            JSONObject associatedUser = adoptionAnimal.getJSONObject("associatedUser");
            Integer publisher_id = Wrench.purifyInteger(associatedUser.getString("id"));
            String publisher_name = Wrench.purifyString(associatedUser.getString("name"));

            JSONObject organization = adoptionAnimal.getJSONObject("organization");
            String organization_name = Wrench.purifyString(animal.getString("organization_name"));
            Integer organization_id = Wrench.purifyInteger(animal.getString("organization_id"));
            Integer organization_nif = Wrench.purifyInteger(animal.getString("organization_nif"));
            String organization_email = Wrench.purifyString(animal.getString("organization_email"));
            Integer organization_address_id = Wrench.purifyInteger(animal.getString("organization_address_id"));
            String organization_street = Wrench.purifyString(animal.getString("organization_street"));
            String organization_door_number = Wrench.purifyString(animal.getString("organization_door_number"));
            String organization_floor = Wrench.purifyString(animal.getString("organization_floor"));
            String organization_city = Wrench.purifyString(animal.getString("organization_city"));
            Integer organization_postal_code = Wrench.purifyInteger(animal.getString("organization_postal_code"));
            Integer organization_street_code = Wrench.purifyInteger(animal.getString("organization_street_code"));
            Integer organization_district_id = Wrench.purifyInteger(animal.getString("organization_district_id"));
            String organization_district_name = Wrench.purifyString(animal.getString("organziation_district_name"));


            auxAnimal = new Animal(id, name, chipId, nature_id, nature_name, nature_parent_id, nature_parent_name, fur_length_id, fur_length_name, fur_color_id, fur_color_name, size_id, size_name, sex, description, createAt, photo, type, publisher_id, publisher_name, is_fat, missingFound_date, foundAnimal_location_id, foundAnimal_street, foundAnimal_city, foundAnimal_district_id, foundAnimal_district_name, organization_id, organization_name, organization_nif, organization_email, organization_address_id, organization_street, organization_door_number, organization_floor, organization_city, organization_postal_code, organization_street_code, organization_district_id, organization_district_name);

        } catch(JSONException e){
            e.printStackTrace();
        }


        return auxAnimal;
    }

    /**
     * Get's a Json response with all animals and parses to an missing animal list object
     * @param response
     * @return
     */
    public static Animal toMissingAnimal(JSONArray response){
        Animal auxAnimal = null;

        try{
            for(int i = 0; i < response.length(); i++){
                JSONObject animal = (JSONObject) response.get(i);
                Integer id = Wrench.purifyInteger(animal.getString("id"));
                String name = Wrench.purifyString(animal.getString("name"));
                String chipId = Wrench.purifyString(animal.getString("chipId"));

                JSONObject nature = animal.getJSONObject("nature");
                Integer nature_id = Wrench.purifyInteger(nature.getString("id"));
                String nature_name = Wrench.purifyString(nature.getString("name"));
                Integer nature_parent_id = Wrench.purifyInteger(animal.getString("parent_nature_id"));
                String nature_parent_name = Wrench.purifyString(animal.getString("parent_nature_name"));

                JSONObject fur_length = animal.getJSONObject("fur_length");
                Integer fur_length_id = Wrench.purifyInteger(fur_length.getString("id"));
                String fur_length_name = Wrench.purifyString(fur_length.getString("fur_length"));

                JSONObject fur_color = animal.getJSONObject("fur_color");
                Integer fur_color_id = Wrench.purifyInteger(fur_color.getString("fur_color_id"));
                String fur_color_name = Wrench.purifyString(fur_color.getString("fur_color"));

                JSONObject size = animal.getJSONObject("missingAnimal");
                Integer size_id = Wrench.purifyInteger(size.getString("id"));
                String size_name = Wrench.purifyString(size.getString("size"));

                String sex = Wrench.purifyString(animal.getString("sex"));
                String description = Wrench.purifyString(animal.getString("description"));
                String createAt = Wrench.purifyString(animal.getString("createAt"));
                Integer photo = Wrench.purifyInteger(animal.getString("photo"));

                String type = Wrench.purifyString(animal.getString("type"));

                Integer is_fat = null;
                String missingFound_date = null;
                Integer foundAnimal_location_id = null;
                String foundAnimal_street = null;
                String foundAnimal_city = null;
                Integer foundAnimal_district_id = null;
                String foundAnimal_district_name = null;

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

                JSONObject missingAnimal = animal.getJSONObject("missingAnimal");
                JSONObject owner = missingAnimal.getJSONObject("owner");
                Integer publisher_id = Wrench.purifyInteger(owner.getString("id"));
                String publisher_name = Wrench.purifyString(owner.getString("name"));

                auxAnimal = new Animal(id, name, chipId, nature_id, nature_name, nature_parent_id, nature_parent_name, fur_length_id, fur_length_name, fur_color_id, fur_color_name, size_id, size_name, sex, description, createAt, photo, type, publisher_id, publisher_name, is_fat, missingFound_date, foundAnimal_location_id, foundAnimal_street, foundAnimal_city, foundAnimal_district_id, foundAnimal_district_name, organization_id, organization_name, organization_nif, organization_email, organization_address_id, organization_street, organization_door_number, organization_floor, organization_city, organization_postal_code, organization_street_code, organization_district_id, organization_district_name);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        return auxAnimal;
    }

    /**
     * Get a Json response and parses to an found animal object
     * @param response
     * @return
     */
    public static Animal toFoundAnimal(String response){
        Animal auxAnimal = null;
        try{
            JSONObject animal = new JSONObject(response);

            Integer id = Wrench.purifyInteger(animal.getString("id"));
            String name = Wrench.purifyString(animal.getString("name"));
            String chipId = Wrench.purifyString(animal.getString("chipId"));

            JSONObject nature = animal.getJSONObject("nature");
            Integer nature_id = Wrench.purifyInteger(nature.getString("id"));
            String nature_name = Wrench.purifyString(nature.getString("name"));
            Integer nature_parent_id = Wrench.purifyInteger(animal.getString("parent_nature_id"));
            String nature_parent_name = Wrench.purifyString(animal.getString("parent_nature_name"));

            JSONObject fur_length = animal.getJSONObject("fur_length");
            Integer fur_length_id = Wrench.purifyInteger(fur_length.getString("id"));
            String fur_length_name = Wrench.purifyString(fur_length.getString("fur_length"));

            JSONObject fur_color = animal.getJSONObject("fur_color");
            Integer fur_color_id = Wrench.purifyInteger(fur_color.getString("fur_color_id"));
            String fur_color_name = Wrench.purifyString(fur_color.getString("fur_color"));

            JSONObject size = animal.getJSONObject("missingAnimal");
            Integer size_id = Wrench.purifyInteger(size.getString("id"));
            String size_name = Wrench.purifyString(size.getString("size"));

            String sex = Wrench.purifyString(animal.getString("sex"));
            String description = Wrench.purifyString(animal.getString("description"));
            String createAt = Wrench.purifyString(animal.getString("createAt"));
            Integer photo = Wrench.purifyInteger(animal.getString("photo"));

            String type = Wrench.purifyString(animal.getString("type"));

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
                    is_fat = Wrench.purifyInteger(animal.getString("is_on_fat"));
                    missingFound_date = Wrench.purifyString(animal.getString("missing_date"));

                    JSONObject associatedUser = adoptionAnimal.getJSONObject("associatedUser");
                    publisher_id = Wrench.purifyInteger(associatedUser.getString("id"));
                    publisher_name = Wrench.purifyString(associatedUser.getString("name"));

                    JSONObject organization = adoptionAnimal.getJSONObject("organization");
                    organization_name = Wrench.purifyString(animal.getString("organization_name"));
                    organization_id = Wrench.purifyInteger(animal.getString("organization_id"));
                    organization_nif = Wrench.purifyInteger(animal.getString("organization_nif"));
                    organization_email = Wrench.purifyString(animal.getString("organization_email"));
                    organization_address_id = Wrench.purifyInteger(animal.getString("organization_address_id"));
                    organization_street = Wrench.purifyString(animal.getString("organization_street"));
                    organization_door_number = Wrench.purifyString(animal.getString("organization_door_number"));
                    organization_floor = Wrench.purifyString(animal.getString("organization_floor"));
                    organization_city = Wrench.purifyString(animal.getString("organization_city"));
                    organization_postal_code = Wrench.purifyInteger(animal.getString("organization_postal_code"));
                    organization_street_code = Wrench.purifyInteger(animal.getString("organization_street_code"));
                    organization_district_id = Wrench.purifyInteger(animal.getString("organization_district_id"));
                    organization_district_name = Wrench.purifyString(animal.getString("organziation_district_name"));

                    break;
                case "missingAnimal":
                    JSONObject missingAnimal = animal.getJSONObject("missingAnimal");

                    JSONObject owner = missingAnimal.getJSONObject("owner");
                    publisher_id = Wrench.purifyInteger(owner.getString("id"));
                    publisher_name = Wrench.purifyString(owner.getString("name"));
                    break;

                case "foundAnimal":
                    JSONObject foundAnimal = animal.getJSONObject("foundAnimal");
                    missingFound_date = Wrench.purifyString(animal.getString("missingFound_date"));
                    foundAnimal_location_id = Wrench.purifyInteger(animal.getString("foundAnimal_location_id"));
                    foundAnimal_street = Wrench.purifyString(animal.getString("foundAnimal_street"));
                    foundAnimal_city = Wrench.purifyString(animal.getString("foundAnimal_city"));
                    foundAnimal_district_id = Wrench.purifyInteger(animal.getString("foundAnimal_district_id"));
                    foundAnimal_district_name = Wrench.purifyString(animal.getString("foundAnimal_district_name"));

                    JSONObject user = foundAnimal.getJSONObject("user");
                    publisher_id = Wrench.purifyInteger(user.getString("id"));
                    publisher_name = Wrench.purifyString(user.getString("name"));
                    break;
            }

            auxAnimal = new Animal(id, name, chipId, nature_id, nature_name, nature_parent_id, nature_parent_name, fur_length_id, fur_length_name, fur_color_id, fur_color_name, size_id, size_name, sex, description, createAt, photo, type, publisher_id, publisher_name, is_fat, missingFound_date, foundAnimal_location_id, foundAnimal_street, foundAnimal_city, foundAnimal_district_id, foundAnimal_district_name, organization_id, organization_name, organization_nif, organization_email, organization_address_id, organization_street, organization_door_number, organization_floor, organization_city, organization_postal_code, organization_street_code, organization_district_id, organization_district_name);

        } catch(JSONException e){
            e.printStackTrace();
        }


        return auxAnimal;
    }

    /**
     * Get's a Json response with all attributes and parses to an attributes object's list
     * @param response received from json
     * @param attId id of the attribute
     * @param attName name of the attribute
     * @return attributes list
     */
    public static ArrayList<Attribute> toAttributes(JSONArray response, String attId, String attName){
        ArrayList<Attribute> attributesList= new ArrayList<>();

        try{
            for(int i = 0; i < response.length(); i++){

                JSONObject attributes = (JSONObject) response.get(i);
                Integer id =  Wrench.purifyInteger(attributes.getString(attId));
                String name =  Wrench.purifyString(attributes.getString(attName));

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
