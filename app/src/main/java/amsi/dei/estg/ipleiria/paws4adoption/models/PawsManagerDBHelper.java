package amsi.dei.estg.ipleiria.paws4adoption.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

public class PawsManagerDBHelper extends SQLiteOpenHelper {

    //Tables name declaration
    private static final String ORGANIZATIONS_TABLE_NAME = "organizations";
    private static final String ANIMALS_TABLE_NAME = "animals";


    //commom animal and organization tables fields declaration
    private static final String ID = "id";
    private static final String NAME= "name";

    //Organizations table fields declaration
    private static final String NIF = "nif";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";

    private static final String ADDRESS_ID = "address_id";
    private static final String STREET = "street";
    private static final String DOOR_NUMBER = "door_number";
    private static final String FLOOR = "floor";
    private static final String POSTAL_CODE = "postal_code";
    private static final String STREET_CODE = "street_code";
    private static final String CITY = "city";

    private static final String DISTRICT_ID = "distric_id";
    private static final String DISTRICT_NAME = "district_name";

    //animal table fields declaration
    private static final String CHIP_ID = "chipId";
    private static final String NATURE_ID = "nature_id";
    private static final String NATURE_NAME = "nature_name";
    private static final String NATURE_PARENT_ID = "nature_parent_id";
    private static final String NATURE_PARENT_NAME = "nature_parent_name";
    private static final String FUR_LENGTH_ID = "fur_length_id";
    private static final String FUR_LENGTH = "fur_length";
    private static final String FUR_COLOR_ID = "fur_color_id";
    private static final String FUR_COLOR= "fur_color";
    private static final String SIZE_ID= "size_id";
    private static final String SIZE = "size";
    private static final String SEX = "sex";
    private static final String DESCRIPTION = "description";
    private static final String CREATE_AT = "createAt";
    private static final String PHOTO = "photo";
    private static final String TYPE = "type";

    private static final String PUBLISHER_ID = "publisher_id";
    private static final String PUBLISHER_NAME = "publisher_name";
    private static final String MISSING_FOUND_DATE = "missingFound_date";
    private static final String FOUND_ANIMAL_LOCATION_ID = "foundAnimal_location_id";
    private static final String FOUND_ANIMAL_STREET = "foundAnimal_street";
    private static final String FOUND_ANIMAL_CITY = "foundAnimal_city";
    private static final String FOUND_ANIMAL_DISTRICT_ID = "foundAnimal_district_id";
    private static final String FOUND_ANIMAL_DESTRICT_NAME = "foundAnimal_district_name";

    private static final String ORGANIZATION_NAME = "organization_name";
    private static final String ORGANIZATION_ID = "organization_id";
    private static final String ORGANIZATION_NIF = "organization_nif";
    private static final String ORGANIZATION_EMAIL = "organization_email";
    private static final String ORGANIZATION_ADDRESS_ID = "organization_address_id";
    private static final String ORGANIZATION_STREET = "organization_street";
    private static final String ORGANIZATION_DOOR_NUMBER = "organization_door_number";
    private static final String ORGANIZATION_FLOOR = "organization_floor";
    private static final String ORGANIZATION_CITY = "organization_city";
    private static final String ORGANIZATION_POSTAL_CODE = "organization_postal_code";
    private static final String ORGANIZATION_STREET_CODE = "organization_street_code";
    private static final String ORGANIZATION_DISTRICT_ID = "organization_district_id";
    private static final String ORGANIZATION_DISTRICT_NAME = "organziation_district_name";

    /**
     * SQLiteDatabase instance
     */
    private final SQLiteDatabase sqLiteDatabase;

    /**
     * Constructor for this class
     * @param context
     */
    public PawsManagerDBHelper(Context context){
        super(context, RockChisel.DB_NAME, null, RockChisel.DB_VERSION);
        this.sqLiteDatabase = this.getReadableDatabase();
    }

