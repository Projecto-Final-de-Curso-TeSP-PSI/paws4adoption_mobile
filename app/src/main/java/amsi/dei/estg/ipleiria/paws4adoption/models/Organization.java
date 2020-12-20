package amsi.dei.estg.ipleiria.paws4adoption.models;

public class Organization {

    private int id;
    private String name;
    private String nif;
    private String email;
    private String phone;

    private String address_id;
    private String street;
    private String door_number;
    private String floor;
    private String postal_code;
    private String street_code;
    private String city;

    private String district_id;
    private String district_name;

    public Organization(int id, String name, String nif, String email, String phone, String address_id, String street, String door_number,
                        String floor, String postal_code, String street_code, String city, String district_id, String district_name){
        this.id = id;
        this.name = name;
        this.nif = nif;
        this.email = email;
        this.phone = phone;

        this.address_id = address_id;
        this.street = street;
        this.door_number = door_number;
        this.floor = floor;
        this.postal_code = postal_code;
        this.street_code = street_code;
        this.city = city;

        this.district_id = district_id;
        this.district_name = district_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDoor_number() {
        return door_number;
    }

    public void setDoor_number(String door_number) {
        this.door_number = door_number;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getStreet_code() {
        return street_code;
    }

    public void setStreet_code(String street_code) {
        this.street_code = street_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

}
