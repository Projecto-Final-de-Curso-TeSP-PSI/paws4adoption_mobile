package amsi.dei.estg.ipleiria.paws4adoption.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.DatePicker;

import java.util.Calendar;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Class tha implements several static method that iun one way or another works as tools
 */
public class Wrench {

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;


    /**
     * Enconde0's an bmp to aBase64 String
     * @param bmp
     * @return
     */
    public static String bmpToBase64(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 1, baos);
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

    /**
     * Get a date from date picker
     * @param datePicker
     * @return
     */
    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public static boolean isNull(String str) {
        if (str == null || str.length() == 0 || str.equals("null"))
            return true;

        return false;
    }

    public static boolean isNotNull(String str) {
        if (str == null || str.length() == 0 || str.equals("null"))
            return false;

        return true;
    }

    public static String zeroToLeft(int intValue){
        return intValue < 10 ? "0" + intValue : "" + intValue;
    }

    public static String getTodaysDate() {
        Calendar calendar =  Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int year = calendar.get(Calendar.YEAR);

        return makeDateString_ddMMyyyy(day, month, year);
    }

    public static String makeDateString_ddMMyyyy(int day, int month, int year) {
        return Wrench.zeroToLeft(day) + "-" + Wrench.zeroToLeft(month) + "-" + year;
    }

    public static String makeDateString_ddMMyyyy(String strDate_yyyyMMdd){
        String[] parcels = strDate_yyyyMMdd.split("-");
        return parcels[2] + "-" + parcels[1] + "-" + parcels[0];
    }

    public static String makeDateString_yyyyMMdd(String strDate_ddMMyyy) {
        String[] parcels = strDate_ddMMyyy.split("-");
        return parcels[2] + "-" + parcels[1] + "-" + parcels[0];
    }

    public static int getDatePart_yyyyMMdd(String strDate, int part){
        String[] parcels = strDate.split("-");
        Integer result;

        try{
            result = Integer.parseInt(parcels[part]);
        } catch(Exception e){
            return -1;
        }
        return result;
    }

}

