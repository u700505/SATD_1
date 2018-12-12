package com.example.unaig.satd_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PeticionRegistro extends AppCompatActivity {

    public String mail;
    public String pass;
    public PeticionRegistro(){
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticion_registro);
    }
}
