package amsi.dei.estg.ipleiria.paws4adoption.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
        private TextView type, name, breed, size, sex, postDate;
        private ImageView imageAnimal;

        public ListAnimalViewHolder(View convertView){
            type = convertView.findViewById(R.id.textView_type);
            name = convertView.findViewById(R.id.textView_name);
            breed = convertView.findViewById(R.id.textView_breed);
            size = convertView.findViewById(R.id.textView_size);
            sex = convertView.findViewById(R.id.textView_sex);
            postDate = convertView.findViewById(R.id.textView_postDate);
            imageAnimal = convertView.findViewById(R.id.imgAnimalList);

        }

        public void update(Animal animal){
            switch (animal.getType()){
                case "adoptionAnimal":
                    type.setText("Animal para Adoção");
                    break;
                case "missingAnimal":
                    type.setText("Animal Perdido");
                    break;
                case "foundAnimal":
                    type.setText("Animal Errante");
                    break;
                default:
                    break;


            }

            name.setText(animal.getName());
            breed.setText(animal.getNature_name());
            size.setText(animal.getSize());
            sex.setText(animal.getSex());
            postDate.setText(animal.getCreateAt());
            Glide.with(context)
                    .load(animal.getPhoto())
                    .placeholder(R.drawable.paws4adoption_logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageAnimal);
        }
    }
}
