package amsi.dei.estg.ipleiria.paws4adoption.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.adapters.OrganizationListAdapter;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.OrganizationsListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;


public class ListOrganizationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OrganizationsListener {

    private ListView lvOrganizations;
    private ArrayList<Organization> listOrganizations;

    private SearchView searchView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;

    /**
     * On create method, runs and initiates all the needed components at the start of the activity
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_organizations, container, false);

        lvOrganizations = rootView.findViewById(R.id.lvOrganizations);

        lvOrganizations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Organization hasOrganization = (Organization) parent.getItemAtPosition(i);
                System.out.println("--> " + hasOrganization.getName());

                Intent intent = new Intent(getContext(), OrganizationDetailsActivity.class);
                intent.putExtra(OrganizationDetailsActivity.ORG_DETAILS, hasOrganization.getId());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        SingletonPawsManager.getInstance(getContext()).setOrganizationsListener(this);
        SingletonPawsManager.getInstance(getContext()).getAllOrganizationsAPI(getContext());

        return rootView;
    }

    /**
     * Set's the definitions of the options menu on it's creation
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_by_district, menu);

        MenuItem searchByDistrict = menu.findItem(R.id.searchByDistrict);
        searchView = (SearchView) searchByDistrict.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Organization> tempOrganizations = new ArrayList<>();

                List<Organization> dbOrganizations = SingletonPawsManager.getInstance(getContext()).getAllOrganizationsDB();

                for (Organization org :
                       dbOrganizations ) {
                    if(org.getDistrict_name().toLowerCase().contains(newText.toLowerCase())){
                        tempOrganizations.add(org);
                    }
                }

                lvOrganizations.setAdapter(new OrganizationListAdapter(getContext(), tempOrganizations));
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Implementatio of the organizations listener interface method onRefreshOrganizationList
     * @param organizationsList
     */
    @Override
    public void onRefreshOrganizationsList(ArrayList<Organization> organizationsList) {
        if(organizationsList != null)
            lvOrganizations.setAdapter(new OrganizationListAdapter(getContext(), organizationsList));
    }

    /**
     * Implementatio of the organizations listener interface method onUpdateOrganizationList
     * @param organization
     * @param operation
     */
    @Override
    public void onUpdateOrganizationsList(Organization organization, int operation) {

    }

    /**
     * Implements the Swipe Listener interface method onRefresh
     */
    @Override
    public void onRefresh() {
        SingletonPawsManager.getInstance(getContext()).getAllOrganizationsAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }
}