    /**
     * Create's an organizations table on the SQLiteDataBase
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createOrganizationsTable =
                "CREATE TABLE " + ORGANIZATIONS_TABLE_NAME + "(" +
                        ID + " INTEGER, " +
                        NAME + " TEXT NOT NULL, " +
                        NIF + " TEXT NOT NULL, " +
                        EMAIL + " TEXT, " +
                        PHONE + " TEXT, " +
                        ADDRESS_ID + " TEXT NOT NULL, " +
                        STREET + " TEXT NOT NULL, " +
                        DOOR_NUMBER + " TEXT, " +
                        FLOOR + " TEXT, " +
                        POSTAL_CODE + " INTEGER NOT NULL, " +
                        STREET_CODE + " INTEGER NOT NULL, " +
                        CITY + " TEXT NOT NULL, " +
                        DISTRICT_ID + " INTEGER NOT NULL, " +
                        DISTRICT_NAME + " TEXT NOT NULL" +
                        ");";
        sqLiteDatabase.execSQL(createOrganizationsTable);

        String createAnimalsTable =
                "CREATE TABLE " + ANIMALS_TABLE_NAME + "(" +
                        ID + " INTEGER, " +
                        NAME + " TEXT, " +
                        CHIP_ID + " TEXT, " +
                        NATURE_ID + " INT NOT NULL, " +
                        NATURE_NAME + " TEXT NOT NULL, " +
                        NATURE_PARENT_ID + " INT NOT NULL, " +
                        NATURE_PARENT_NAME + " TEXT NOT NULL, " +
                        FUR_LENGTH_ID + " INT NOT NULL, " +
                        FUR_LENGTH + " TEXT NOT NULL, " +
                        FUR_COLOR_ID + " INT NOT NULL, " +
                        FUR_COLOR + " TEXT NOT NULL, " +
                        SIZE_ID + " TEXT NOT NULL, " +
                        SIZE + " TEXT NOT NULL, " +
                        SEX + " TEXT NOT NULL, " +
                        DESCRIPTION + " TEXT NOT NULL, " +
                        CREATE_AT + " TEXT NOT NULL," +
                        PHOTO + " TEXT," +
                        TYPE + " TEXT NOT NULL," +
                        PUBLISHER_ID + " INT NOT NULL," +
                        PUBLISHER_NAME + " TEXT NOT NULL," +
                        MISSING_FOUND_DATE + " TEXT," +
                        FOUND_ANIMAL_LOCATION_ID + " INT," +
                        FOUND_ANIMAL_STREET + " TEXT," +
                        FOUND_ANIMAL_CITY + " TEXT," +
                        FOUND_ANIMAL_DISTRICT_ID + " INT," +
                        FOUND_ANIMAL_DESTRICT_NAME + " TEXT," +
                        ORGANIZATION_ID + " INT," +
                        ORGANIZATION_NAME + " TEXT," +
                        ORGANIZATION_NIF + " INT," +
                        ORGANIZATION_EMAIL + " TEXT," +
                        ORGANIZATION_ADDRESS_ID + " INT," +
                        ORGANIZATION_STREET + " TEXT," +
                        ORGANIZATION_DOOR_NUMBER + " INT," +
                        ORGANIZATION_FLOOR + " INT," +
                        ORGANIZATION_CITY + " TEXT," +
                        ORGANIZATION_POSTAL_CODE + " INT," +
                        ORGANIZATION_STREET_CODE + " INT," +
                        ORGANIZATION_DISTRICT_ID + " INT," +
                        ORGANIZATION_DISTRICT_NAME + " TEXT" +
                        ");";
        sqLiteDatabase.execSQL(createAnimalsTable);
    }

    /**
     * Drops and recreates an organitions tables on the SQLiteDataBase
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ORGANIZATIONS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ANIMALS_TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }


    //################ ORGANIZATIONS ################

    /**
     * Get all organizations
     * @return all the organizations on the database
     */
    public ArrayList<Organization> getAllOrganizationsDB(){
        ArrayList<Organization> organizations = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(ORGANIZATIONS_TABLE_NAME, new String[]{
                        ID, NAME, NIF, EMAIL, PHONE, ADDRESS_ID, STREET, DOOR_NUMBER, FLOOR, POSTAL_CODE, STREET_CODE, CITY, DISTRICT_ID, DISTRICT_NAME},
                null, null, null, null, null
        );

        if(cursor.moveToFirst()) {

            do {
                Organization auxOrg = new Organization(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getInt(9),
                        cursor.getInt(10),
                        cursor.getString(11),
                        cursor.getInt(12),
                        cursor.getString(13)
                );

                organizations.add(auxOrg);
            } while (cursor.moveToNext());
        }

        return organizations;
    }

