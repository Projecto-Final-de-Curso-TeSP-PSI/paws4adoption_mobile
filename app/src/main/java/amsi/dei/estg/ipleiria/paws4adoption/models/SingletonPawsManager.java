package amsi.dei.estg.ipleiria.paws4adoption.models;

import android.content.Context;

import java.util.ArrayList;
//import java.util.Base64;
import android.util.Base64;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AttributeListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.OrganizationsListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.RequestListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UploadPhotoListener;
import amsi.dei.estg.ipleiria.paws4adoption.utils.JsonParser;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Wrench;
import amsi.dei.estg.ipleiria.paws4adoption.views.MenuMainActivity;
import amsi.dei.estg.ipleiria.paws4adoption.views.PostAnimalActivity;

public class SingletonPawsManager implements OrganizationsListener, AnimalListener{

    //API local address (may change each time you start your machine)
    //private static final String COMPUTER_LOCAL_IP = "10.0.2.2";
    //private static final String COMPUTER_LOCAL_IP = "192.168.1.65";
//    private static final String COMPUTER_LOCAL_IP = "192.168.42.129";
    private static final String COMPUTER_LOCAL_IP = "10.0.211.189";


    //private static final String COMPUTER_LOCAL_IP = "localhost";
    private static final String API_LOCAL_URL = "http://" + COMPUTER_LOCAL_IP + "/pet-adoption/paws4adoption_web/backend/web/api/";

    //Singleton instance
    private static SingletonPawsManager instance = null;

    //DB list's declaration
    ArrayList<Organization> organizations;
    ArrayList<Animal> animals;
    ArrayList<Attribute> attributes;

    //Volley static queue
    private static RequestQueue volleyQueue = null;

    //Listeners declaration
    LoginListener loginListener;
    UploadPhotoListener uploadPhotoListener;
    UserProfileListener userProfileListener;
    OrganizationsListener organizationsListener;
    AnimalListener animalListener;
    AttributeListener attributeListener;
    RequestListener requestListener;

    //BD Helper's declaration
    OrganizationsDBHelper organizationsDBHelper;

    //Endpoints for requests
    private static final String mUrlAPILogin = API_LOCAL_URL + "users/token";
    private static final String mUrlAPIUserProfile = API_LOCAL_URL + "users";
    private static final String mUrlAPIOrganizations = API_LOCAL_URL + "organizations";
    private static final String mUrlAPIAnimals = API_LOCAL_URL + "animals";
    private static final String mUrlAPIMissingAnimals = API_LOCAL_URL + "missing-animals";
    private static final String mUrlAPIFoundAnimal = API_LOCAL_URL + "found-animals";


    /**
     * Gets the one and only instance of the singleton
     *
     * @param context
     * @return
     */
    public static synchronized SingletonPawsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonPawsManager(context);

            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    /**
     * Contructor for the SingletonPawsManager class
     *
     * @param context
     */
    private SingletonPawsManager(Context context) {
        this.organizationsDBHelper = new OrganizationsDBHelper(context);
    }


    //############################################# LISTENERS IMPLEMENTATION ##################################################

    /**
     * Method for register the login listener
     *
     * @param loginListener
     */
    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    /**
     * Method for register the user profile listener
     * @param userProfileListener
     */
    public void setUserProfileListener(UserProfileListener userProfileListener){
        this.userProfileListener = userProfileListener;
    }

    /**
     * Method for registe the request listener
     * @param requestListener
     */
    public void setRequestListener(RequestListener requestListener){
        this.requestListener = requestListener;
    }

    //################ ORGANIZATIONS ################

    /**
     * Method for register the organization listener
     *
     * @param organizationsLisneter
     */
    public void setOrganizationsListener(OrganizationsListener organizationsLisneter){
        this.organizationsListener = organizationsLisneter;
    }

    @Override
    public void onRefreshOrganizationsList(ArrayList<Organization> organizationsList) {
        //TODO: falta implementar este metodo
    }

    @Override
    public void onUpdateOrganizationsList(Organization organization, int operation) {
        switch(operation){
            case RockChisel.INSERT_DB:
                insertOrganizationDB(organization);
                break;
            case RockChisel.UPDATE_DB:
                updateOrganizationDB(organization);
                break;
            case RockChisel.DELETE_BD:
                deleteOrganizationDB(organization.getId());
                break;
        }
    }

    //################ ANIMAL ################

