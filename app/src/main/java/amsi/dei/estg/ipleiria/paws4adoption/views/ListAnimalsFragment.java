package amsi.dei.estg.ipleiria.paws4adoption.views;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.adapters.ListAnimalsAdapter;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalsListListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;


public class ListAnimalsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AnimalsListListener {

    //################ INTENT PARAMETERS ################
    private String scenario = null;
    private String animal_type = null;

    private ListView lvListAdoptionAnimal;
    private SearchView searchView;

    private ArrayList<Animal> listAnimals;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            scenario = bundle.getString(RockChisel.SCENARIO);

            if(!scenario.equals(RockChisel.SCENARIO_MY_LIST))
                animal_type = bundle.getString(RockChisel.ANIMAL_TYPE);
        }

        View rootView = inflater.inflate(R.layout.fragment_list_adoption_animals, container, false);

        lvListAdoptionAnimal = rootView.findViewById(R.id.lvListAdoptionAnimal);

        lvListAdoptionAnimal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Animal hasAnimal = (Animal) parent.getItemAtPosition(i);
                System.out.println("--> " + hasAnimal.getName());

                Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
                intent.putExtra(RockChisel.SCENARIO, scenario);
                intent.putExtra(RockChisel.ANIMAL_ID, hasAnimal.getId());

                startActivity(intent);
            }
        });

        swipeRefreshLayout =  rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        SingletonPawsManager.getInstance(getContext()).setAnimalsListListener(this);
        SingletonPawsManager.getInstance(getContext()).getAllAnimalsAPI(getContext());

        return rootView;

    }

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
                ArrayList<Animal> tempAnimals = new ArrayList<>();

                for(Animal animal : listAnimals){

                    switch(animal.getType()){

                        case RockChisel.ADOPTION_ANIMAL:
                            if(animal.getOrganization_district_name().toLowerCase().contains(newText.toLowerCase())){
                                tempAnimals.add(animal);
                            }
                            break;

                        case RockChisel.MISSING_ANIMAL:
                            if(animal.getPublisher_district_name().toLowerCase().contains(newText.toLowerCase())){
                                tempAnimals.add(animal);
                            }
                            break;

                        case RockChisel.FOUND_ANIMAL:
                            if(animal.getFoundAnimal_district_name().toLowerCase().contains(newText.toLowerCase())){
                                tempAnimals.add(animal);
                            }
                            break;
                    }

                }

                lvListAdoptionAnimal.setAdapter(new ListAnimalsAdapter(getContext(), tempAnimals));
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Method called when the swipe is triggered
     */
    @Override
    public void onRefresh() {
        SingletonPawsManager.getInstance(getContext()).getAllAnimalsAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Method called when the animals list is refreshed
     * @param animalsList
     */
    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsList) {
        ArrayList<Animal> newListAnimals = new ArrayList<>();

        switch (scenario){

            case RockChisel.SCENARIO_GENERAL_LIST:

                for (Animal animal : animalsList) {
                    if (animal.getType().equals(animal_type)) {
                        newListAnimals.add(animal);
                    }
                }

                break;

            case RockChisel.SCENARIO_MY_LIST:

                for (Animal animal : animalsList) {
                    //TODO: colocar o id do utilizador dinamico
                    if (animal.getPublisher_id().equals(Vault.getIdLoggedUser(getContext())) && (animal.getType().equals(RockChisel.MISSING_ANIMAL) || animal.getType().equals(RockChisel.FOUND_ANIMAL))) {
                        newListAnimals.add(animal);
                    }
                }

                break;
        }

        listAnimals = newListAnimals;
        lvListAdoptionAnimal.setAdapter(new ListAnimalsAdapter(getContext(), newListAnimals));
    }

    @Override
    public void onUpdateAnimalsList(Animal animal, int operation) {

    }

    @Override
    public void onCreateAnimalFromList() {
        Toast.makeText(getContext(), getString(R.string.msg_animal_created), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateAnimalFromList() {
        Toast.makeText(getContext(), getString(R.string.msg_animal_updated), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteAnimalFromList() {
        Toast.makeText(getContext(), getString(R.string.msg_animal_deleted), Toast.LENGTH_SHORT).show();
    }

}