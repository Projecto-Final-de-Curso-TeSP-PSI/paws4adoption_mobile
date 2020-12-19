package amsi.dei.estg.ipleiria.paws4adoption.models;

import android.content.Context;
import java.util.Base64;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.utils.JsonParser;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

public class SingletonPawsManager {

    //API local address (may change each time you start your machine)
    private static final String COMPUTER_LOCAL_IP = "10.0.2.2";

    //private static final String COMPUTER_LOCAL_IP = "localhost";
    private static final String API_LOCAL_URL = "http://" + COMPUTER_LOCAL_IP + "/pet-adoption/paws4adoption_web/backend/web/api/";

    //Singleton instance
    private static SingletonPawsManager instance = null;

    //Volley static queue
    private static RequestQueue volleyQueue = null;

    //Listeners declaration
    LoginListener loginListener;
    UserProfileListener userProfileListener;

    //Endpoints for requests
    private static final String mUrlAPILogin = API_LOCAL_URL + "users/token";
    private static final String mUrlAPIUserProfile = API_LOCAL_URL + "users";

    //Static constants declaration for DB
    private static final int INSERT_DB = 1;
    private static final int UPDATE_DB = 2;
    private static final int DELETE_BD = 3;

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
        //TODO: register animals helper
    }

    /**
     * Method for register the login listener
     *
     * @param loginListener
     */
    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setUserProfileListener(UserProfileListener userProfileListener){
        this.userProfileListener = userProfileListener;
    }
    //############################################# API ##################################################


    //################ LOGIN ################

    /**
     * Method that makes a login request to the api and receives the access token
     * @param username
     * @param password
     * @param context
     */
    public void loginRequest(final String username, final String password, final Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPILogin + "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String token = JsonParser.parserJsonLogin(response);
                        loginListener.onValidLogin(token, username);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro de login", Toast.LENGTH_SHORT).show();
                    }
                }) {
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
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("token", userProfile.getToken());
                        params.put("email", userProfile.getEmail());
                        params.put("username", userProfile.getUsername());
                        params.put("firstName", userProfile.getFirstName());
                        params.put("lastName", userProfile.getLastName());
                        params.put("nif", userProfile.getNif());
                        params.put("phone", userProfile.getPhone());
                        params.put("street", userProfile.getStreet());
                        params.put("doorNumber", userProfile.getDoorNumber());
                        params.put("floor", userProfile.getFloor());
                        params.put("postalCode", userProfile.getPostalCode());
                        params.put("streetCode", userProfile.getStreetCode());
                        params.put("city", userProfile.getCity());
                        params.put("districtId", userProfile.getDistrictId());
                        return params;
                    }
                };
    }

    // Converter para base64 username e password
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createBasicAuth(final String username,
                                     final String password)
    {
        final String pair = username + ":" + password;

        Base64.Encoder encoder = Base64.getEncoder();

        return "Basic " + encoder.encodeToString(pair.getBytes());
    }
}
