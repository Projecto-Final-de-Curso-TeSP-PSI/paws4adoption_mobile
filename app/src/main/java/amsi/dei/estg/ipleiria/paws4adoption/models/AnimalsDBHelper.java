package amsi.dei.estg.ipleiria.paws4adoption.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

public class AnimalsDBHelper extends SQLiteOpenHelper {

    //Tables name declaration
    private static final String ANIMAL_TABLE_NAME = "Animals";

    //animal table fields declaration
    private static final String ID = "id";
    private static final String NAME = "name";
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
    private static final String IS_FAT = "is_fat";
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
     *
     * @param context
     */
    public AnimalsDBHelper (Context context){
        super(context, RockChisel.DB_NAME, null, RockChisel.DB_VERSION);
        this.sqLiteDatabase = this.getReadableDatabase();
    }

    /**
     * Create's an animals table on the SQLiteDataBase
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createOrganizationsTable =
                "CREATE TABLE " + ANIMAL_TABLE_NAME + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
                        IS_FAT + " INT NOT NULL," +
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
        sqLiteDatabase.execSQL(createOrganizationsTable);
    }

    /**
     * Drops and recreates an organitions tables on the SQLiteDataBase
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ANIMAL_TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    /**
     *
     * @param animal
     */
    public void insertAnimalDB(Animal animal) {
        ContentValues values = new ContentValues();

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
        values.put(IS_FAT, animal.getIs_fat());
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

        this.sqLiteDatabase.insert(ANIMAL_TABLE_NAME, null, values);
    }

    /**
     *
     * @param animal
     * @return
     */
    public boolean updateAnimalDB(Animal animal){
        ContentValues values= new ContentValues();

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
        values.put(IS_FAT, animal.getIs_fat());
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

        return this.sqLiteDatabase.update(ANIMAL_TABLE_NAME, values,"id = ?", new String[]{"" + animal.getId()}) > 0;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteAnimalDB(int id){
        return (this.sqLiteDatabase.delete(ANIMAL_TABLE_NAME, "id = ?", new String[]{"" + id})) == 1;
    }


    public ArrayList<Animal> getAllAnimalsDB(){
        ArrayList<Animal> animals = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(ANIMAL_TABLE_NAME, new String[]{
                        ID, NAME, CHIP_ID, NATURE_ID, NATURE_NAME, NATURE_PARENT_ID, NATURE_PARENT_NAME, FUR_LENGTH_ID, FUR_LENGTH, FUR_COLOR_ID, FUR_COLOR, SIZE_ID, SIZE, SEX, DESCRIPTION, CREATE_AT, PHOTO, TYPE, PUBLISHER_ID, PUBLISHER_NAME, IS_FAT, MISSING_FOUND_DATE, FOUND_ANIMAL_LOCATION_ID, FOUND_ANIMAL_STREET, FOUND_ANIMAL_CITY, FOUND_ANIMAL_DISTRICT_ID, FOUND_ANIMAL_DESTRICT_NAME, ORGANIZATION_ID, ORGANIZATION_NAME, ORGANIZATION_NIF, ORGANIZATION_EMAIL, ORGANIZATION_ADDRESS_ID, ORGANIZATION_STREET, ORGANIZATION_DOOR_NUMBER, ORGANIZATION_FLOOR, ORGANIZATION_CITY, ORGANIZATION_POSTAL_CODE,ORGANIZATION_STREET_CODE, ORGANIZATION_DISTRICT_ID, ORGANIZATION_DISTRICT_NAME },
                null, null, null, null, null
        );

        if(cursor.moveToFirst()) {

            do {
                Animal auxOrg = new Animal(
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
                        cursor.getInt(16),
                        cursor.getString(17),
                        cursor.getInt(18),
                        cursor.getString(19),
                        cursor.getInt(20),
                        cursor.getString(21),
                        cursor.getInt(22),
                        cursor.getString(23),
                        cursor.getString(24),
                        cursor.getInt(25),
                        cursor.getString(26),
                        cursor.getInt(27),
                        cursor.getString(28),
                        cursor.getInt(29),
                        cursor.getString(30),
                        cursor.getInt(31),
                        cursor.getString(32),
                        cursor.getString(33),
                        cursor.getString(34),
                        cursor.getString(35),
                        cursor.getInt(36),
                        cursor.getInt(37),
                        cursor.getInt(38),
                        cursor.getString(39)
                );

            } while (cursor.moveToNext());
        }

        return animals;
    }

    /**
     * Deletes all organizations from the organizations table
     *
     */
    public void deleteAllOrganizationsDB(){
        this.sqLiteDatabase.delete(ANIMAL_TABLE_NAME, null, null);
    }



}
