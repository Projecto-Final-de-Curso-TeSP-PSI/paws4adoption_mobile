package amsi.dei.estg.ipleiria.paws4adoption.models;

import android.content.ContentValues;
import android.content.Context;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Base64;
import android.util.Base64;

import android.media.MediaSync;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.OrganizationsListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.utils.JsonParser;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

public class SingletonPawsManager implements OrganizationsListener{

    //API local address (may change each time you start your machine)
    private static final String COMPUTER_LOCAL_IP = "10.0.2.2";
//    private static final String COMPUTER_LOCAL_IP = "192.168.42.129";

    //private static final String COMPUTER_LOCAL_IP = "localhost";
    private static final String API_LOCAL_URL = "http://" + COMPUTER_LOCAL_IP + "/pet-adoption/paws4adoption_web/backend/web/api/";

    //Singleton instance
    private static SingletonPawsManager instance = null;

    //DB list's declaration
    ArrayList<Organization> organizations;

    //Volley static queue
    private static RequestQueue volleyQueue = null;

    //Listeners declaration
    LoginListener loginListener;
    UserProfileListener userProfileListener;
    OrganizationsListener organizationsListener;

    //BD Helper's declaration
    OrganizationsDBHelper organizationsDBHelper = null;

    //Endpoints for requests
    private static final String mUrlAPILogin = API_LOCAL_URL + "users/token";
    private static final String mUrlAPIUserProfile = API_LOCAL_URL + "users";
    private static final String mUrlAPIOrganizations = API_LOCAL_URL + "organizations";

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

                    System.out.println("--> Organizations: " + error.getStackTrace());
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
                        Toast.makeText(context, "Erro de login", Toast.LENGTH_SHORT).show();
                        error.getStackTrace();
                    }
                })
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                System.out.println("--> método getHeaders");
                Map<String, String> headers = new HashMap<>();
                headers.putIfAbsent("Cookie", "XDEBUG_SESSION_START=PHPSTORM");
                headers.putIfAbsent("authorization", createBasicAuth(username, password));
                return headers;
            }
        };
        /*{
            //Passagem dos dados por parametro no pedido à API
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(RockChisel.USERNAME, username);
                params.put(RockChisel.PASSWORD, password);
                return params;
            }
        };*/

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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.putIfAbsent("authorization", createBasicAuth(userProfile.getUsername(),
                                                                             userProfile.getPassword()));
                        return headers;
                    }

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
