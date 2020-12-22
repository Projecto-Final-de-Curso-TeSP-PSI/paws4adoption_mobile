package amsi.dei.estg.ipleiria.paws4adoption.models;

import java.util.Date;

public class Animal {
    //############################################# Attributos ##################################################
    private Integer
            id,
            nature_id,
            nature_parent_id,
            fur_length_id,
            fur_color_id,
            size_id,
            photo,
            publisher_id,
            organization_id,
            organization_nif,
            organization_district_id,
            organization_address_id,
            organization_postal_code,
            organization_street_code,
            foundAnimal_location_id,
            foundAnimal_district_id,
            is_fat;

    private String
            chipId,
            description,
            nature_name,
            nature_parent_name,
            fur_length,
            fur_color,
            size,
            sex,
            name,
            type,
            createAt,
            publisher_name,
            missingFound_date,
            organization_name,
            organization_email,
            organization_street,
            organization_city,
            organization_district_name,
            foundAnimal_street,
            foundAnimal_city,
            foundAnimal_district_name,
            organization_door_number,
            organization_floor;



    //############################################# FIM Atributos ##################################################


    //############################################# Construtor Animal ##################################################

    public Animal(int id, String name, String chipId, int nature_id, String nature_name, int nature_parent_id, String nature_parent_name, int fur_length_id, String fur_length, int fur_color_id, String fur_color, int size_id, String size, String sex, String description, String createAt, int photo, String type, int publisher_id, String publisher_name, Integer is_fat, String missingFound_date, int foundAnimal_location_id, String foundAnimal_street, String foundAnimal_city, int foundAnimal_district_id, String foundAnimal_district_name, int organization_id, String organization_name, int organization_nif, String organization_email, int organization_address_id, String organization_street, String organization_door_number, String organization_floor, String organization_city, int organization_postal_code, int organization_street_code, int organization_district_id, String organization_district_name

    ) {
        this.id = id;
        this.name = name;
        this.chipId = chipId;
        this.nature_id = nature_id;
        this.nature_name = nature_name;
        this.nature_parent_id = nature_parent_id;
        this.nature_parent_name = nature_parent_name;
        this.fur_length_id = fur_length_id;
        this.fur_length = fur_length;
        this.fur_color_id = fur_color_id;
        this.fur_color = fur_color;
        this.size_id = size_id;
        this.size = size;
        this.sex = sex;
        this.description = description;
        this.createAt = createAt;
        this.photo = photo;
        this.type = type;
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.is_fat = is_fat;
        this.missingFound_date = missingFound_date;
        this.foundAnimal_location_id = foundAnimal_location_id;
        this.foundAnimal_street = foundAnimal_street;
        this.foundAnimal_city = foundAnimal_city;
        this.foundAnimal_district_id = foundAnimal_district_id;
        this.foundAnimal_district_name = foundAnimal_district_name;
        this.organization_id = organization_id;
        this.organization_name = organization_name;
        this.organization_nif = organization_nif;
        this.organization_email = organization_email;
        this.organization_address_id = organization_address_id;
        this.organization_street = organization_street;
        this.organization_door_number = organization_door_number;
        this.organization_floor = organization_floor;
        this.organization_city = organization_city;
        this.organization_postal_code = organization_postal_code;
        this.organization_street_code = organization_street_code;
        this.organization_district_id = organization_district_id;
        this.organization_district_name = organization_district_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNature_id() {
        return nature_id;
    }

    public void setNature_id(Integer nature_id) {
        this.nature_id = nature_id;
    }

    public Integer getNature_parent_id() {
        return nature_parent_id;
    }

    public void setNature_parent_id(Integer nature_parent_id) {
        this.nature_parent_id = nature_parent_id;
    }

    public Integer getFur_length_id() {
        return fur_length_id;
    }

    public void setFur_length_id(Integer fur_length_id) {
        this.fur_length_id = fur_length_id;
    }

    public Integer getFur_color_id() {
        return fur_color_id;
    }

    public void setFur_color_id(Integer fur_color_id) {
        this.fur_color_id = fur_color_id;
    }

    public Integer getSize_id() {
        return size_id;
    }

    public void setSize_id(Integer size_id) {
        this.size_id = size_id;
    }

    public Integer getPhoto() {
        return photo;
    }

    public void setPhoto(Integer photo) {
        this.photo = photo;
    }

    public Integer getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(Integer publisher_id) {
        this.publisher_id = publisher_id;
    }

    public Integer getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Integer organization_id) {
        this.organization_id = organization_id;
    }

