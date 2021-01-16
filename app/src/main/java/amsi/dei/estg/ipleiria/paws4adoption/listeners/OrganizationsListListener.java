package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;

public interface OrganizationsListListener {
    void onRefreshOrganizationsList(ArrayList<Organization> organizationsList);
    void onUpdateOrganizationsList(Organization organization, int operation);
}
