package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;

/**
 * ANIMAL_REQUEST
 * ORGANIZATION_REQUEST
 */

public interface RequestListener {
    void onRequestSuccess(Animal animal);
    void onRequestError(String message);
}
