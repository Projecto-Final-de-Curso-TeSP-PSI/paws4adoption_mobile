package amsi.dei.estg.ipleiria.paws4adoption.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;

public class ListNewAdoptionAnimalsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Animal> newAdoptionAnimals;

    public ListNewAdoptionAnimalsAdapter(Context context, ArrayList<Animal> newAdoptionAnimals) {
        this.context = context;
        this.newAdoptionAnimals = newAdoptionAnimals;
    }

    @Override
    public int getCount() {
        return newAdoptionAnimals.size();
    }

    @Override
    public Object getItem(int position) {
        return newAdoptionAnimals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.fragment_item_list_new_adoption_animals, null);
        }

        ListNewAdoptionAnimalViewHolder listNewAdoptionAnimalViewHolder = (ListNewAdoptionAnimalViewHolder) convertView.getTag();
        if(listNewAdoptionAnimalViewHolder == null){
            listNewAdoptionAnimalViewHolder = new ListNewAdoptionAnimalViewHolder(convertView);
            convertView.setTag(listNewAdoptionAnimalViewHolder);
        }
        listNewAdoptionAnimalViewHolder.update(newAdoptionAnimals.get(position));
        return convertView;
    }

    private class ListNewAdoptionAnimalViewHolder {
        private TextView name, specie, breed;

        public ListNewAdoptionAnimalViewHolder(View convertView){
            name = convertView.findViewById(R.id.tvNewAdoptionAnimalName);
            specie = convertView.findViewById(R.id.tvSpecieName);
            breed = convertView.findViewById(R.id.tvBreedName);
        }

        public void update(Animal animal){
            name.setText(animal.getName());
            specie.setText(animal.getNature_parent_name());
            breed.setText(animal.getNature_name());
        }
    }
}