    /**
     * Inserts an organizartion into the organizations table
     * @param organization to insert into the database
     */
    public boolean insertOrganizationDB(Organization organization){
        ContentValues values= new ContentValues();

        values.put(ID, organization.getId());
        values.put(NAME, organization.getName());
        values.put(NIF, organization.getNif());
        values.put(EMAIL, organization.getEmail());
        values.put(PHONE, organization.getPhone());
        values.put(ADDRESS_ID, organization.getAddress_id());
        values.put(STREET, organization.getStreet());
        values.put(DOOR_NUMBER, organization.getDoor_number());
        values.put(FLOOR, organization.getFloor());
        values.put(POSTAL_CODE, organization.getPostal_code());
        values.put(STREET_CODE, organization.getStreet_code());
        values.put(CITY, organization.getCity());
        values.put(DISTRICT_ID, organization.getDistrict_id());
        values.put(DISTRICT_NAME, organization.getDistrict_name());

        return (this.sqLiteDatabase.insert(ORGANIZATIONS_TABLE_NAME, null, values)) != -1;
    }

    /**
     * Updates an organization into the organizations table
     * @param organization to update on the database
     * @return true if successfully updated
     */
    public boolean updateOrganizationDB(Organization organization){
        ContentValues values= new ContentValues();

        values.put(ID, organization.getId());
        values.put(NAME, organization.getName());
        values.put(NIF, organization.getNif());
        values.put(EMAIL, organization.getEmail());
        values.put(PHONE, organization.getPhone());
        values.put(ADDRESS_ID, organization.getAddress_id());
        values.put(STREET, organization.getStreet());
        values.put(DOOR_NUMBER, organization.getDoor_number());
        values.put(FLOOR, organization.getFloor());
        values.put(POSTAL_CODE, organization.getPostal_code());
        values.put(STREET_CODE, organization.getStreet_code());
        values.put(CITY, organization.getCity());
        values.put(DISTRICT_ID, organization.getDistrict_id());
        values.put(DISTRICT_NAME, organization.getDistrict_name());

        return this.sqLiteDatabase.update(ORGANIZATIONS_TABLE_NAME, values,"id = ?", new String[]{"" + organization.getId()}) > 0;
    }

    /**
     * Deletes an organization from the organizations tables
     * @param id of the record to delete
     * @return true if successfully deleted, or false otherwise
     */
    public boolean deleteOrganizationDB(int id){
        return (this.sqLiteDatabase.delete(ORGANIZATIONS_TABLE_NAME, "id = ?", new String[]{"" + id}) == 1);
    }

    /**
     * Deletes all organizations from the organizations table
     *
     */
    public void deleteAllOrganizationsDB(){
        this.sqLiteDatabase.delete(ORGANIZATIONS_TABLE_NAME, null, null);
    }


    //################ ANIMALS ################

