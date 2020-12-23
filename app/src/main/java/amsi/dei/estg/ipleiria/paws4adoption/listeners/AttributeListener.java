package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.models.Attribute;

public interface AttributeListener {
    void onReceivedAttributes(ArrayList<Attribute> attributes, String attibuteType);
}
