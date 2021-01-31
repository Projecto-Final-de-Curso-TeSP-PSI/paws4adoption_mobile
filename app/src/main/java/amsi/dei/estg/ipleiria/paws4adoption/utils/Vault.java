package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import amsi.dei.estg.ipleiria.paws4adoption.models.Login;

/**
 * Class that implements several static methods to work with the shared preferences
 */
public class Vault {

    /**
     * Method for saving the USERNAME and TOKEN on the shared preferences
     * @param context
     * @param login
     * @param username
     */
    public static void saveUserPreferences(Context context, Login login, String username){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(RockChisel.USER_PREFERENCES,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RockChisel.USERNAME, username);
        editor.putString(RockChisel.TOKEN, login.getToken());
        editor.putInt(RockChisel.ID_USER, login.getId());
        editor.apply();
    }

    /**
     * Method that clear a preference group in the shared preferences
     * @param context
     * @param group
     */
    public static void clearPreferences(Context context, String group){
        SharedPreferences sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    /**
     * CL
     * @param context
     */
    public static void clearUserPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(RockChisel.USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    /**
     * Returns the logged user, or null if not exists
     *
     * @return string|null
     */
    public static int getIdLoggedUser(Context context) {
        int id;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(RockChisel.USER_PREFERENCES, Context.MODE_PRIVATE);
            id = sharedPreferences.getInt(RockChisel.ID_USER, 0);
        } catch(Exception e){
            System.out.println("getIdLoggedUser error --> " + e);
            return 0;
        }
        return id;
    }


    /**
     * Returns the logged user, or null if not exists
     *
     * @return string|null
     */
    public static String getLoggedUsername(Context context) {
        String username;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(RockChisel.USER_PREFERENCES, Context.MODE_PRIVATE);
            username = sharedPreferences.getString(RockChisel.USERNAME, null);
        } catch(Exception e){
            return null;
        }
        return username;
    }

    /**
     * Get's the auth token of the logged user
     * @param context
     * @return teh token
     */
    public static String getAuthToken(Context context) {
        String token;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(RockChisel.USER_PREFERENCES, Context.MODE_PRIVATE);
            token = sharedPreferences.getString(RockChisel.TOKEN, null);
        } catch(Exception e){
            return null;
        }
        return token;
    }

}