    /**
     * Get all animals
     * @return
     */
    public ArrayList<Animal> getAllAnimalsDB(){
        ArrayList<Animal> animals = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(ANIMALS_TABLE_NAME, new String[]{
                        ID,
                        NAME,
                        CHIP_ID,
                        NATURE_ID,
                        NATURE_NAME,
                        NATURE_PARENT_ID,
                        NATURE_PARENT_NAME,
                        FUR_LENGTH_ID,
                        FUR_LENGTH,
                        FUR_COLOR_ID,
                        FUR_COLOR, //10
                        SIZE_ID,
                        SIZE,
                        SEX,
                        DESCRIPTION,
                        CREATE_AT,
                        PHOTO,
                        TYPE,
                        PUBLISHER_ID,
                        PUBLISHER_NAME,
                        MISSING_FOUND_DATE,
                        FOUND_ANIMAL_LOCATION_ID,
                        FOUND_ANIMAL_STREET,
                        FOUND_ANIMAL_CITY,
                        FOUND_ANIMAL_DISTRICT_ID,
                        FOUND_ANIMAL_DESTRICT_NAME,
                        ORGANIZATION_ID,
                        ORGANIZATION_NAME,
                        ORGANIZATION_NIF,
                        ORGANIZATION_EMAIL, //30
                        ORGANIZATION_ADDRESS_ID,
                        ORGANIZATION_STREET,
                        ORGANIZATION_DOOR_NUMBER,
                        ORGANIZATION_FLOOR,
                        ORGANIZATION_CITY,
                        ORGANIZATION_POSTAL_CODE,
                        ORGANIZATION_STREET_CODE,
                        ORGANIZATION_DISTRICT_ID,
                        ORGANIZATION_DISTRICT_NAME },
                null, null, null, null, null
        );

        if(cursor.moveToFirst()) {

            do {
                Animal auxAnimal = new Animal(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getInt(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17),
                        cursor.getInt(18),
                        cursor.getString(19),
                        cursor.getString(20),
                        cursor.getInt(21),
                        cursor.getString(22),
                        cursor.getString(23),
                        cursor.getInt(24),
                        cursor.getString(25),
                        cursor.getInt(26),
                        cursor.getString(27),
                        cursor.getInt(28),
                        cursor.getString(29),
                        cursor.getInt(30),
                        cursor.getString(31),
                        cursor.getString(32),
                        cursor.getString(33),
                        cursor.getString(34),
                        cursor.getInt(35),
                        cursor.getInt(36),
                        cursor.getInt(37),
                        cursor.getString(38)
                );
                animals.add(auxAnimal);
            } while (cursor.moveToNext());
        }

        return animals;
    }

    /**
     * Get's one animal fro teh DataBase
     * @return
     */
    public Animal getAnimalDB(int id){

        Animal auxAnimal = null;

        Cursor cursor = this.sqLiteDatabase.query(ANIMALS_TABLE_NAME, new String[]{
                ID,
                NAME,
                CHIP_ID,
                NATURE_ID,
                NATURE_NAME,
                NATURE_PARENT_ID,
                NATURE_PARENT_NAME,
                FUR_LENGTH_ID,
                FUR_LENGTH,
                FUR_COLOR_ID,
                FUR_COLOR, //10
                SIZE_ID,
                SIZE,
                SEX,
                DESCRIPTION,
                CREATE_AT,
                PHOTO,
                TYPE,
                PUBLISHER_ID,
                PUBLISHER_NAME,
                MISSING_FOUND_DATE,
                FOUND_ANIMAL_LOCATION_ID,
                FOUND_ANIMAL_STREET,
                FOUND_ANIMAL_CITY,
                FOUND_ANIMAL_DISTRICT_ID,
                FOUND_ANIMAL_DESTRICT_NAME,
                ORGANIZATION_ID,
                ORGANIZATION_NAME,
                ORGANIZATION_NIF,
                ORGANIZATION_EMAIL, //30
                ORGANIZATION_ADDRESS_ID,
                ORGANIZATION_STREET,
                ORGANIZATION_DOOR_NUMBER,
                ORGANIZATION_FLOOR,
                ORGANIZATION_CITY,
                ORGANIZATION_POSTAL_CODE,
                ORGANIZATION_STREET_CODE,
                ORGANIZATION_DISTRICT_ID,
                ORGANIZATION_DISTRICT_NAME
        }, ID + "=" + id, null, null, null, null, null);

                auxAnimal = new Animal(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getInt(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17),
                        cursor.getInt(18),
                        cursor.getString(19),
                        cursor.getString(20),
                        cursor.getInt(21),
                        cursor.getString(22),
                        cursor.getString(23),
                        cursor.getInt(24),
                        cursor.getString(25),
                        cursor.getInt(26),
                        cursor.getString(27),
                        cursor.getInt(27),
                        cursor.getString(29),
                        cursor.getInt(30),
                        cursor.getString(31),
                        cursor.getString(32),
                        cursor.getString(33),
                        cursor.getString(34),
                        cursor.getInt(35),
                        cursor.getInt(36),
                        cursor.getInt(37),
                        cursor.getString(38)
                );

        return auxAnimal;
    }