    /**
     * Method for register the animal listener
     *
     * @param animalListener
     */
    public void setAnimalListener(AnimalListener animalListener){
        this.animalListener = animalListener;
    }

    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsList){
        //TODO:
    }

    @Override
    public void onUpdateAnimalsList(Animal animal, int operation){
        switch(operation){
            case RockChisel.INSERT_DB:
                insertAnimalDB(animal);
                break;
            case RockChisel.UPDATE_DB:
                updateAnimalDB(animal);
                break;
            case RockChisel.DELETE_BD:
                deleteAnimalDB(animal.getId());
                break;
        }
    }

    //################ ATTRIBUTES ################

    /**
     * Method for register the attribute listener
     * @param attributeListener
     */
    public void setAttributeListener(AttributeListener attributeListener){
        this.attributeListener = attributeListener;
    }


    //############################################# DB ACCESS METHODS ##################################################

    //################ ORGANIZATIONS ################

    /**
     * Get's all organizations from the SQLiteDatabase
     * @return
     */
    public ArrayList<Organization> getAllOrganizationsDB(){
        organizations = organizationsDBHelper.getAllOrganizationsDB();
        return organizations;
    }

    /**
     * Get the organization with the id passed by parameter
     * @param id
     * @return
     */
    public Organization getOrganization(int id){
        for (Organization organization : organizations) {
            if(organization.getId()== id){
                return organization;
            }
        }
        return null;
    }

    /**
     * Insert's an organization into the SQLiteDatabase
     * @param organization
     */
    public void insertOrganizationDB(Organization organization){
        organizationsDBHelper.insertOrganizationDB(organization);
        System.out.println("--> Organization inserted successfully on the DB");
    }

    /**
     * Update's an organization on the SQLiteDatabase
     * @param organization
     */
    public void updateOrganizationDB(Organization organization){

        if(!organizations.contains(organization)){
            return;
        }

        Organization auxOrg =  getOrganization(organization.getId());

        auxOrg.setId(organization.getId());
        auxOrg.setName(organization.getName());
        auxOrg.setNif(organization.getNif());
        auxOrg.setEmail(organization.getEmail());
        auxOrg.setPhone(organization.getPhone());
        auxOrg.setAddress_id(organization.getAddress_id());
        auxOrg.setStreet(organization.getStreet());
        auxOrg.setDoor_number(organization.getDoor_number());
        auxOrg.setFloor(organization.getFloor());
        auxOrg.setPostal_code(organization.getPostal_code());
        auxOrg.setStreet_code(organization.getStreet_code());
        auxOrg.setCity(organization.getCity());
        auxOrg.setDistrict_id(organization.getDistrict_id());
        auxOrg.setDistrict_name(organization.getDistrict_name());

        if(organizationsDBHelper.updateOrganizationDB(auxOrg)){
            System.out.println("--> organization updated successfully on the DB");
        }

    }

    /**
     * Delete's as organizations from the SQLite Database
     * @param id
     */
    public void deleteOrganizationDB(int id){
        Organization organization = getOrganization(id);

        if(organization != null){
            if(organizationsDBHelper.deleteOrganizationDB(organization.getId())){
                organizations.remove(organization);
                System.out.println("--> organization successfully deleted from the DB");
            }
        }
    }

    /**
     * Inserts all organizations into the SQLite Database
     * @param organizations
     */
    public void insertAllOrganizationsDB(ArrayList<Organization> organizations){
        organizationsDBHelper.deleteAllOrganizationsDB();

        for (Organization org : organizations) {
            insertOrganizationDB(org);
        }

    }


    //################ ANIMAL ################

    //Vai buscar todos os animais a BD
    public ArrayList<Animal> getAllAnimalsDB(){
        animals = organizationsDBHelper.getAllAnimalsDB();
        return animals;
    }

    //Vai buscar um animal a BD
    public Animal getAnimal(int id){
        for (Animal animal : animals) {
            if(animal.getId()== id){
                return animal;
            }
        }
        return null;
    }

    //Insere um animal na BD
    public void insertAnimalDB(Animal animal){
        organizationsDBHelper.insertAnimalDB(animal);
        System.out.println("--> Animal inserted successfully on the DB");
    }

    public void updateAnimalDB(Animal animal){

        if(!animals.contains(animal)){
            return;
        }

        Animal auxAnimal =  getAnimal(animal.getId());

        auxAnimal.setId(animal.getId());
        auxAnimal.setName(animal.getName());


        auxAnimal.setNature_id(animal.getNature_id());
        auxAnimal.setNature_name(animal.getNature_name());
        auxAnimal.setNature_parent_id(animal.getNature_parent_id());
        auxAnimal.setNature_parent_name(animal.getNature_parent_name());
        auxAnimal.setFur_length_id(animal.getFur_length_id());
        auxAnimal.setFur_length(animal.getFur_length());
        auxAnimal.setFur_color_id(animal.getFur_color_id());
        auxAnimal.setFur_color(animal.getFur_color());
        auxAnimal.setSize_id(animal.getSize_id());
        auxAnimal.setSize(animal.getSize());
        auxAnimal.setSex(animal.getSex());
        auxAnimal.setDescription(animal.getDescription());
        auxAnimal.setCreateAt(animal.getCreateAt());
        auxAnimal.setPhoto(animal.getPhoto());
        auxAnimal.setType(animal.getType());
        auxAnimal.setPublisher_id(animal.getPublisher_id());
        auxAnimal.setPublisher_name(animal.getPublisher_name());
        auxAnimal.setIs_fat(animal.getIs_fat());
        auxAnimal.setMissingFound_date(animal.getMissingFound_date());
        auxAnimal.setFoundAnimal_location_id(animal.getFoundAnimal_location_id());
        auxAnimal.setFoundAnimal_street(animal.getFoundAnimal_street());
        auxAnimal.setFoundAnimal_city(animal.getFoundAnimal_city());
        auxAnimal.setFoundAnimal_district_id(animal.getFoundAnimal_district_id());
        auxAnimal.setFoundAnimal_district_name(animal.getFoundAnimal_district_name());
        auxAnimal.setOrganization_id(animal.getOrganization_id());
        auxAnimal.setOrganization_name(animal.getOrganization_name());
        auxAnimal.setOrganization_nif(animal.getOrganization_nif());
        auxAnimal.setOrganization_email(animal.getOrganization_email());
        auxAnimal.setOrganization_address_id(animal.getOrganization_address_id());
        auxAnimal.setOrganization_street(animal.getOrganization_street());
        auxAnimal.setOrganization_door_number(animal.getOrganization_door_number());
        auxAnimal.setOrganization_floor(animal.getOrganization_floor());
        auxAnimal.setOrganization_city(animal.getOrganization_city());
        auxAnimal.setOrganization_postal_code(animal.getOrganization_postal_code());
        auxAnimal.setOrganization_street_code(animal.getOrganization_street_code());
        auxAnimal.setOrganization_district_id(animal.getOrganization_district_id());
        auxAnimal.setOrganization_district_name(animal.getOrganization_district_name());


        if(organizationsDBHelper.updateAnimalDB(animal)){
            System.out.println("--> animal updated successfully on the DB");
        }

    }

    //Deleta um animal da BD
    public void deleteAnimalDB(int id){
        Animal animal = getAnimal(id);

        if(animal != null){
            if(organizationsDBHelper.deleteAnimalDB(animal.getId())){
                animals.remove(animal);
                System.out.println("--> animal successfully deleted from the DB");
            }
        }
    }

    //Insere todos os animais na tabela Animais
    public void insertAllAnimalsDB(ArrayList<Animal> animalsList){
        organizationsDBHelper.deleteAllAnimalsDB();

        for (Animal animal : animals) {
            insertAnimalDB(animal);
        }

    }


    //############################################# API ACCESS METHODS ##################################################

    //################ ORGANIZATIONS ################

    /**
     * Get's all organizations from the API
     * @param context
     */
    public void getAllOrganizationsAPI(final Context context){

        if(!FortuneTeller.isThereInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();
            if(organizationsListener != null){
                organizationsListener.onRefreshOrganizationsList(organizationsDBHelper.getAllOrganizationsDB());
            }
        } else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIOrganizations, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    organizations = JsonParser.toOrganizations(response);
                    insertAllOrganizationsDB(organizations);

                    if (organizationsListener != null) {
                        organizationsListener.onRefreshOrganizationsList(organizations);
                    }

                    System.out.println("--> Organizations: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                    System.out.println("--> Organizations: " + Arrays.toString(error.getStackTrace()));
                }
            });
            volleyQueue.add(request);
        }
    }

    /**
     * Send's a request to the API to insert an organization
     * @param organization
     * @param context
     */
    public void insertOrganizationAPI(final Organization organization, final Context context){
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPIOrganizations,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Organization auxOrg = JsonParser.toOrganization(response);
                    onUpdateOrganizationsList(auxOrg, RockChisel.INSERT_DB);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", organization.getName());
                params.put("nif", organization.getNif());
                params.put("email", organization.getEmail());
                params.put("phone", organization.getPhone());
                params.put("address_id", "" + organization.getAddress_id());
                params.put("street", organization.getStreet());
                params.put("door_number", organization.getDoor_number());
                params.put("floor", organization.getFloor());
                params.put("postal_code", "" + organization.getPostal_code());
                params.put("street_code", "" + organization.getStreet_code());
                params.put("city", organization.getCity());
                params.put("district_id", "" + organization.getDistrict_id());
                params.put("district_name", organization.getDistrict_name());

                return params;
            }
        };
        volleyQueue.add(request);
    }

    /**
     * Send's a request to the API to update an organization
     * @param organization
     * @param context
     */
    public void updateOrganizationAPI(final Organization organization, final Context context){
        StringRequest request = new StringRequest(Request.Method.PUT, mUrlAPIOrganizations + "/" + organization.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Organization auxOrg = JsonParser.toOrganization(response);
                        onUpdateOrganizationsList(auxOrg, RockChisel.UPDATE_DB);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", organization.getName());
                params.put("nif", organization.getNif());
                params.put("email", organization.getEmail());
                params.put("phone", organization.getPhone());
                params.put("address_id", "" + organization.getAddress_id());
                params.put("street", organization.getStreet());
                params.put("door_number", organization.getDoor_number());
                params.put("floor", organization.getFloor());
                params.put("postal_code", "" + organization.getPostal_code());
                params.put("street_code", "" + organization.getStreet_code());
                params.put("city", organization.getCity());
                params.put("district_id", "" + organization.getDistrict_id());
                params.put("district_name", organization.getDistrict_name());

                return params;
            }
        };
        volleyQueue.add(request);
    }

    /**
     * Send a request to the API to delete an organization
     * @param organization
     * @param context
     */
    public void deleteOrganizationAPI(final Organization organization, final Context context){
        StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPIOrganizations + "/" + organization.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onUpdateOrganizationsList(organization, RockChisel.DELETE_BD);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        volleyQueue.add(request);
    }

    //################ ANIMAL ################

    //Vai buscar todos os animais há api
    public void getAllAnimalsAPI(final Context context){

        if(!FortuneTeller.isThereInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();

            //Carregar dados da base de dados
            if(animalListener != null){
                animalListener.onRefreshAnimalsList(organizationsDBHelper.getAllAnimalsDB());
            }
        } else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIAnimals, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    animals = JsonParser.toAnimals(response);
                    insertAllAnimalsDB(animals);

                    if (animalListener != null) {
                        animalListener.onRefreshAnimalsList(animals);
                    }

                    System.out.println("--> Animals: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                    System.out.println("--> Animals: " + error.getStackTrace());
                }
            });
            volleyQueue.add(request);
        }

    }

    /**
     * Send's a request to the API to insert an animal
     * @param animal to inserted on the API
     * @param apiService specific animal service on the API
     * @param context
     */
    public void insertAnimalAPI(final Animal animal, final String apiService, final String token, final Context context){

        JSONObject params = new JSONObject();

        try{
            if(Wrench.isNotNull(animal.getName()))
                params.put("name", "" + Wrench.purifyString(animal.getName()));

            if(Wrench.isNotNull(animal.getChipId()))
                params.put("chipId", "" + Wrench.purifyInteger(animal.getChipId()));

            params.put("description", "" + animal.getDescription());
            params.put("nature_id", "" + animal.getNature_id());
            params.put("fur_length_id", "" + animal.getFur_length_id());
            params.put("fur_color_id", "" + animal.getFur_color_id());
            params.put("size_id", "" + animal.getSize_id());
            params.put("sex", "" + animal.getSex());
            //params.put("photo", "" + animal.getPhoto());

            switch(apiService){
                case RockChisel.MISSING_ANIMALS_API_SERVICE:
                    params.put("missing_date", "20201220");
                    break;
                case RockChisel.FOUND_ANIMALS_API_SERVICE:
                    params.put("found_date", "20201220");
                    params.put("street", "" + animal.getFoundAnimal_street());
                    params.put("city", "" + animal.getFoundAnimal_city());
                    params.put("district_id", "" + animal.getFoundAnimal_district_id());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Animal[] auxAnimal = {null};

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_LOCAL_URL + apiService, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        switch (apiService){
                            case RockChisel.MISSING_ANIMALS_API_SERVICE:
                                auxAnimal[0] = JsonParser.missingAnimalToAnimal(response);
                                break;
                            case RockChisel.FOUND_ANIMALS_API_SERVICE:
                                auxAnimal[0] = JsonParser.missingAnimalToAnimal(response);
                                break;
                        }

                        if(auxAnimal[0] == null) {
                            requestListener.onRequestError("Erro ao fazer parse da resposta");
                        } else{
                            onUpdateAnimalsList(auxAnimal[0], RockChisel.INSERT_DB);
                            requestListener.onRequestSuccess();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errormessage = error.getMessage();
                        requestListener.onRequestError(errormessage);
                    }
                })
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer "+ token);
                        return headers;
                    }
                };
        volleyQueue.add(request);
    }

    /**
     * Send's a request to the API to insert an animal
     * @param animal to inserted on the API
     * @param apiService specific animal service on the API
     * @param context
     */
