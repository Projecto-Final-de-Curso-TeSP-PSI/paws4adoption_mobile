package amsi.dei.estg.ipleiria.paws4adoption.models;

public class Adoption {

    private Integer id, adopted_animal_id, adopter_id;
    private String motivation, type;

    public Adoption(Integer id, Integer adopted_animal_id, Integer adopter_id, String motivation, String type) {
        this.id = id;
        this.adopted_animal_id = adopted_animal_id;
        this.adopter_id = adopter_id;
        this.motivation = motivation;
        this.type = type;
    }

    public Adoption() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdopted_animal_id() {
        return adopted_animal_id;
    }

    public void setAdopted_animal_id(Integer adopted_animal_id) {
        this.adopted_animal_id = adopted_animal_id;
    }

    public Integer getAdopter_id() {
        return adopter_id;
    }

    public void setAdopter_id(Integer adopter_id) {
        this.adopter_id = adopter_id;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