    public Integer getOrganization_nif() {
        return organization_nif;
    }

    public void setOrganization_nif(Integer organization_nif) {
        this.organization_nif = organization_nif;
    }

    public Integer getOrganization_district_id() {
        return organization_district_id;
    }

    public void setOrganization_district_id(Integer organization_district_id) {
        this.organization_district_id = organization_district_id;
    }

    public Integer getOrganization_address_id() {
        return organization_address_id;
    }

    public void setOrganization_address_id(Integer organization_address_id) {
        this.organization_address_id = organization_address_id;
    }

    public Integer getOrganization_postal_code() {
        return organization_postal_code;
    }

    public void setOrganization_postal_code(Integer organization_postal_code) {
        this.organization_postal_code = organization_postal_code;
    }

    public Integer getOrganization_street_code() {
        return organization_street_code;
    }

    public void setOrganization_street_code(Integer organization_street_code) {
        this.organization_street_code = organization_street_code;
    }

    public Integer getFoundAnimal_location_id() {
        return foundAnimal_location_id;
    }

    public void setFoundAnimal_location_id(Integer foundAnimal_location_id) {
        this.foundAnimal_location_id = foundAnimal_location_id;
    }

    public Integer getFoundAnimal_district_id() {
        return foundAnimal_district_id;
    }

    public void setFoundAnimal_district_id(Integer foundAnimal_district_id) {
        this.foundAnimal_district_id = foundAnimal_district_id;
    }

    public Integer getIs_fat() {
        return is_fat;
    }

    public void setIs_fat(Integer is_fat) {
        this.is_fat = is_fat;
    }

    public String getChipId() {
        return chipId;
    }

    public void setChipId(String chipId) {
        this.chipId = chipId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNature_name() {
        return nature_name;
    }

    public void setNature_name(String nature_name) {
        this.nature_name = nature_name;
    }

    public String getNature_parent_name() {
        return nature_parent_name;
    }

    public void setNature_parent_name(String nature_parent_name) {
        this.nature_parent_name = nature_parent_name;
    }

    public String getFur_length() {
        return fur_length;
    }

    public void setFur_length(String fur_length) {
        this.fur_length = fur_length;
    }

    public String getFur_color() {
        return fur_color;
    }

    public void setFur_color(String fur_color) {
        this.fur_color = fur_color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getMissingFound_date() {
        return missingFound_date;
    }

    public void setMissingFound_date(String missingFound_date) {
        this.missingFound_date = missingFound_date;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getOrganization_email() {
        return organization_email;
    }

    public void setOrganization_email(String organization_email) {
        this.organization_email = organization_email;
    }

    public String getOrganization_street() {
        return organization_street;
    }

    public void setOrganization_street(String organization_street) {
        this.organization_street = organization_street;
    }

    public String getOrganization_city() {
        return organization_city;
    }

    public void setOrganization_city(String organization_city) {
        this.organization_city = organization_city;
    }

    public String getOrganization_district_name() {
        return organization_district_name;
    }

    public void setOrganization_district_name(String organization_district_name) {
        this.organization_district_name = organization_district_name;
    }

    public String getFoundAnimal_street() {
        return foundAnimal_street;
    }

    public void setFoundAnimal_street(String foundAnimal_street) {
        this.foundAnimal_street = foundAnimal_street;
    }

    public String getFoundAnimal_city() {
        return foundAnimal_city;
    }

    public void setFoundAnimal_city(String foundAnimal_city) {
        this.foundAnimal_city = foundAnimal_city;
    }

    public String getFoundAnimal_district_name() {
        return foundAnimal_district_name;
    }

    public void setFoundAnimal_district_name(String foundAnimal_district_name) {
        this.foundAnimal_district_name = foundAnimal_district_name;
    }

    public String getOrganization_door_number() {
        return organization_door_number;
    }

    public void setOrganization_door_number(String organization_door_number) {
        this.organization_door_number = organization_door_number;
    }

    public String getOrganization_floor() {
        return organization_floor;
    }

    public void setOrganization_floor(String organization_floor) {
        this.organization_floor = organization_floor;
    }


    //############################################# FIM Construtor ##################################################

    //############################################# GETTER and SETTER ##################################################
}