//    public void updateAnimalAPI(final Animal animal, final String apiService, final Context context){
//
//        StringRequest request = new StringRequest(Request.Method.PUT, API_LOCAL_URL + "/" + apiService + "/" + animal.getId(),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Animal auxAnimal = null;
//
//                        switch (apiService){
//                            case RockChisel.MISSING_ANIMALS_API_SERVICE:
//                                auxAnimal = JsonParser.missingAnimalToAnimal(response);
//                                break;
//                            case RockChisel.FOUND_ANIMALS_API_SERVICE:
//                                auxAnimal = JsonParser.missingAnimalToAnimal(response);
//                                break;
//
//                        }
//                        onUpdateAnimalsList(auxAnimal, RockChisel.UPDATE_DB);
//
//                        //TODO: update my animals list
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", "" + animal.getId());
//                params.put("nature_id", "" + animal.getNature_id());
//                params.put("nature_parent_id", "" + animal.getNature_parent_id());
//                params.put("fur_length_id", "" + animal.getFur_length_id());
//                params.put("fur_color_id", "" + animal.getFur_color_id());
//                params.put("size_id", "" + animal.getSize_id());
//                params.put("photo", "" + animal.getPhoto());
//                params.put("publisher_id", "" + animal.getPublisher_id());
//                params.put("organization_id", "" + animal.getOrganization_id());
//                params.put("organization_nif", "" + animal.getOrganization_nif());
//                params.put("organization_district_id", "" + animal.getOrganization_district_id());
//                params.put("organization_address_id", "" + animal.getOrganization_address_id());
//                params.put("organization_postal_code", "" + animal.getOrganization_postal_code());
//                params.put("organization_street_code", "" + animal.getOrganization_street_code());
//                params.put("foundAnimal_location_id", "" + animal.getFoundAnimal_location_id());
//                params.put("foundAnimal_district_id", "" + animal.getFoundAnimal_district_id());
//                params.put("is_fat;", "" + animal.getIs_fat());
//
//                params.put("chipId", "" + animal.getChipId());
//                params.put("description", "" + animal.getDescription());
//                params.put("nature_name", "" + animal.getNature_name());
//                params.put("nature_parent_name", "" + animal.getNature_parent_name());
//                params.put("fur_length", "" + animal.getFur_length());
//                params.put("fur_color", "" + animal.getFur_color());
//                params.put("size", "" + animal.getSize());
//                params.put("sex", "" + animal.getSex());
//                params.put("name", "" + animal.getName());
//                params.put("type", "" + animal.getType());
//                params.put("createAt", "" + animal.getCreateAt());
//                params.put("publisher_name", "" + animal.getPublisher_name());
//                params.put("missingFound_date", "" + animal.getMissingFound_date());
//                params.put("organization_name", "" + animal.getOrganization_name());
//                params.put("organization_email", "" + animal.getOrganization_email());
//                params.put("organization_street", "" + animal.getOrganization_street());
//                params.put("organization_city", "" + animal.getOrganization_city());
//                params.put("organization_district_name", "" + animal.getOrganization_district_name());
//                params.put("foundAnimal_street", "" + animal.getFoundAnimal_street());
//                params.put("foundAnimal_city", "" + animal.getFoundAnimal_city());
//                params.put("foundAnimal_district_name", "" + animal.getFoundAnimal_district_name());
//                params.put("organization_door_number", "" + animal.getOrganization_door_number());
//                params.put("organization_floor;", "" + animal.getOrganization_floor());
//
//                return params;
//            }
//        };
//        volleyQueue.add(request);
//    }

    /**
     * Send a request to the API to delete an animal
     * @param animal
     * @param apiService
     * @param context
     */
    public void deleteOrganizationAPI(final Animal animal, final String apiService, final Context context){
        StringRequest request = new StringRequest(Request.Method.DELETE, API_LOCAL_URL + "/" + apiService + "/" + animal.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onUpdateAnimalsList(animal, RockChisel.DELETE_BD);

                        //TODO: update my list animals
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        volleyQueue.add(request);
    }


    //################ ATTRIBUTTES ################

    /**
     * Get's all attributes from the API
     * @param context
     */
    public void getAttributesAPI(final Context context, final String attributeType, final String attSymLink, final @Nullable Integer id){

        if(!FortuneTeller.isThereInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();

            //TODO: a implementar o que fazer se não houver net

        } else{

            String jsonRequest = null;
            if(attributeType.equals(RockChisel.ATTR_SUBSPECIE)){
                jsonRequest = API_LOCAL_URL + RockChisel.ATTR_SPECIE + "/" + id + "/" + RockChisel.ATTR_SUBSPECIE;
            } else{
                jsonRequest = API_LOCAL_URL + attributeType;
            }

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, jsonRequest, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    attributes = JsonParser.toAttributes(response, attSymLink);

                    if (attributeListener != null) {
                        attributeListener.onReceivedAttributes(attributes, attributeType);
                    }

                    System.out.println("--> Attributes: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                    System.out.println("--> Attributes: " + error.getStackTrace());
                }
            });
            volleyQueue.add(request);
        }
    }

    //################ LOGIN ################

    /**
     * Method that makes a login request to the api and receives the access token
     * @param username
     * @param password
     * @param context
     */
    public void loginRequest(final String username, final String password, final Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPILogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String token = JsonParser.parserJsonLogin(response);
                        Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
                        loginListener.onValidLogin(token, username);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginListener.onInvalidLogin();
                        error.getStackTrace();
                    }
                })
