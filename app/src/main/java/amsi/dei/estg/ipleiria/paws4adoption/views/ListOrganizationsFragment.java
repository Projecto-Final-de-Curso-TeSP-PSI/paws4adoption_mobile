package amsi.dei.estg.ipleiria.paws4adoption.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_organizations, container, false);

        lvOrganizations = rootView.findViewById(R.id.lvOrganizations);

        //TODO: implementar o click nas organizações


        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        SingletonPawsManager.getInstance(getContext()).setOrganizationsListener(this);
        SingletonPawsManager.getInstance(getContext()).getAllOrganizationsAPI(getContext());

        return rootView;
    }

    @Override
    public void onRefreshOrganizationsList(ArrayList<Organization> organizationsList) {
        if(organizationsList != null)
            lvOrganizations.setAdapter(new OrganizationListAdapter(getContext(), organizationsList));

    }

    @Override
    public void onUpdateOrganizationsList(Organization organization, int operation) {

    }

    @Override
    public void onRefresh() {
        SingletonPawsManager.getInstance(getContext()).getAllOrganizationsAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }
}