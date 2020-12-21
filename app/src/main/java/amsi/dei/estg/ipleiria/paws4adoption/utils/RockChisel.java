package amsi.dei.estg.ipleiria.paws4adoption.utils;


/**
 * Class that makes available a set of global constants
 */
public class RockChisel {

    //Package related constants
    private static final String PACKAGE_NAME = "amsi.dei.estg.ipleiria.paws4adoption";

    //Location related constants
    public static final String RESULT_DATAKEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    //Simple Success/Failure constants
    public static final int SUCCESS_RESULT = 1;
    public static final int FAILURE_RESULT = 0;

    //Login related constants
    public static final String USER_PREFERENCES = "user_preferences";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    //Database related constants
    public static final int DB_VERSION =1;
    public static final String DB_NAME = "paws4adoptionDB";
    public static final int INSERT_DB = 1;
    public static final int UPDATE_DB = 2;
    public static final int DELETE_BD = 3;
}
