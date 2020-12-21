package amsi.dei.estg.ipleiria.paws4adoption;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import amsi.dei.estg.ipleiria.paws4adoption.adapters.ListAnimalsAdapter;
import amsi.dei.estg.ipleiria.paws4adoption.adapters.OrganizationListAdapter;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;


public class ListAdoptionAnimalsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AnimalListener {

    private ListView lvListAdoptionAnimal;


    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_list_adoption_animals, container, false);

        lvListAdoptionAnimal = rootView.findViewById(R.id.lvListAdoptionAnimal);

        lvListAdoptionAnimal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Animal hasAnimal = (Animal) parent.getItemAtPosition(i);
                System.out.println("--> " + hasAnimal.getName());
            }
        });


        return rootView;

    }



    @Override
    public void onRefresh() {
        //TODO::
    }

    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsList) {
        if(animalsList != null)
            lvListAdoptionAnimal.setAdapter(new ListAnimalsAdapter(getContext(), animalsList));
    }

    @Override
    public void onUpdateAnimalsList(Animal animal, int operation) {

    }
}