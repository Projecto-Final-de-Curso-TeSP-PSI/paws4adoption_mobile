package amsi.dei.estg.ipleiria.paws4adoption.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

public class OrganizationsDBHelper extends SQLiteOpenHelper {

    //Tables name declaration
    private static final String TABLE_NAME = "organizations";

    //Organizations table fields declaration
    private static final String ID = "id";
    private static final String NAME= "name";
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

    /**
     * SQLiteDatabase instance
     */
    private final SQLiteDatabase sqLiteDatabase;

    /**
     * Constructor for this class
     * @param context
     */
    public OrganizationsDBHelper(Context context){
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
                "CREATE TABLE " + TABLE_NAME + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
    }

    /**
     * Drops and recreates an organitions tables on the SQLiteDataBase
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    /**
     * Inserts an organizartion into the organizations table
     * @param organization
     */
    public void insertOrganizationDB(Organization organization){
        ContentValues values= new ContentValues();

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

        this.sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * Updates an organization into the organizations table
     * @param organization
     * @return
     */
    public boolean updateOrganizationDB(Organization organization){
        ContentValues values= new ContentValues();

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

        return this.sqLiteDatabase.update(TABLE_NAME, values,"id = ?", new String[]{"" + organization.getId()}) > 0;
    }

    /**
     * Deletes an organization from teh organizations tables
     * @param id
     * @return
     */
    public boolean deleteOrganizationDB(int id){
        return (this.sqLiteDatabase.delete(TABLE_NAME, "id = ?", new String[]{"" + id})) == 1;
    }

    /**
     * Get all organizations
     * @return
     */
    public ArrayList<Organization> getAllOrganizationsDB(){
        ArrayList<Organization> organizations = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME, new String[]{
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
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13)
                );

                organizations.add(auxOrg);
            } while (cursor.moveToNext());
        }

        return organizations;
    }

    /**
     * Deletes all organizations from the organizations table
     *
     */
    public void deleteAllOrganizationsDB(){
        this.sqLiteDatabase.delete(TABLE_NAME, null, null);
    }

}
