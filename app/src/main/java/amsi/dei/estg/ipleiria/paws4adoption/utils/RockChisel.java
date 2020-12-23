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
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    //Database related constants
    public static final int DB_VERSION =1;
    public static final String DB_NAME = "paws4adoptionDB";
    public static final int INSERT_DB = 1;
    public static final int UPDATE_DB = 2;
    public static final int DELETE_BD = 3;

    //Attributes related constants

    //API Service
    public static final String ATTR_NATURE = "nature";
    public static final String ATTR_FUR_COLOR = "fur-color";
    public static final String ATTR_FUR_LENGTH = "fur-length";
    public static final String ATTR_SIZE = "size";

    //API SymLink
    public static final String ATTR_NATURE_SYMLINK = "name";
    public static final String ATTR_FUR_COLOR_SYMLINK = "fur_color";
    public static final String ATTR_FUR_LENGTH_SYMLINK = "fur_length";
    public static final String ATTR_SIZE_SYMLINK = "size";
}
