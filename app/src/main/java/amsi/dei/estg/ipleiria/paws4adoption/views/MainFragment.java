package amsi.dei.estg.ipleiria.paws4adoption.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalsListListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements MqttCallback {

    private String scenario = null;
    private String animal_type = null;
    private ListView lvMosquittoListNewAdoptionAnimals;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            MqttClient client = new MqttClient("tcp://192.168.1.65:1883", "Paws4AdoptionMobileSub", new MemoryPersistence());
            client.setCallback(this);
            client.connect();
            String topic = "NEW_ADOPTION_ANIMAL";
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if(bundle != null){
            scenario = bundle.getString(RockChisel.SCENARIO);

            if(!scenario.equals(RockChisel.SCENARIO_MY_LIST))
                animal_type = bundle.getString(RockChisel.ANIMAL_TYPE);
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lvMosquittoListNewAdoptionAnimals = rootView.findViewById(R.id.lvMosquittoListNewAdoptionAnimals);

        lvMosquittoListNewAdoptionAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        SingletonPawsManager.getInstance(getContext()).getAllAnimalsAPI(getContext());

        return rootView;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        Log.d(TAG, payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "--> deliveryComplete");
    }
}