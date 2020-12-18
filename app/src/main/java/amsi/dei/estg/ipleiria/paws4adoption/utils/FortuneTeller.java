package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Class that implements several static methods that make inquirings
 */
public class FortuneTeller {

    /**
     * Checks if the mobile equipment has internet connection
     * @param context
     * @return
     */
    public static boolean isThereInternetConnection(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Tells if there is a logged user
     *
     * @return string|null
     */
    public static boolean isThereLoggedUser(Context context) {
        String username;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(RockChisel.USER_PREFERENCES, Context.MODE_PRIVATE);

            username = sharedPreferences.getString(RockChisel.USERNAME, null);

        } catch(Exception e){
            return false;
        }

        return !(username == null);
    }

}