//        {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                System.out.println("--> método getHeaders");
//                Map<String, String> headers = new HashMap<>();
//                headers.putIfAbsent("Cookie", "XDEBUG_SESSION_START=PHPSTORM");
//                headers.putIfAbsent("authorization", createBasicAuth(username, password));
//                return headers;
//            }
//        };
        {
            //Passagem dos dados por parametro no pedido à API
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(RockChisel.USERNAME, username);
                params.put(RockChisel.PASSWORD, password);
                return params;
            }
        };

        volleyQueue.add(request);
    }

    // Criar novo utilizador na API
    public void addUserAPI(final UserProfile userProfile, final Context context) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                mUrlAPIUserProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            UserProfile userProfileVindoDaAPI = JsonParser.parserJsonUserProfile(response);
                            userProfileListener.onUserProfileRequest(userProfileVindoDaAPI);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro na resposta.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                })
                {
//                    @RequiresApi(api = Build.VERSION_CODES.O)
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> headers = new HashMap<>();
//                        headers.putIfAbsent("authorization", createBasicAuth(userProfile.getUsername(),
//                                                                             userProfile.getPassword()));
//                        return headers;
//                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", userProfile.getEmail());
                        params.put("firstName", userProfile.getFirstName());
                        params.put("lastName", userProfile.getLastName());
                        params.put("nif", userProfile.getNif());
                        params.put("phone", userProfile.getPhone());
                        params.put("street", userProfile.getStreet());
                        params.put("door_number", userProfile.getDoorNumber());
                        params.put("floor", userProfile.getFloor());
                        params.put("postal_code", userProfile.getPostalCode());
                        params.put("street_code", userProfile.getStreetCode());
                        params.put("city", userProfile.getCity());
                        params.put("district_id", "" + userProfile.getDistrictId());

                        return params;
                    }
                };
        volleyQueue.add(request);
    }

    // Converter para base64 username e password
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createBasicAuth(final String username, final String password) {
        final String pair = username + ":" + password;

        return "Basic " + Base64.encodeToString(pair.getBytes(), Base64.DEFAULT);
    }

}
