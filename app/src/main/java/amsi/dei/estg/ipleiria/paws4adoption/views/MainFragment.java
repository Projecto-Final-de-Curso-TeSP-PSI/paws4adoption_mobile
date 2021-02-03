package amsi.dei.estg.ipleiria.paws4adoption.views;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.adapters.ListAnimalsAdapter;
import amsi.dei.estg.ipleiria.paws4adoption.adapters.ListNewAdoptionAnimalsAdapter;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalsListListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.NetworkStateReceiver;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment
        implements MqttCallback, NetworkStateReceiver.NetworkStateReceiverListener {

    private String scenario = null;
    private String animal_type = null;
    private ListView lvMosquittoListNewAdoptionAnimals;
    private ArrayList<Animal> latestAdoptionAnimals;
    private Handler mHandler;
    private NetworkStateReceiver networkStateReceiver;
    private MqttClient client;
    private MqttConnectOptions mqttConnectOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        Objects.requireNonNull(getActivity()).registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        latestAdoptionAnimals = new ArrayList<>();

        mHandler = new Handler();
    }

    @Override
    public void onPause(){
        super.onPause();
        Vault.setLatestAnimals(getContext(), latestAdoptionAnimals);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Animal> savedAnimals = Vault.getLatestAnimals(getContext());
        if ( savedAnimals != null){
            for (int i = 0; i < savedAnimals.size() && latestAdoptionAnimals.size() < 5 ; i++){
                latestAdoptionAnimals.add(savedAnimals.get(i));
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    lvMosquittoListNewAdoptionAnimals.setAdapter(new ListNewAdoptionAnimalsAdapter(getContext(), latestAdoptionAnimals));
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            String[] url = new String[1];
            url[0] = "tcp://"+RockChisel.COMPUTER_LOCAL_IP+":1883";
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);

            client = new MqttClient("tcp://"+RockChisel.COMPUTER_LOCAL_IP+":1883", "Paws4AdoptionMobileSub", new MemoryPersistence());

            if (!client.isConnected()){
                client.setCallback(this);
                client.connect(mqttConnectOptions);
                String topic = "NEW_ADOPTION_ANIMAL";
                client.subscribe(topic);
            }
        } catch (MqttException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lvMosquittoListNewAdoptionAnimals = rootView.findViewById(R.id.lvMosquittoListNewAdoptionAnimals);

        lvMosquittoListNewAdoptionAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Animal hasAnimal = (Animal) parent.getItemAtPosition(i);
                System.out.println("--> " + hasAnimal.getName());

                Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
                intent.putExtra(RockChisel.SCENARIO, RockChisel.ADOPTION_ANIMAL);
                intent.putExtra(RockChisel.ANIMAL_ID, hasAnimal.getId());

                startActivity(intent);
            }
        });

        return rootView;
    }

    private void runOnUiThread(Runnable runnable) {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        Log.d(TAG, payload);

        JSONObject adoptionAnimalJson = new JSONObject(payload);

        Animal auxAnimal = new Animal();
        auxAnimal.setId(adoptionAnimalJson.getInt("id"));
        auxAnimal.setName(adoptionAnimalJson.getString("name"));
        auxAnimal.setNature_parent_name(adoptionAnimalJson.getString("parent_nature_name"));
        auxAnimal.setNature_name(adoptionAnimalJson.getString("nature_name"));

        latestAdoptionAnimals.add(0, auxAnimal);
        if (latestAdoptionAnimals.size() > 5){
            latestAdoptionAnimals.remove(5);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                lvMosquittoListNewAdoptionAnimals.setAdapter(new ListNewAdoptionAnimalsAdapter(getContext(), latestAdoptionAnimals));
            }
        });

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    lvMosquittoListNewAdoptionAnimals.setAdapter(new ListNewAdoptionAnimalsAdapter(getContext(), latestAdoptionAnimals));
//                } catch (Exception e){
//                    System.err.println("--> " + e.getMessage());
//                }
//            }
//        });


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("My First Notification")
//                .setContentText("Teste de texto comprido que não cabe apenas numa linha... Much longer text that cannot fit one line...")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Teste de texto comprido que não cabe apenas numa linha... Much longer text that cannot fit one line..."))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "--> deliveryComplete");
    }

    @Override
    public void networkAvailable() {
        Log.d("net", "---> Com net");
        lvMosquittoListNewAdoptionAnimals.setEnabled(true);
        lvMosquittoListNewAdoptionAnimals.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLigth));
        try {
            if (!client.isConnected()){
                client.setCallback(this);
                client.connect(mqttConnectOptions);
                String topic = "NEW_ADOPTION_ANIMAL";
                client.subscribe(topic);
            }
        } catch (MqttException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void networkUnavailable() {
        Log.d("unet", "---> Sem net");
        lvMosquittoListNewAdoptionAnimals.setEnabled(false);
        lvMosquittoListNewAdoptionAnimals.setBackgroundColor(Color.WHITE);
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.new_adoption_animal);
//            String description = getString(R.string.new_adoption_animal_desc);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
}