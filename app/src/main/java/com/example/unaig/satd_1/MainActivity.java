package com.example.unaig.satd_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.ProgressDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.*;

import java.net.MalformedURLException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MobileServiceClient conexionServidorApi;
    private Spinner TenistaUno;
    private Spinner TenistaDos;
    private Spinner Superficie;
    private EditText Peso1;
    private EditText Peso2;
    private EditText Peso3;
    private Button button;
    private String tenista1;
    private String tenista2;
    private String tipoSuperficie;
    private Float porcentaje1;
    private Float porcentaje2;
    private Float porcentaje3;
    private ProgressDialog progressDialog;
    private TextView global1;
    private TextView global2;
    private TextView superficie1;
    private TextView superficie2;
    private TextView directo1;
    private TextView directo2;
    private TextView coeficiente1;
    private TextView coeficiente2;
    private TextView result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);

        TenistaUno = (Spinner)findViewById(R.id.TenistaUno);
        TenistaDos = (Spinner)findViewById(R.id.TenistaDos);
        Superficie = (Spinner)findViewById(R.id.Superficie);
        String[] Superficies = {"Dura", "Tierra", "Hierba"};
        Superficie.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Superficies));
        Peso1 = (EditText) findViewById(R.id.peso1);
        Peso2 = (EditText)findViewById(R.id.peso2);
        Peso3 = (EditText)findViewById(R.id.peso3);

        progressDialog = new ProgressDialog(this);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {
        recomendar();
    }

    public void recomendar(){
        result = (TextView)findViewById(R.id.textView3);

        tenista1 = TenistaUno.getSelectedItem().toString();
        tenista2 = TenistaDos.getSelectedItem().toString();
        tipoSuperficie = Superficie.getSelectedItem().toString();
        porcentaje1 = Float.parseFloat(Peso1.getText().toString());
        porcentaje2 = Float.parseFloat(Peso2.getText().toString());
        porcentaje3 = Float.parseFloat(Peso3.getText().toString());

        global1 = (TextView) findViewById(R.id.textView9);
        global2 = (TextView) findViewById(R.id.textView12);
        superficie1 = (TextView) findViewById(R.id.textView10);
        superficie2 = (TextView) findViewById(R.id.textView13);
        directo1 = (TextView) findViewById(R.id.textView11);
        directo2 = (TextView) findViewById(R.id.textView14);
        coeficiente1 = (TextView) findViewById(R.id.textView17);
        coeficiente2 = (TextView) findViewById(R.id.textView16);

        if((porcentaje1+porcentaje2+porcentaje3) != 100){
            Toast.makeText(this, "Los pesos no suman 100%", Toast.LENGTH_LONG).show();
            return;
        }

        if(tenista1.equals(tenista2)){
            Toast.makeText(this, "Seleccione dos tenistas diferentes", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Conectando al servidor...");
        progressDialog.show();

        try {
            conexionServidorApi = new MobileServiceClient("https://satdprueba1.azurewebsites.net", this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        final PeticionRecomendacion peticionRecomendacion = new PeticionRecomendacion();
        peticionRecomendacion.setTenistaUno(tenista1);
        peticionRecomendacion.setTenistaDos(tenista2);
        peticionRecomendacion.setSuperficie(tipoSuperficie);

        ListenableFuture<RespuestaRecomendacion> resultado = conexionServidorApi.invokeApi("ObtenerRecomendacion", peticionRecomendacion, RespuestaRecomendacion.class);
        Futures.addCallback(resultado, new FutureCallback< RespuestaRecomendacion>() {
            @Override
            public void onFailure(Throwable exc) {
                // Acciones a realizar si la llamada devuelve un error
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo en el servidor",
                        Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSuccess(RespuestaRecomendacion resultadoCorrecto) {
                // Acciones a realizar si la llamada devuelve un ok
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Opcion 1: " + resultadoCorrecto.getMesesOpcionUno() + "Opcion 2: " + resultadoCorrecto.getMesesOpcionDos(),
                        //Toast.LENGTH_LONG).show();
                DecimalFormat formato1 = new DecimalFormat("#.##");
                global1.setText(formato1.format(resultadoCorrecto.getGlobal1()) + "%");
                global2.setText(formato1.format(resultadoCorrecto.getGlobal2()) + "%");
                superficie1.setText(formato1.format(resultadoCorrecto.getSuperficie1()) + "%");
                superficie2.setText(formato1.format(resultadoCorrecto.getSuperficie2()) + "%");
                directo1.setText(formato1.format(resultadoCorrecto.getDirecto1()) + "%");
                directo2.setText(formato1.format(resultadoCorrecto.getDirecto2()) + "%");
                float coef1 = (resultadoCorrecto.getGlobal1() * (porcentaje1/100) + resultadoCorrecto.getSuperficie1() * (porcentaje2/100) + resultadoCorrecto.getDirecto1() * (porcentaje3/100))/100;
                float coef2 = (resultadoCorrecto.getGlobal2() * (porcentaje1/100) + resultadoCorrecto.getSuperficie2() * (porcentaje2/100) + resultadoCorrecto.getDirecto2() * (porcentaje3/100))/100;
                coeficiente1.setText(formato1.format(coef1));
                coeficiente2.setText(formato1.format(coef2));

                if (coef1 > coef2){
                    result.setText("Recomendación: Opción 1");
                }
                if (coef2 > coef1){
                    result.setText("Recomendación: Opción 2");
                }
            }
        });
    }

}