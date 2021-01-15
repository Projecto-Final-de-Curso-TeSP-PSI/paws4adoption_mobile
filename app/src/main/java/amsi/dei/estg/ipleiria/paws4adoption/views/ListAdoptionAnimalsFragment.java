package amsi.dei.estg.ipleiria.paws4adoption.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.adapters.ListAnimalsAdapter;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;


public class ListAdoptionAnimalsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AnimalListener {

    //################ INTENT PARAMETERS ################
    private static final String scenario = null;
    private String type_list = null;

    private ListView lvListAdoptionAnimal;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            type_list = bundle.getString(RockChisel.ANIMAL_TYPE);
        }

        View rootView = inflater.inflate(R.layout.fragment_list_adoption_animals, container, false);

        lvListAdoptionAnimal = rootView.findViewById(R.id.lvListAdoptionAnimal);

        lvListAdoptionAnimal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Animal hasAnimal = (Animal) parent.getItemAtPosition(i);
                System.out.println("--> " + hasAnimal.getName());

                Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
                intent.putExtra(AnimalDetailsActivity.ANIMAL_ID, hasAnimal.getId());
                intent.putExtra(AnimalDetailsActivity.SCENARIO, hasAnimal.getType());
                startActivity(intent);
            }
        });

        SingletonPawsManager.getInstance(getContext()).setAnimalListener(this);
        SingletonPawsManager.getInstance(getContext()).getAllAnimalsAPI(getContext());

        return rootView;

    }



    @Override
    public void onRefresh() {
        //TODO::
    }

    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsList) {
        ArrayList<Animal> listAnimals = new ArrayList<>();
        if (animalsList != null){
            if(type_list.equals(RockChisel.SCENARIO_MY_LIST)){
                for (Animal animal : animalsList) {
                    if (animal.getPublisher_id().equals(Vault.getIdLoggedUser(getContext()))){
                        if(!animal.getType().equals("adoptionAnimal")) {
                            listAnimals.add(animal);
                        }

                    }
                }
            }else{
                for (Animal animal : animalsList) {
                    if (animal.getType().equals(type_list)) {
                        listAnimals.add(animal);
                    }
                }
            }

            lvListAdoptionAnimal.setAdapter(new ListAnimalsAdapter(getContext(), listAnimals));
        }
    }

    @Override
    public void onUpdateAnimalsList(Animal animal, int operation) {

    }

    @Override
    public void onGetAnimalAPI(Animal animal) {

    }
}