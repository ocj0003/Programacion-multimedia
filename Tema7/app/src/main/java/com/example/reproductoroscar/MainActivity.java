package com.example.reproductoroscar;

import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private SeekBar seekBar;
    private TextView tvActual, tvTotal;

    // Efectos de sonido
    private SoundPool soundPool;
    private int idTambores, idTransicion, idGrito, idDramatico;
    private int cargados = 0;              // cuántos sonidos se han cargado ya
    private ColorStateList colorOriginal;  // el morado de los botones

    // Handler que actualiza la barra cada medio segundo
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable actualizarBarra = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress((int) player.getCurrentPosition());
            tvActual.setText(formatear(player.getCurrentPosition()));
            handler.postDelayed(this, 500); // se vuelve a llamar dentro de 500 ms
        }
    };

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

        // Vistas del reproductor
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPausa = findViewById(R.id.btnPausa);
        seekBar = findViewById(R.id.seekBar);
        tvActual = findViewById(R.id.tvActual);
        tvTotal = findViewById(R.id.tvTotal);

        // Crear el reproductor con la canción de res/raw
        player = new ExoPlayer.Builder(this).build();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();

        // Cuando el reproductor está listo ya sabemos la duración
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int estado) {
                if (estado == Player.STATE_READY) {
                    seekBar.setMax((int) player.getDuration());
                    tvTotal.setText(formatear(player.getDuration()));
                }
            }
        });

        // Play: si la canción ha terminado, vuelve al principio
        btnPlay.setOnClickListener(v -> {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });

        // Pausa
        btnPausa.setOnClickListener(v -> player.pause());

        // El usuario mueve la barra: al soltarla, la canción salta a ese punto
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
                tvActual.setText(formatear(seekBar.getProgress()));
            }
        });

        // ===== Ejercicio 2: efectos con SoundPool =====
        Button btnTambores = findViewById(R.id.btnTambores);
        Button btnTransicion = findViewById(R.id.btnTransicion);
        Button btnGrito = findViewById(R.id.btnGrito);
        Button btnDramatico = findViewById(R.id.btnDramatico);

        // Guardamos el color original (morado) para volver a él
        colorOriginal = btnTambores.getBackgroundTintList();

        AudioAttributes atributos = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        // Cuando se carga cada sonido suma 1; con los 4 cargados se activan los botones
        soundPool.setOnLoadCompleteListener((pool, idSonido, estado) -> {
            if (estado == 0) {
                cargados++;
            }
            if (cargados == 4) {
                btnTambores.setEnabled(true);
                btnTransicion.setEnabled(true);
                btnGrito.setEnabled(true);
                btnDramatico.setEnabled(true);
            }
        });

        // Cargar los 4 efectos (guardamos su id)
        idTambores = soundPool.load(this, R.raw.efecto_tambores, 1);
        idTransicion = soundPool.load(this, R.raw.efecto_transicion, 1);
        idGrito = soundPool.load(this, R.raw.efecto_grito, 1);
        idDramatico = soundPool.load(this, R.raw.efecto_dramatico, 1);

        // Cada botón reproduce su efecto y se pone verde un momento
        btnTambores.setOnClickListener(v -> reproducirEfecto(btnTambores, idTambores));
        btnTransicion.setOnClickListener(v -> reproducirEfecto(btnTransicion, idTransicion));
        btnGrito.setOnClickListener(v -> reproducirEfecto(btnGrito, idGrito));
        btnDramatico.setOnClickListener(v -> reproducirEfecto(btnDramatico, idDramatico));
    }

    // Reproduce un efecto y pone su botón en verde durante 1 segundo
    private void reproducirEfecto(Button boton, int idSonido) {
        soundPool.play(idSonido, 1f, 1f, 1, 0, 1f);

        // Verde
        boton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.efecto_activo)));

        // Al pasar 1 segundo vuelve al morado
        handler.postDelayed(() -> boton.setBackgroundTintList(colorOriginal), 1000);
    }

    // Pasa de milisegundos a mm:ss
    private String formatear(long ms) {
        long segundos = ms / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(actualizarBarra); // la barra vuelve a actualizarse
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();                            // la música se para
        handler.removeCallbacks(actualizarBarra);  // la barra deja de actualizarse
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();    // libera el reproductor
        soundPool.release(); // libera los efectos
    }
}