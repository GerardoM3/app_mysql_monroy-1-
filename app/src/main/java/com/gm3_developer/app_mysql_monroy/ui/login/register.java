package com.gm3_developer.app_mysql_monroy.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gm3_developer.app_mysql_monroy.MySingleton;
import com.gm3_developer.app_mysql_monroy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public register() {
    }

    EditText et_nombre, et_apellidos, et_correo, et_usuario, et_clave, et_respuesta;
    Spinner sp_tipo, sp_estado_usuarios, sp_pregunta;
    Button btnSaveUser, btnCancel;

    String datoTipo, datoStatusUser, datoPregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellidos = findViewById(R.id.et_apellidos);
        et_correo = findViewById(R.id.et_correo);
        et_usuario = findViewById(R.id.et_usuario);
        et_clave = findViewById(R.id.et_clave);
        sp_tipo = findViewById(R.id.sp_tipo);
        sp_estado_usuarios = findViewById(R.id.sp_estado_usuarios);
        sp_pregunta = findViewById(R.id.sp_pregunta);
        et_respuesta = findViewById(R.id.et_respuesta);

        sp_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sp_tipo.getSelectedItemPosition()>0){
                    datoTipo = sp_tipo.getSelectedItem().toString();
                }else{
                    datoTipo = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_estado_usuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sp_estado_usuarios.getSelectedItemPosition()>0){
                    datoStatusUser = sp_estado_usuarios.getSelectedItem().toString();
                }else{
                    datoStatusUser = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_pregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sp_pregunta.getSelectedItemPosition()>0){
                    datoPregunta = sp_pregunta.getSelectedItem().toString();
                }else{
                    datoPregunta = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void registerNow(View v) {
        String nombre = et_nombre.getText().toString();
        String apellidos = et_apellidos.getText().toString();
        String correo = et_correo.getText().toString();
        String usuario = et_usuario.getText().toString();
        String clave = et_clave.getText().toString();
        String respuesta = et_respuesta.getText().toString();

        if(nombre.length() == 0){
            et_nombre.setError("Campo obligatorio");
        }else if(apellidos.length() == 0){
            et_apellidos.setError("Campo obligatorio");
        }else if(correo.length() == 0){
            et_correo.setError("Campo obligatorio");
        }else if(usuario.length() == 0){
            et_usuario.setError("Campo obligatoria");
        }else if(clave.length() == 0){
            et_clave.setError("Campo obligatorio");
        }else if(sp_tipo.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Debe seleccionar el tipo de usuario", Toast.LENGTH_SHORT).show();
        }else if(sp_estado_usuarios.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Debe seleccionar el estado del usuario", Toast.LENGTH_SHORT).show();
        }else if(sp_pregunta.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Debe seleccionar una pregunta de recuperación", Toast.LENGTH_SHORT).show();
        }else if(respuesta.length() == 0){
            et_respuesta.setError("Campo obligatorio");
        }else{

            registrarUser(this, nombre, apellidos, correo, usuario, clave, datoTipo, datoStatusUser, datoPregunta, respuesta);
            finish();

        }
    }


    public void registrarUser(final Context context, final String nombre, final String apellidos, final String correo, final String usuario, final String clave, final String tipo, final String estado, final String pregunta, final String respuesta){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/guardar_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if (estado.equals("1")) {
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    } else if (estado.equals("2")) {
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se pudo guardar. \n" + "Inténtalo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }){

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("nombre", nombre);
                map.put("apellidos", apellidos);
                map.put("correo", correo);
                map.put("usuario", usuario);
                map.put("clave", clave);
                map.put("tipo", tipo);
                map.put("estado", estado);
                map.put("pregunta", pregunta);
                map.put("respuesta", respuesta);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void cancelar(View v){
        finish();
    }


}