package com.example.piacpalota;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.piacpalota.domain.Buy;
import com.example.piacpalota.u.i.buydetails.BuyDetailsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.piacpalota.u.i.AccountFragment;
import com.example.piacpalota.u.i.buylist.BuyFragment;
import com.example.piacpalota.u.i.ChooseFragment;
import com.example.piacpalota.u.i.HomeFragment;
import com.example.piacpalota.u.i.LogInFragment;
import com.example.piacpalota.u.i.MessageFragment;
import com.example.piacpalota.u.i.SalesFragment;
import com.example.piacpalota.u.i.ShoppingFragment;
import com.example.piacpalota.u.i.SingInFragment;
import com.example.piacpalota.u.i.UpLoadFragment;
import com.example.piacpalota.u.i.AboutFragment;

public class MainActivity extends AppCompatActivity implements BuyFragment.OnBuySelectedListener {

    private Buy selectedBuy;

    public Buy getSelectedBuy() {
        return selectedBuy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle("Piac Palota");

        // Initial Fragment
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }
    }

    @Override
    public void onBuySelected(Buy buy) {
        showBuyDetails(buy);  // Meghívjuk a showBuyDetails metódust
    }

    public void showBuyDetails(Buy buy) {
        this.selectedBuy = buy;
        loadFragment(new BuyDetailsFragment(), "buyDetails");
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            getMenuInflater().inflate(R.menu.menu_logged_in, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = null;

        // Ellenőrizd a navigációs gombot (vissza gomb)
        if (id == android.R.id.home) {
            onBackPressed(); // Visszalépés az előző fragmentre
            return true; // Visszatér a metódusból
        }

        // Az action menüelemek kezelése
        if (id == R.id.action_home) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.action_sales) {
            selectedFragment = new SalesFragment();
        } else if (id == R.id.action_buy) {
            selectedFragment = new BuyFragment();
        } else if (id == R.id.action_upload) {
            selectedFragment = new UpLoadFragment();
        } else if (id == R.id.action_choose) {
            selectedFragment = new ChooseFragment();
        } else if (id == R.id.action_shopping) {
            selectedFragment = new ShoppingFragment();
        } else if (id == R.id.action_message) {
            selectedFragment = new MessageFragment();
        } else if (id == R.id.action_account) {
            selectedFragment = new AccountFragment();
        } else if (id == R.id.action_login) {
            selectedFragment = new LogInFragment();
        } else if (id == R.id.action_singin) {
            selectedFragment = new SingInFragment();
        } else if (id == R.id.action_about) {
            selectedFragment = new AboutFragment(); // Navigálj az AboutFragment-re
        }

        if (selectedFragment != null) {
            replaceFragment(selectedFragment);
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment) // Győződj meg róla, hogy a megfelelő konténer ID-t használod
                .addToBackStack(null) // Opcionális: hozzáadja a tranzakciót a visszaállítási veremhez
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(); // Visszalépés az előző fragmentre
        } else {
            super.onBackPressed(); // Alapértelmezett visszalépés, ha nincs visszaállítási verem
        }
    }
}
