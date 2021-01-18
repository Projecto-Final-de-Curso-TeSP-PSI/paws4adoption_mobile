package amsi.dei.estg.ipleiria.paws4adoption.models;

import android.content.Context;

import java.util.ArrayList;
//import java.util.Base64;

import android.os.Build;
import android.widget.ArrayAdapter;
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

import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalDetailListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalsListListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AttributeListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.OrganizationDetailListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.OrganizationsListListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.RequestListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.SignupListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UploadPhotoListener;
import amsi.dei.estg.ipleiria.paws4adoption.utils.JsonParser;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

public class SingletonPawsManager implements OrganizationsListListener, AnimalsListListener {

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
    SignupListener signupListener;
    UploadPhotoListener uploadPhotoListener;
    UserProfileListener userProfileListener;
    OrganizationsListListener organizationsListListener;
    OrganizationDetailListener organizationDetailListener;
    AnimalsListListener animalsListListener;
    AnimalDetailListener animalDetailListener;
    AttributeListener attributeListener;
    RequestListener requestListener;

    //BD Helper's declaration
    PawsManagerDBHelper pawsManagerDBHelper;

    //Endpoints for requests
    private static final String mUrlAPILogin = RockChisel.API_LOCAL_URL + "users/token";
    private static final String mUrlAPIUserProfile = RockChisel.API_LOCAL_URL + "users";
    private static final String mUrlAPIOrganizations = RockChisel.API_LOCAL_URL + "organizations";
    private static final String mUrlAPIAnimals = RockChisel.API_LOCAL_URL + "animals";
    private static final String mUrlAPIMissingAnimals = RockChisel.API_LOCAL_URL + "missing-animals";
    private static final String mUrlAPIFoundAnimal = RockChisel.API_LOCAL_URL + "found-animals";


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
        this.pawsManagerDBHelper = new PawsManagerDBHelper(context);
    }


    //############################################# LISTENERS IMPLEMENTATION ##################################################

    /**
     * Method for register the organization listener
     *
     * @param organizationsListListener
     */
    public void setOrganizationsListListener(OrganizationsListListener organizationsListListener){
        this.organizationsListListener = organizationsListListener;
    }

    /**
     * Method for register the organization detail listener
     *
     * @param organizationDetailListener
     */
    public void setOrganizationDetailListener(OrganizationDetailListener organizationDetailListener){
        this.organizationDetailListener = organizationDetailListener;
    }

    /**
     * Method for register the login listener
     *
     * @param loginListener
     */
    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    /**
     * Method for register the signup listener
     *
     * @param signupListener
     */
    public void setSignupListener(SignupListener signupListener) {
        this.signupListener = signupListener;
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

    /**
     * Method for register the animal listener
     *
     * @param animalsListListener
     */
    public void setAnimalsListListener(AnimalsListListener animalsListListener){
        this.animalsListListener = animalsListListener;
    }

    /**
     * Method for register the animal listener
     *
     * @param animalsDetailListener
     */
    public void setAnimalDetailListener(AnimalDetailListener animalsDetailListener){
        this.animalDetailListener = animalsDetailListener;
    }

    //################ ORGANIZATIONS ################

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

    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsList){

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

    @Override
    public void onCreateAnimalFromList() {

    }

    @Override
    public void onUpdateAnimalFromList() {

    }

    @Override
    public void onDeleteAnimalFromList() {

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
        this.organizations = pawsManagerDBHelper.getAllOrganizationsDB();
        return this.organizations;
    }

    /**
     * Get the organization with the id passed by parameter
     * @param id
     * @return
     */
    public Organization getOrganizationDB(int id){
        for (Organization organization : organizations) {
            if(organization.getId()== id){
                return organization;
            }
        }
        return null;
    }

    /**
     * Inserts all organizations into the SQLite Database
     * @param organizations
     */
    public void insertAllOrganizationsDB(ArrayList<Organization> organizations){
        pawsManagerDBHelper.deleteAllOrganizationsDB();

        for (Organization org : organizations) {
            insertOrganizationDB(org);
        }

    }

    /**
     * Insert's an organization into the SQLiteDatabase
     * @param organization
     */
    public void insertOrganizationDB(Organization organization){
        pawsManagerDBHelper.insertOrganizationDB(organization);
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

        Organization auxOrg =  getOrganizationDB(organization.getId());

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

        if(pawsManagerDBHelper.updateOrganizationDB(auxOrg)){
            System.out.println("--> organization updated successfully on the DB");
        }

    }

    /**
     * Delete's as organizations from the SQLite Database
     * @param id
     */
    public void deleteOrganizationDB(int id){
        Organization organization = getOrganizationDB(id);

        if(organization != null){
            if(pawsManagerDBHelper.deleteOrganizationDB(organization.getId())){
                organizations.remove(organization);
                System.out.println("--> organization successfully deleted from the DB");
            }
        }
    }

    //################ ANIMAL ################

    /**
     * Get's all animals from the SQLiteDatabase
     * @return
     */
    public ArrayList<Animal> getAllAnimalsDB(){
        this.animals = pawsManagerDBHelper.getAllAnimalsDB();
        return this.animals;
    }

    /**
     * Get the animal with the id passed by parameter
     * @param id
     * @return
     */
    public Animal getAnimalDB(int id){

        for (Animal animal : animals) {
            if(animal.getId()== id){
                return animal;
            }
        }
        return null;

    }

    /**
     * Insert's an animal into the SQLiteDatabase
     * @param animal the animal to be inserted
     */
    public void insertAnimalDB(Animal animal){
        if(pawsManagerDBHelper.insertAnimalDB(animal))
            System.out.println("--> Animal inserted successfully on the DB");
    }

    /**
     * Update's an aniamls on the SQLiteDatabase
     * @param animal
     */
    public void updateAnimalDB(Animal animal){

        if(animals == null){
            return;
        }




//        if(!animals.inde(animal)){
//            return;
//        }





//        for (Animal animalArr : animals
//        ) {
//            int animalIdArrau = animalArr.getId();
//            int animalAPI = animal.getId();
//
//            if(animalAPI == animalIdArrau){
//                animalArr = animal;
//                int a =1;
//            }
//        }




            Animal auxAnimal =  getAnimalDB(animal.getId());
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

        if(pawsManagerDBHelper.updateAnimalDB(auxAnimal))
            System.out.println("--> animal updated successfully on the DB");

    }

    /**
     * Delete's an animal from the SQLite Database
     * @param id
     */
    public void deleteAnimalDB(int id){
        Animal animal = getAnimalDB(id);

        if(animal != null){
            if(pawsManagerDBHelper.deleteAnimalDB(animal.getId())){
                animals.remove(animal);
                System.out.println("--> animal successfully deleted from the DB");
            }
        }
    }

    /**
     * Inserts all animals into the SQLite Database
     * @param animalsList
     */
    public void insertAllAnimalsDB(ArrayList<Animal> animalsList){
        pawsManagerDBHelper.deleteAllAnimalsDB();
        for (Animal animal : animalsList) {
            //insertAnimalDB(animal);
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

        if(!FortuneTeller.isInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();
            if(organizationsListListener != null){
                organizationsListListener.onRefreshOrganizationsList(this.getAllOrganizationsDB());
            }
        } else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIOrganizations, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    organizations = JsonParser.toOrganizations(response);
                    if(organizations != null){
                        insertAllOrganizationsDB(organizations);
                        if (organizationsListListener != null) {
                            organizationsListListener.onRefreshOrganizationsList(organizations);
                        }
                        System.out.println("--> Organizations: " + response);
                    } else {
                        //requestListener.onRequestError("Erro ao fazer o parse das organizações");
                        Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("--> Organizations: " + Arrays.toString(error.getStackTrace()));
                    //requestListener.onRequestError("Erro na obtenção das organizações: " + error.getMessage());
                    Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(request);
        }
    }

    /**
     * Get's one animal from teh API
     * @param context
     */
    public void getOrganizationAPI(final Context context, final int organization_id){

        if(!FortuneTeller.isInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();
            //Carregar dados da base de dados
            if (organizationDetailListener != null) {
                organizationDetailListener.onGetOrganization(this.getOrganizationDB(organization_id));
            }

        } else {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrlAPIOrganizations + "/" + organization_id, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Organization organization = JsonParser.toOrganization(response);
                            if (organization != null) {
                                if (organizationDetailListener != null) {
                                    organizationDetailListener.onGetOrganization(organization);
                                }
                                System.out.println("--> Organization: " + response);
                            } else {
                                //requestListener.onRequestError("Erro ao obter lista de animais da API");
                                Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //requestListener.onRequestError("Erro ao obter a lista de animais da API");
                            Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                            System.out.println("--> Organization: " + Arrays.toString(error.getStackTrace()));
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
    public void insertOrganizationAPI(final Organization organization, final String token, final Context context){

        JSONObject bodyParameters = this.getOrganizationParameters(organization);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, mUrlAPIOrganizations, bodyParameters,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Organization auxOrg = JsonParser.toOrganization(response);
                    if(auxOrg != null){
                        onUpdateOrganizationsList(auxOrg, RockChisel.INSERT_DB);
                    } else{
                        //TODO: do something
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
            }
        };
        volleyQueue.add(request);
    }

    /**
     * Send's a request to the API to update an organization
     * @param organization
     * @param context
     */
    public void updateOrganizationAPI(final Organization organization, final String token, final Context context) {

        JSONObject bodyParameters = this.getOrganizationParameters(organization);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, mUrlAPIOrganizations + "/" + organization.getId(), bodyParameters,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Organization auxOrg = JsonParser.toOrganization(response);
                    if(auxOrg != null){
                        onUpdateOrganizationsList(auxOrg, RockChisel.UPDATE_DB);
                    } else{
                         //TODO: do something
                     }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
        volleyQueue.add(request);
    }

    /**
     * Send a request to the API to delete an organization
     * @param organization
     * @param context
     */
    public void deleteOrganizationAPI(final Organization organization, final String token, final Context context){
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
                })
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                }
        };
        volleyQueue.add(request);
    }

    /**
     * Get's a JSON Object of the organization parameters
     * @param organization the organizations paramenters to include
     * @returns the organization parameters
     */
    public JSONObject getOrganizationParameters(Organization organization){
        JSONObject params = new JSONObject();
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return params;
    }

    //################ ANIMAL ################

    /**
     * Get's all animals from the API
     * @param context
     */
    public void getAllAnimalsAPI(final Context context){

        if(!FortuneTeller.isInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();
            //Carregar dados da base de dados
            if(animalsListListener != null){
                animalsListListener.onRefreshAnimalsList(this.getAllAnimalsDB());
            }
        } else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIAnimals, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    animals = JsonParser.toAnimals(response);
                    if(animals != null){
                        insertAllAnimalsDB(animals);
                        if (animalsListListener != null) {
                            animalsListListener.onRefreshAnimalsList(animals);
                        }
                        System.out.println("--> Animals: " + response);
                    } else {
                        Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                        //requestListener.onRequestError("Erro ao obter lista de animais da API");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //requestListener.onRequestError("Erro ao obter a lista de animais da API");
                    Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                    System.out.println("--> Animals: " + Arrays.toString(error.getStackTrace()));
                }
            });
            volleyQueue.add(request);
        }
    }

    /**
     * Get's one animal from teh API
     * @param context
     */
    public void getAnimalAPI(final Context context, final int animal_id){

        if(!FortuneTeller.isInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();
            //Carregar dados da base de dados
            if(animalDetailListener != null){
                animalDetailListener.onGetAnimal(this.getAnimalDB(animal_id));
            }

        } else {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrlAPIAnimals + "/" + animal_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Animal auxAnimal = JsonParser.toAnimal(response);
                        if (auxAnimal != null) {
                            if (animalDetailListener != null) {
                                animalDetailListener.onGetAnimal(auxAnimal);
                            }

                            if(requestListener != null){
                                requestListener.onReadAnimal(auxAnimal);
                            }

                            System.out.println("--> Animals: " + response);
                        } else {
                            //requestListener.onRequestError("Erro ao obter lista de animais da API");
                            Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //requestListener.onRequestError("Erro ao obter a lista de animais da API");
                        Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                        System.out.println("--> Animals: " + Arrays.toString(error.getStackTrace()));
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
        JSONObject bodyParameters = this.getAnimalParameters(animal, apiService, RockChisel.ACTION_CREATE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RockChisel.API_LOCAL_URL + apiService, bodyParameters,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Animal auxAnimal = JsonParser.toAnimal(response);
                    if(auxAnimal != null) {

                        if(animals == null){
                            animals = new ArrayList<>();
                        }
                        animals.add(auxAnimal);

                        onUpdateAnimalsList(auxAnimal, RockChisel.INSERT_DB);
                        if(requestListener != null) {
                            requestListener.onCreateAnimal();
                        }

                        if (animalsListListener != null) {
                            animalsListListener.onRefreshAnimalsList(animals);
                            animalsListListener.onCreateAnimalFromList();
                        }

                    } else{
                        //requestListener.onRequestError("Erro ao inserir animal na API");
                        Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                     String errormessage = error.getMessage();
                    //requestListener.onRequestError(errormessage);
                    Toast.makeText(context, "Erro ao comunicar com a API", Toast.LENGTH_SHORT).show();
                    System.out.println("--> Animals: " + Arrays.toString(error.getStackTrace()));
                }
            })
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
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
    public void updateAnimalAPI(final Animal animal, final String apiService, final String token, final Context context){
        JSONObject bodyParameters = this.getAnimalParameters(animal, apiService, RockChisel.ACTION_UPDATE);

        String serviceUrl = RockChisel.API_LOCAL_URL + apiService + "/" + animal.getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, serviceUrl, bodyParameters,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Animal auxAnimal = JsonParser.toAnimal(response);
                    if(auxAnimal != null){
                        onUpdateAnimalsList(auxAnimal, RockChisel.UPDATE_DB);

                        if(requestListener != null){
                            requestListener.onUpdateAnimal();
                        }

                        if (animalsListListener != null) {

                            ArrayList<Animal> newAnimalsList =  pawsManagerDBHelper.getAllAnimalsDB();

                            animalsListListener.onRefreshAnimalsList(newAnimalsList);
                            animalsListListener.onUpdateAnimalFromList();
                        }

                        Toast.makeText(context, "Animal atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        //requestListener.onRequestSuccess("Animal atualizado com sucesso");
                    } else {
                        //requestListener.onRequestError("Erro ao atualizar o animal");
                        Toast.makeText(context, "Prrsing error", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer "+ token);
                    return headers;
            }
        };
        volleyQueue.add(request);
    }

    /**
     * Send a request to the API to delete an animal
     * @param animal
     * @param apiService
     * @param context
     */
    public void deleteAnimalAPI(final Animal animal, final String apiService, final String token, final Context context){

        String serviceUrl = RockChisel.API_LOCAL_URL + apiService + "/" + animal.getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, serviceUrl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Animal auxAnimal = JsonParser.toAnimal(response);

                    onUpdateAnimalsList(auxAnimal, RockChisel.DELETE_BD);

                    if(animalDetailListener != null){
                        animalDetailListener.onDeleteAnimalFromList();
                    }

                    if (animalsListListener != null) {
                        animalsListListener.onRefreshAnimalsList(animals);
                        animalsListListener.onDeleteAnimalFromList();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer "+ token);
                    return headers;
                }
            };
        volleyQueue.add(request);
    }

    /**
     * Get's a JSON Object of the animal parameters
     * @param animal
     * @param apiService
     * @return
     */
    public JSONObject getAnimalParameters(Animal animal, String apiService, @Nullable String actionType){
        JSONObject params = new JSONObject();
        try {
            if(actionType == RockChisel.ACTION_UPDATE){
                params.put("id", animal.getId());
            }
            params.put("name", animal.getName());
            params.put("chipId", animal.getChipId());
            params.put("description", animal.getDescription());
            params.put("nature_id", animal.getNature_id());
            params.put("fur_length_id", animal.getFur_length_id());
            params.put("fur_color_id", animal.getFur_color_id());
            params.put("size_id", animal.getSize_id());
            params.put("sex", animal.getSex());

            if(animal.getPhoto() != null){
                params.put("photo", "" + animal.getPhoto());
            }
            switch(apiService){
                case RockChisel.MISSING_ANIMALS_API_SERVICE:
                    params.put("missing_date", "20201220");
                    break;
                case RockChisel.FOUND_ANIMALS_API_SERVICE:
                    params.put("found_date", "20201220");
                    params.put("street", animal.getFoundAnimal_street());
                    params.put("city", animal.getFoundAnimal_city());
                    params.put("district_id", animal.getFoundAnimal_district_id());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return params;
    }


    //################ ATTRIBUTTES ################

    /**
     * Get's all attributes from the API
     * @param context
     */
    public void getAttributesAPI(final Context context, final String attributeType, final String attSymLink, final @Nullable Integer id){

        if(!FortuneTeller.isInternetConnection(context)){
            Toast.makeText(context, "Não existe ligação à internet", Toast.LENGTH_SHORT).show();

            //TODO: a implementar o que fazer se não houver net

        } else{

            String jsonRequest = null;
            if(attributeType.equals(RockChisel.ATTR_SUBSPECIE)){
                jsonRequest = RockChisel.API_LOCAL_URL + RockChisel.ATTR_SPECIE + "/" + id + "/" + RockChisel.ATTR_SUBSPECIE;
            } else{
                jsonRequest = RockChisel.API_LOCAL_URL + attributeType;
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
    public void tokenRequest(final String username, final String password, final Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPILogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Login login = JsonParser.parserJsonLogin(response);
                        Toast.makeText(context, "Login efetuado com sucesso! Bem-vindo " + username, Toast.LENGTH_SHORT).show();
                        loginListener.onValidLogin(login, username);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginListener.onInvalidLogin();
                        error.getStackTrace();
                    }
                })
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

    /**
     * Method that creates a new user on the API
     * @param userProfile
     * @param context
     */
    public void addUserAPI(final UserProfile userProfile, final Context context) {
        JSONObject params = new JSONObject();
        try {
            params.put("username", userProfile.getUsername());
            params.put("password", userProfile.getPassword());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                mUrlAPIUserProfile,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserProfile userProfileVindoDaAPI = JsonParser.parserJsonUserProfile(response);
                            signupListener.onSuccessfullSignup(userProfileVindoDaAPI);
                        } catch (Exception e){
                            System.out.println("--> Lançou excepção ao receber resposta.");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        signupListener.onSignupFail();
                        System.out.println("--> Recebido erro.");
                        error.printStackTrace();
                    }
                })
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

        volleyQueue.add(request);
    }
}
