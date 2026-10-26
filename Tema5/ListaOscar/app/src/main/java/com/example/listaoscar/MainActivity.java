package com.example.listaoscar;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Los datos: al menos 8 elementos
        ArrayList<Videojuego> videojuegos = new ArrayList<>();
        videojuegos.add(new Videojuego("The Legend of Zelda", "Nintendo Switch", R.drawable.ic_mando));
        videojuegos.add(new Videojuego("FIFA 23", "PlayStation 5", R.drawable.ic_consola));
        videojuegos.add(new Videojuego("Counter-Strike 2", "PC", R.drawable.ic_pc));
        videojuegos.add(new Videojuego("Clash Royale", "Móvil", R.drawable.ic_movil));
        videojuegos.add(new Videojuego("Mario Kart 8", "Nintendo Switch", R.drawable.ic_mando));
        videojuegos.add(new Videojuego("Gran Turismo 7", "PlayStation 5", R.drawable.ic_consola));
        videojuegos.add(new Videojuego("League of Legends", "PC", R.drawable.ic_pc));
        videojuegos.add(new Videojuego("Brawl Stars", "Móvil", R.drawable.ic_movil));
        videojuegos.add(new Videojuego("Minecraft", "PC", R.drawable.ic_pc));

        // 2. La lista
        RecyclerView rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.setAdapter(new AdaptadorVideojuegos(videojuegos));
    }
}