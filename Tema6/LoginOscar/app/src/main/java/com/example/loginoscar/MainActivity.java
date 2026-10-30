package com.example.loginoscar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {


    private int intentos = 0;
    private static final String PREFS = "sesion";
    private static final String CLAVE_CORREO = "correo";

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


        EditText etCorreo = findViewById(R.id.etCorreo);
        EditText etPass = findViewById(R.id.etPass);
        Button btnContinuar = findViewById(R.id.btnContinuar);
        TextView tvMensaje = findViewById(R.id.tvMensaje);
        MaterialSwitch swRecordar = findViewById(R.id.swRecordar);


        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String correoGuardado = prefs.getString(CLAVE_CORREO, "");
        if (!correoGuardado.isEmpty()) {
            etCorreo.setText(correoGuardado);
            swRecordar.setChecked(true);
            etPass.requestFocus();
        }


        btnContinuar.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString();
            String pass = etPass.getText().toString();


            if (correo.isEmpty()) {
                etCorreo.setError(getString(R.string.error_correo_vacio));
                return;
            }
            if (pass.isEmpty()) {
                etPass.setError(getString(R.string.error_pass_vacia));
                return;
            }


            if (correo.equals("oscar@correo.com") && pass.equals("1234")) {
                intentos = 0;

                SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                if (swRecordar.isChecked()) {
                    editor.putString(CLAVE_CORREO, correo);
                } else {
                    editor.remove(CLAVE_CORREO);
                }
                editor.apply();
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_CORREO, correo);
                startActivity(intent);
                finish();
            } else {
                intentos++;
                if (intentos >= 3) {
                    btnContinuar.setEnabled(false);
                    tvMensaje.setText(R.string.msg_demasiados);
                    Toast.makeText(this, R.string.msg_demasiados, Toast.LENGTH_SHORT).show();
                } else {
                    tvMensaje.setText(R.string.msg_incorrecto);
                }
                tvMensaje.setTextColor(getColor(R.color.rojo));
            }
        });
    }
}