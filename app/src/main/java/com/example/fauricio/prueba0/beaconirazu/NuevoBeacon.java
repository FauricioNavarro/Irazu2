package com.example.fauricio.prueba0.beaconirazu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NuevoBeacon extends AppCompatActivity {
    public Connection conexionSql;
    public EditText nombre,descripcion;
    public String nombreAux,descripcionAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_beacon);
        nombre = findViewById(R.id.et_nombre);
        descripcion = findViewById(R.id.et_descrip);

    }

    public void Registrar(View view){
        nombreAux = nombre.getText().toString();
        descripcionAux = descripcion.getText().toString();

        servicio servicioAux = new servicio();
        servicioAux.execute(nombreAux,descripcionAux);
    }

    public class servicio extends AsyncTask<String,String,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String mensajeAux;
            Boolean resultSet = null;
            try{
                conexionSql = connectionclass();
                if(conexionSql==null){
                    mensajeAux = "Revisar acceso a internet";
                    Log.e("mesaje >>","Referencia nula");
                }else{
                    CallableStatement cStmt = conexionSql.prepareCall("{call INSERTAR_TIPO_USUARIO(?,?)}");
                    cStmt.setString(1,params[0]);
                    cStmt.setString(2,params[1]);
                    resultSet = cStmt.execute();

                    if(!resultSet){
                        mensajeAux = "Query exitoso";
                    }else{
                        mensajeAux = "Query no exitoso";
                    }
                    //CallableStatement callSp = conexionSql.prepareCall("{call TablaProyectos()}");
                }
            }catch (Exception e){
                mensajeAux = e.getMessage();
            }
            //textView.setText(mensaje);
            Log.e("mesaje final >>",mensajeAux);
            return resultSet;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            super.onPostExecute(aBoolean);
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String stringConexion;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            stringConexion = "jdbc:jtds:sqlserver://diseno.database.windows.net:1433;DatabaseName=Proyectos_SETECLab;user=adminDiseno@diseno;password=Irazu2701$;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            //stringConexion = "jdbc:jtds:sqlserver://192.168.100.7:1433;database=Proyectos_SETECLab;user=adminDiseno@diseno;password=Irazu2701$;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            //stringConexion = "jdbc:jtds:sqlserver://172.19.48.1:1433;database=Proyectos_SETECLab;user=adminDiseno@diseno;password=Irazu2701$;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

            Log.e("MSG >>>>","Entro string de conexion");
            connection = DriverManager.getConnection(stringConexion);
            //connection = DriverManager.getConnection(stringConexion);
        }catch (SQLException e){
            Log.e("error 1",e.getMessage());
        }catch (ClassNotFoundException a){
            Log.e("error 2",a.getMessage());
        }catch (Exception f){
            Log.e("error 3",f.getMessage());
        }
        return connection;
    }
}
