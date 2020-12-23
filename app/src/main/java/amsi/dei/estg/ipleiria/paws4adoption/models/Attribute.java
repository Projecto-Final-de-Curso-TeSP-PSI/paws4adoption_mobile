package amsi.dei.estg.ipleiria.paws4adoption.models;

import java.util.HashMap;

public class Attribute {

    private Integer id;
    private String name;

    public Attribute(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
