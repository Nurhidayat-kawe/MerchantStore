package com.yyaayyaatt.merchantstore;

import static com.yyaayyaatt.merchantstore.R.id.navigation_home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yyaayyaatt.merchantstore.fragment.AkunFragment;
import com.yyaayyaatt.merchantstore.fragment.HomeFragment;
import com.yyaayyaatt.merchantstore.fragment.PelangganFragment;
import com.yyaayyaatt.merchantstore.fragment.PesananFragment;
import com.yyaayyaatt.merchantstore.fragment.ProdukFragment;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton fab;
    SharedPrefManager sharedPrefManager;
    TextView textCartItemCount;
    int mCartItemCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        loadFragment(new HomeFragment());

        sharedPrefManager = new SharedPrefManager(HomeActivity.this);
        fab = findViewById(R.id.fabHome);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProdukFragment();
                loadFragment(fragment);
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        System.out.println("Token>>>"+token);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("pesanan")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d("Pesanan>>>", msg);
                        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logout() {
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        startActivity(new Intent(HomeActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        MenuItem menuItem = menu.findItem(R.id.menu_item_home_notif);
//        View actionView = menuItem.getActionView();
//        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
//
//        setupBadge();
//
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Fragment fragment = null;
//        switch (item.getItemId()) {
//            case R.id.menu_item_home_notif:
//                // Not implemented here
//                startActivity(new Intent(HomeActivity.this,PesananActivity.class));
//                return false;
//            case R.id.menu_item_home_logout:
//                logout();
//                return false;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

            if(id==navigation_home) {
                fragment = new HomeFragment();
            }
            else if (id==R.id.navigation_pesanan) {
                fragment = new PesananFragment();
            }
            else if(id == R.id.navigation_produk) {
                fragment = new ProdukFragment();
            }
            else if(id==R.id.navigation_pelanggan) {
                fragment = new PelangganFragment();
            }
            else if (id == R.id.navigation_akun) {
                fragment = new AkunFragment();
            }
        return loadFragment(fragment);
    }
}