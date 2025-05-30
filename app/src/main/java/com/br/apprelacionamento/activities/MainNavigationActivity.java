package com.br.apprelacionamento.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.br.apprelacionamento.R;
import com.br.apprelacionamento.fragments.ContatosFragment;
import com.br.apprelacionamento.fragments.MatchFragment;
import com.br.apprelacionamento.fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainNavigationActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        settingsIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.END);
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            preferences.edit().clear().apply();
            startActivity(new Intent(MainNavigationActivity.this, LoginActivity.class));
            finish();
        });


        // Inicializa ViewPager2
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));
        viewPager.setCurrentItem(1); // Começa na tela do meio (Match)

        // Inicializa BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Navegar entre as telas ao clicar nos ícones
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_perfil) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (item.getItemId() == R.id.navigation_match) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (item.getItemId() == R.id.navigation_contatos) {
                viewPager.setCurrentItem(2);
                return true;
            }
            return false;
        });

        // Atualizar o botão ativo quando deslizar o ViewPager
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_perfil);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_match);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_contatos);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Sair do aplicativo")
                .setMessage("Você deseja realmente sair?")
                .setPositiveButton("Sim", (dialog, which) -> finishAffinity()) // Fecha todas as activities
                .setNegativeButton("Não", null)
                .show();
    }


    // Adapter para gerenciar os fragments
    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new PerfilFragment();
                case 1:
                    return new MatchFragment();
                case 2:
                    return new ContatosFragment();
                default:
                    return new MatchFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
