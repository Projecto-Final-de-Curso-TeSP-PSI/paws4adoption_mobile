package amsi.dei.estg.ipleiria.paws4adoption.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;

public class ListAnimalsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Animal> animals;

    public ListAnimalsAdapter(Context context, ArrayList<Animal> animals){
        this.context = context;
        this.animals = animals;
    }

    @Override
    public int getCount() {
        return animals.size();
    }

    @Override
    public Object getItem(int position) {
        return animals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.fragment_item_list_animals, null);
        }
        ListAnimalViewHolder listAnimalviewHolder = (ListAnimalViewHolder) convertView.getTag();
        if(listAnimalviewHolder == null){
            listAnimalviewHolder = new ListAnimalViewHolder(convertView);
            convertView.setTag(listAnimalviewHolder);
        }
        listAnimalviewHolder.update(animals.get(position));
        return convertView;
    }

    private class ListAnimalViewHolder{
        private TextView name, breed, size, sex, postDate;
        private ImageView imageAnimal;

        public ListAnimalViewHolder(View convertView){
            name = convertView.findViewById(R.id.textView_name);
            breed = convertView.findViewById(R.id.textView_breed);
            size = convertView.findViewById(R.id.textView_size);
            sex = convertView.findViewById(R.id.textView_sex);
            postDate = convertView.findViewById(R.id.textView_postDate);
            imageAnimal = convertView.findViewById(R.id.imgAnimalPhoto);
        }

        public void update(Animal animal){
            name.setText(animal.getName());
            breed.setText(animal.getNature_parent_name());
            size.setText(animal.getSize());
            sex.setText(animal.getSex());
            postDate.setText(animal.getCreateAt());
            imageAnimal.setImageResource(animal.getPhoto());
        }
    }
}
