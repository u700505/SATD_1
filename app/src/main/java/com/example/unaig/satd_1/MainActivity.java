package com.example.unaig.satd_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.*;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private MobileServiceClient conexionServidorApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            conexionServidorApi = new MobileServiceClient("https://satdprueba1.azurewebsites.net", this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final PeticionRegistro peticionRegistro = new PeticionRegistro();
        peticionRegistro.setMail("mail@mail.com");
        peticionRegistro.setPass("1234");

        ListenableFuture<ResultadoRegistro> resultado = conexionServidorApi.invokeApi("Registro", peticionRegistro, ResultadoRegistro.class);
        Futures.addCallback(resultado, new FutureCallback< ResultadoRegistro>() {
            @Override
            public void onFailure(Throwable exc) {
                // Acciones a realizar si la llamada devuelve un error
            }
            @Override
            public void onSuccess(ResultadoRegistro resultadoCorrecto) {
                // Acciones a realizar si la llamada devuelve un ok
                Toast.makeText(getApplicationContext(), resultadoCorrecto.getResultado(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}