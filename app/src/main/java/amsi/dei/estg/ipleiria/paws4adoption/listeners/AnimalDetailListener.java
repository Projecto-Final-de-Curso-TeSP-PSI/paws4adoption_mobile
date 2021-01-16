package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;

public interface AnimalDetailListener {
    void onGetAnimal(Animal animal);
    void onDeleteAnimalFromList();
    void onUpdateAnimalFromList();
}
