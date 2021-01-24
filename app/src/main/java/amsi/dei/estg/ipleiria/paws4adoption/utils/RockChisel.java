package amsi.dei.estg.ipleiria.paws4adoption.utils;


/**
 * Class that makes available a set of global constants
 */
public class RockChisel {

    //Package related constants
    public static final String PACKAGE_NAME = "amsi.dei.estg.ipleiria.paws4adoption";
    public static final String COMPUTER_LOCAL_IP = "192.168.1.65";
    public static final String API_LOCAL_URL = "http://" + COMPUTER_LOCAL_IP + "/pet-adoption/paws4adoption_web/backend/web/api/";
    public static final String PHOTO_LOCAL_URL =  "http://" + COMPUTER_LOCAL_IP + "/pet-adoption/paws4adoption_web/frontend/web/images/animal/";

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
    public static final String ID_USER = "id";

    //Database related constants
    public static final String DB_NAME = "paws4adoptionDB";
    public static final int DB_VERSION = 1;
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
    public static final String ADOPTIONS_API_SERVICE = "adoptions";

    //API SymLink
    public static final String ATTR_SPECIE_SYMLINK = "name";
    public static final String ATTR_SUBSPECIE_SYMLINK = "name";
    public static final String ATTR_FUR_COLOR_SYMLINK = "fur_color";
    public static final String ATTR_FUR_LENGTH_SYMLINK = "fur_length";
    public static final String ATTR_SIZE_SYMLINK = "size";
    public static final String ATTR_DISTRICT_SYMLINK = "name";

    //Scenarios
    public static final String SCENARIO = "scenario";

    public static final String SCENARIO_ADOPTION_ANIMAL = "adoptionAnimal";
    public static final String SCENARIO_MISSING_ANIMAL = "missingAnimal";
    public static final String SCENARIO_FOUND_ANIMAL = "foundAnimal";

    public static final String SCENARIO_GENERAL_LIST = "generalList";
    public static final String SCENARIO_MY_LIST = "myList";

    public static final String SCENARIO_GENERAL_ANIMAL = "generalAnimal";
    public static final String SCENARIO_MY_ANIMAL = "myAnimal";

    public static final String ANIMAL_TYPE = "animalType";
    public static final String ANIMAL_ID = "animalId";

    //Actions
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_READ = "read";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";

    //Animal types
    public static final String ADOPTION_ANIMAL = "adoptionAnimal";
    public static final String MISSING_ANIMAL = "missingAnimal";
    public static final String FOUND_ANIMAL = "foundAnimal";

    //Request types
    public static final String ADOPTION_REQUEST = "adoption";
    public static final String TFF_REQUEST = "fat";
    public static final String ANIMAL_REQUEST = "animal_request";
    public static final String ORGANIZATION_REQUEST = "organization_request";
}
