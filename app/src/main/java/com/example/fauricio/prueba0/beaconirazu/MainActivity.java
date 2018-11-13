package com.example.fauricio.prueba0.beaconirazu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton nuevo_usuario;
    public Connection conexionSql;
    private ListView lista;
    private ArrayList<Item> ArrayItem = null;
    private ListAdapter adapter;
    public static ResultSet aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista = findViewById(R.id.ListaUsuario);
        nuevo_usuario = findViewById(R.id.FB_agregar);
        nuevo_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NuevoBeacon.class);
                startActivity(intent);
            }
        });
        ArrayItem = new ArrayList<>();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                //code to do the HTTP request
                servicio servicio1 = new servicio();
                servicio1.execute("");
            }
        });
        thread.start();

        //cargarLista(this);
    }

    public void cargarLista(Context context, ResultSet s){

        try{
            ArrayItem.add(new Item(R.drawable.filelogo,s.getString("NOMBRE"),s.getString("DESCRIPCION")));
            while (s.next()) {
                String nombre = s.getString("NOMBRE");
                String usuario = s.getString("DESCRIPCION");
                //Log.e("===>>>>>","Fecha_Registro: "+nombre+" - Beacon: "+usuario);
                ArrayItem.add(new Item(R.drawable.filelogo,nombre,usuario));
            }
            adapter = new ListAdapter(ArrayItem, context);
            lista.setAdapter(adapter);
        }catch(SQLException e){
            Log.e("error >>",e.getMessage());
        }catch (NetworkOnMainThreadException f){
            Log.e("error >>",f.getMessage());
        }catch (Exception n){
            Log.e("error >>",n.getMessage());
        }
        /*
        ArrayItem.add(new Item(R.drawable.userlogo,"Livertad","Viva la libertad"));
        ArrayItem.add(new Item(R.drawable.userlogo,"Desempleo","Desempleado"));

        */
    }

    public class servicio extends AsyncTask<String,String,ResultSet> {

        @Override
        protected ResultSet doInBackground(String... params) {
            String mensajeAux;
            ResultSet resultSet = null;
            try{
                conexionSql = connectionclass();
                if(conexionSql==null){
                    mensajeAux = "Revisar acceso a internet";
                    Log.e("mesaje >>","Referencia nula");
                }else{

                    CallableStatement cStmt = conexionSql.prepareCall("{call CONSULTAR_TIPO_USUARIO()}");
                    resultSet = cStmt.executeQuery();

                    if(resultSet.next()){
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
        protected void onPostExecute(ResultSet s) {
            cargarLista(getApplicationContext(),s);
            super.onPostExecute(s);
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
            stringConexion = "jdbc:jtds:sqlserver://diseno.database.windows.net:1433;DatabaseName=Proyectos_SETECLab;user=adminDiseno@diseno;password=Irazu2701$;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=3000;";
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
