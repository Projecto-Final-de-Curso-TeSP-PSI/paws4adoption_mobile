package amsi.dei.estg.ipleiria.paws4adoption.utils;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Api;

import java.util.Optional;

/**
 * Class tha implements several static method that iun one way or another works as tools
 */
public class Wrench {

    /**
     * Purifies a given string from null values to an String
     * @param str the input string
     * @return null if it is a "null", or returns it self otherwise
     */
    public static String purifyString(String str) {
        if (str.equals("null"))
            return null;
        else
            return str;
    }

    /**
     * Purifies a given string from null values to an integer
     * @param str the input string
     * @return null if it is a "null", or returns the integer parse
     */
    public static Integer purifyInteger(String str) {
        if (str == null || str.length() == 0 || str.equals("null"))
            return null;
        else
            return Integer.parseInt(str);
    }

    /**
     * Encode's the string on the database with a before and after aditional string
     * Cleans the null values from the data
     * @param before the before value to be added to the string
     * @param str the string to be encoded am
     * @param after teh after value to be added to the string
     * @return
     */
    public static String encode(String before, String str, String after) {
            return (before == null ? "" : before) + (str == null ? "" : str)  + (after == null ? "" : after);
    }

}

