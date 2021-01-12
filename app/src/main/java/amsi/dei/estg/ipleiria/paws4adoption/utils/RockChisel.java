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
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "paws4adoptionDB";
    public static final int INSERT_DB = 1;
    public static final int UPDATE_DB = 2;
    public static final int DELETE_BD = 3;

    //Attributes related constants

    //API Services
    public static final String ATTR_SPECIE = "species";
    public static final String ATTR_SUBSPECIE = "sub-species";
    public static final String ATTR_FUR_COLOR = "fur-colors";
    public static final String ATTR_FUR_LENGTH = "fur-lengths";
    public static final String ATTR_SIZE = "sizes";
    public static final String ATTR_DISTRICT = "districts";

    public static final String MISSING_ANIMALS_API_SERVICE = "missing-animals";
    public static final String FOUND_ANIMALS_API_SERVICE = "found-animals";

    //API SymLink
    public static final String ATTR_SPECIE_SYMLINK = "name";
    public static final String ATTR_SUBSPECIE_SYMLINK = "name";
    public static final String ATTR_FUR_COLOR_SYMLINK = "fur_color";
    public static final String ATTR_FUR_LENGTH_SYMLINK = "fur_length";
    public static final String ATTR_SIZE_SYMLINK = "size";
    public static final String ATTR_DISTRICT_SYMLINK = "name";

    //Scenarios
    public static final String SCENARIO_ADOPTION_ANIMAL = "adoptionAnimal";
    public static final String SCENARIO_MISSING_ANIMAL = "missingAnimal";
    public static final String SCENARIO_FOUND_ANIMAL = "foundAnimal";

    //Actions
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";

    //Animal types
    public static final String ADOPTION_ANIMAL = "adoptionAnimal";
    public static final String MISSING_ANIMAL = "missingAnimal";
    public static final String FOUNDANIMAL = "foundAnimal";

    //Request types
    public static final String ANIMAL_REQUEST = "animal_request";
    public static final String ORGANIZATION_REQUEST = "organization_request";

}
