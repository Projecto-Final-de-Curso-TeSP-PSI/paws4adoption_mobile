package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;

public class MenuMainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String USERNAME = null;
    public static final String TOKEN = null;
    public static final String USER_PREFERENCES = null;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);

        drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();

        navigationView.setCheckedItem(R.id.navHome);

        carregarCabecalho();
        carregarMenuInicial();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        Fragment fragment = null;
        Intent intent = null;

        MenuItem previousSelectedItem = navigationView.getCheckedItem();

        if(previousSelectedItem != null )
            previousSelectedItem.setChecked(false);

        menuItem.setChecked(true);

        switch (menuItem .getItemId()) {
            case R.id.navHome:
                break;
            case R.id.navSearchAnimals:
                break;
            case R.id.navSearchAssociations:
                break;
            case R.id.navMyAnimals:
                break;
            case R.id.navPostWanderingAnimal:
                intent = new Intent(getApplicationContext(), PostAnimalActivity.class);
                break;
            case R.id.navPostLostAnimal:
                break;
            case R.id.navLogin:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                break;

            default:
                //lista de ultimos animais publicados
        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        if(intent != null){
            startActivity(intent);
        }


        //Poderiamos chamar aqui uma activity???

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void carregarCabecalho(){

        //if(getIntent())
        //   email = getIntent().getStringExtra(EMAIL);

        /*View hView = navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.tvEmail);
        nav_user.setText(email);*/
    }

    private void carregarMenuInicial(){

        //Fragment fragment = new ListaLivrosFragment();
        //fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
    }

}