    /**
     * Inserts one animal into the DB
     * @param animal
     */
    public boolean insertAnimalDB(Animal animal) {
        ContentValues values = new ContentValues();

        values.put(ID, animal.getId());
        values.put(NAME, animal.getName());
        values.put(CHIP_ID, animal.getChipId());
        values.put(NATURE_ID, animal.getNature_id());
        values.put(NATURE_NAME, animal.getNature_name());
        values.put(NATURE_PARENT_ID, animal.getNature_parent_id());
        values.put(NATURE_PARENT_NAME, animal.getNature_parent_name());
        values.put(FUR_LENGTH_ID, animal.getFur_length_id());
        values.put(FUR_LENGTH, animal.getFur_length());
        values.put(FUR_COLOR_ID, animal.getFur_color_id());
        values.put(FUR_COLOR, animal.getFur_color());
        values.put(SIZE_ID, animal.getSize_id());
        values.put(SIZE, animal.getSize());
        values.put(SEX, animal.getSex());
        values.put(DESCRIPTION, animal.getDescription());
        values.put(CREATE_AT, animal.getCreateAt());
        values.put(PHOTO, animal.getPhoto());
        values.put(TYPE, animal.getType());
        values.put(PUBLISHER_ID, animal.getPublisher_id());
        values.put(PUBLISHER_NAME, animal.getPublisher_name());
        values.put(MISSING_FOUND_DATE, animal.getMissingFound_date());
        values.put(FOUND_ANIMAL_LOCATION_ID, animal.getFoundAnimal_location_id());
        values.put(FOUND_ANIMAL_STREET, animal.getFoundAnimal_street());
        values.put(FOUND_ANIMAL_CITY, animal.getFoundAnimal_city());
        values.put(FOUND_ANIMAL_DISTRICT_ID, animal.getFoundAnimal_district_id());
        values.put(FOUND_ANIMAL_DESTRICT_NAME, animal.getFoundAnimal_district_name());
        values.put(ORGANIZATION_ID, animal.getOrganization_id());
        values.put(ORGANIZATION_NAME, animal.getOrganization_name());
        values.put(ORGANIZATION_NIF, animal.getOrganization_nif());
        values.put(ORGANIZATION_EMAIL, animal.getOrganization_email());
        values.put(ORGANIZATION_ADDRESS_ID, animal.getOrganization_address_id());
        values.put(ORGANIZATION_STREET, animal.getOrganization_street());
        values.put(ORGANIZATION_DOOR_NUMBER, animal.getOrganization_door_number());
        values.put(ORGANIZATION_FLOOR, animal.getOrganization_floor());
        values.put(ORGANIZATION_CITY, animal.getOrganization_city());
        values.put(ORGANIZATION_POSTAL_CODE, animal.getOrganization_postal_code());
        values.put(ORGANIZATION_STREET_CODE, animal.getOrganization_street_code());
        values.put(ORGANIZATION_DISTRICT_ID, animal.getOrganization_district_id());
        values.put(ORGANIZATION_DISTRICT_NAME, animal.getOrganization_district_name());

        return this.sqLiteDatabase.insert(ANIMALS_TABLE_NAME, null, values) != -1;
    }

