package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import amsi.dei.estg.ipleiria.paws4adoption.models.Adoption;

public interface AdoptionListener {
    void onRequestSuccess(String action, Adoption adoption);
    void onRequestError(String message);

    void onCreateAdoption();
}
