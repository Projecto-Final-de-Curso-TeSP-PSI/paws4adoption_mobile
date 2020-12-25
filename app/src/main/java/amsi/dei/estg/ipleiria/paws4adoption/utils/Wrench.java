package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.DatePicker;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Api;

import java.util.Calendar;
import java.util.Optional;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import amsi.dei.estg.ipleiria.paws4adoption.views.LoginActivity;

/**
 * Class tha implements several static method that iun one way or another works as tools
 */
public class Wrench {

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

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
        else if (str.equals("false")){
            return 0;
        }else if (str.equals("true")){
            return 1;
        }else
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



    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

}

