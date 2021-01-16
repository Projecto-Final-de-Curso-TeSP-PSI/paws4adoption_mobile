package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;

public interface AnimalsListListener {
    void onRefreshAnimalsList(ArrayList<Animal> animalsList);
    void onUpdateAnimalsList(Animal animal, int operation);
    void onCreateAnimalFromList();
    void onUpdateAnimalFromList();
    void onDeleteAnimalFromList();
}
