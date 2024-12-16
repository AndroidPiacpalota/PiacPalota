package com.example.piacpalota;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.piacpalota.u.i.MessagesFragment;
import com.example.piacpalota.u.i.buylist.Product;
import com.example.piacpalota.u.i.AccountFragment;
import com.example.piacpalota.u.i.AboutFragment;
import com.example.piacpalota.u.i.BuyFragment;
import com.example.piacpalota.u.i.ChooseFragment;
import com.example.piacpalota.u.i.HomeFragment;
import com.example.piacpalota.u.i.LogInFragment;
import com.example.piacpalota.u.i.SalesFragment;
import com.example.piacpalota.u.i.ShoppingFragment;
import com.example.piacpalota.u.i.SingInFragment;
import com.example.piacpalota.u.i.UpLoadFragment;
import com.example.piacpalota.u.i.WelcomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BuyFragment.OnBuySelectedListener {

    private static final int REQUEST_MEDIA_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle("Piac Palota");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back/home button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Set back button icon

        // Modern back button handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack(); // Navigate back to the previous fragment
                } else {
                    finish(); // Exit the activity
                }
            }
        });

        // Load default fragment if no saved instance state
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        // Check media permissions
        checkMediaPermissions();
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (id == R.id.action_home) {
            // Ha a felhasználó be van jelentkezve, akkor a WelcomeFragment, ha nincs, akkor a HomeFragment jelenik meg
            selectedFragment = (user != null) ? new WelcomeFragment() : new HomeFragment();
        } else if (id == android.R.id.home) {
            // Handle back navigation for home button
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // Go back to the previous fragment if available
                getSupportFragmentManager().popBackStack();
            } else {
                // If no fragments are left in the back stack, exit the activity
                finish();
            }
            return true;
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
            selectedFragment = new MessagesFragment();
        } else if (id == R.id.action_account) {
            selectedFragment = new AccountFragment();
        } else if (id == R.id.action_login) {
            selectedFragment = new LogInFragment();
        } else if (id == R.id.action_singin) {
            selectedFragment = new SingInFragment();
        } else if (id == R.id.action_about) {
            selectedFragment = new AboutFragment();
        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            invalidateOptionsMenu();
            replaceFragment(new HomeFragment()); // After logout, return to HomeFragment
            return true;
        }

        if (selectedFragment != null) {
            replaceFragment(selectedFragment);
        }

        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null) // Add to back stack
                    .commit();
        } catch (Exception e) {
            Log.e("FragmentTransaction", "Error replacing fragment", e);
        }
    }

    private void checkMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33)
            boolean hasImagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            boolean hasVideoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;

            if (!hasImagePermission || !hasVideoPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO
                        },
                        REQUEST_MEDIA_PERMISSIONS);
            }
        } else {
            // Android 12 és korábbi
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_MEDIA_PERMISSIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MEDIA_PERMISSIONS) {
            boolean granted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                Log.i("Permissions", "Media permissions granted.");
            } else {
                Log.e("Permissions", "Media permissions denied.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBuySelected(Product product) {
        // Handle item selection from BuyFragment
        Log.d("BuyFragment", "Selected product: " + product.getName());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Kijelentkezés, ha a felhasználó elhagyja az aktivitást
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            auth.signOut(); // Kijelentkezés
            Log.d("MainActivity", "Felhasználó automatikusan kijelentkezett.");
        }
    }
}