    /**
     * Updates one animal into the DB
     * @param animal
     * @return
     */
    public boolean updateAnimalDB(Animal animal){
        ContentValues values= new ContentValues();

        values.put(ID, animal.getId());
        values.put(NAME, animal.getName());
        values.put(CHIP_ID, animal.getChipId());
        values.put(NATURE_ID, animal.getNature_id());
        values.put(NATURE_NAME, animal.getNature_name());
        values.put(NATURE_PARENT_ID, animal.getNature_parent_id());
        values.put(NATURE_PARENT_NAME, animal.getNature_parent_name());
        values.put(FUR_LENGTH_ID, animal.getFur_length_id());
        values.put(FUR_LENGTH, animal.getFur_length());
        values.put(FUR_COLOR_ID, animal.getFur_color_id());
        values.put(FUR_COLOR, animal.getFur_color());
        values.put(SIZE_ID, animal.getSize_id());
        values.put(SIZE, animal.getSize());
        values.put(SEX, animal.getSex());
        values.put(DESCRIPTION, animal.getDescription());
        values.put(CREATE_AT, animal.getCreateAt());
        values.put(PHOTO, animal.getPhoto());
        values.put(TYPE, animal.getType());
        values.put(PUBLISHER_ID, animal.getPublisher_id());
        values.put(PUBLISHER_NAME, animal.getPublisher_name());
        values.put(MISSING_FOUND_DATE, animal.getMissingFound_date());
        values.put(FOUND_ANIMAL_LOCATION_ID, animal.getFoundAnimal_location_id());
        values.put(FOUND_ANIMAL_STREET, animal.getFoundAnimal_street());
        values.put(FOUND_ANIMAL_CITY, animal.getFoundAnimal_city());
        values.put(FOUND_ANIMAL_DISTRICT_ID, animal.getFoundAnimal_district_id());
        values.put(FOUND_ANIMAL_DESTRICT_NAME, animal.getFoundAnimal_district_name());
        values.put(ORGANIZATION_ID, animal.getOrganization_id());
        values.put(ORGANIZATION_NAME, animal.getOrganization_name());
        values.put(ORGANIZATION_NIF, animal.getOrganization_nif());
        values.put(ORGANIZATION_EMAIL, animal.getOrganization_email());
        values.put(ORGANIZATION_ADDRESS_ID, animal.getOrganization_address_id());
        values.put(ORGANIZATION_STREET, animal.getOrganization_street());
        values.put(ORGANIZATION_DOOR_NUMBER, animal.getOrganization_door_number());
        values.put(ORGANIZATION_FLOOR, animal.getOrganization_floor());
        values.put(ORGANIZATION_CITY, animal.getOrganization_city());
        values.put(ORGANIZATION_POSTAL_CODE, animal.getOrganization_postal_code());
        values.put(ORGANIZATION_STREET_CODE, animal.getOrganization_street_code());
        values.put(ORGANIZATION_DISTRICT_ID, animal.getOrganization_district_id());
        values.put(ORGANIZATION_DISTRICT_NAME, animal.getOrganization_district_name());

        return this.sqLiteDatabase.update(ANIMALS_TABLE_NAME, values,"id = ?", new String[]{"" + animal.getId()}) > 0;
    }

    /**
     * Deletes one animal on the DB
     * @param idAnimal
     * @return
     */
    public boolean deleteAnimalDB(int idAnimal){
        boolean deleted = (this.sqLiteDatabase.delete(ANIMALS_TABLE_NAME, "id = ?", new String[]{"" + idAnimal}) == 1);
        return deleted;
    }

    /**
     * Deletes all Animals from the animal table
     *
     */
    public void deleteAllAnimalsDB(){
        this.sqLiteDatabase.delete(ANIMALS_TABLE_NAME, null, null);
    }

}
