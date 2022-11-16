package com.gm3_developer.app_mysql_monroy.ui.categorias;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gm3_developer.app_mysql_monroy.MySingleton;
import com.gm3_developer.app_mysql_monroy.R;
import com.gm3_developer.app_mysql_monroy.dto_categorias;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AECategoria extends AppCompatActivity {

    private EditText et_id_categoria, et_nom_categoria;
    private Spinner sp_estado_categoria;
    private Button btnModificarCategoria, btnEliminarCategoria;

    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;

    String id_categoria = "";
    String nom_categoria = "";
    int conta = 0;

    String datoStatusCategoria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ae_categoria);

        et_id_categoria = findViewById(R.id.et_id_categoria);
        et_nom_categoria = findViewById(R.id.et_nom_categoria);
        sp_estado_categoria = findViewById(R.id.sp_estado_categoria);
        btnModificarCategoria = findViewById(R.id.btnModificarCategoria);
        btnEliminarCategoria = findViewById(R.id.btnEliminarCategoria);

        Bundle objeto = getIntent().getExtras();
        dto_categorias dto = null;
        if(objeto!=null){
            dto = (dto_categorias) objeto.getSerializable("categoria");
            et_id_categoria.setText(String.valueOf(dto.getId_categoria()));
            et_nom_categoria.setText(dto.getNom_categoria());

        }

        btnModificarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_categoria = et_id_categoria.getText().toString();
                String nom_categoria = et_nom_categoria.getText().toString();

                if(id_categoria.length() == 0){
                    et_id_categoria.setError("Campo obligatorio");
                }else if(nom_categoria.length() == 0){
                    et_nom_categoria.setError("Campo obligatorio");
                }else if(sp_estado_categoria.getSelectedItemPosition() == 0){
                    Toast.makeText(AECategoria.this, "Debe seleccionar el estado de la categoría.", Toast.LENGTH_SHORT).show();
                }else{
                    modificando_categoria(AECategoria.this, id_categoria, nom_categoria, datoStatusCategoria);
                    finish();
                }
            }
        });





        sp_estado_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sp_estado_categoria.getSelectedItemPosition() > 0){
                    datoStatusCategoria = sp_estado_categoria.getSelectedItem().toString();
                }else{
                    datoStatusCategoria = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void modificando_categoria(final Context context, final String id_categoria, final String nom_categoria, final String estado_categoria){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/actualizar_categoria.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                Toast.makeText(context, "No se pudo actualizar. \n" + "Inténtelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_categoria", id_categoria);
                map.put("nom_categoria", nom_categoria);
                map.put("estado_categoria", estado_categoria);
                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        finish();
    }

    public void eliminar_Categoria(View v){
        String id_categoria = et_id_categoria.getText().toString();

        if(id_categoria.length() == 0){
            et_id_categoria.setError("Campo obligatorio");
        }else{
            eliminando_categoria(AECategoria.this, id_categoria);

        }
    }

    private void eliminando_categoria(final Context context, final String id_categoria) {

        Bundle objeto = getIntent().getExtras();
        dto_categorias dto = null;
        dto = (dto_categorias) objeto.getSerializable("categoria");

        String id_cat = String.valueOf(dto.getId_categoria());
        String nom_cat = dto.getNom_categoria();
        String estado_cat = String.valueOf(dto.getEstado_categoria());


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Advertencia");
        builder.setMessage("¿Está seguro que desea borrar el siguiente registro? \nID Categoría: " + id_cat + "\nNombre de la categoría: " + nom_cat + "\nEstado de categoría: " + estado_cat);
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/eliminar_categoria.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response + " ~ " + id_cat, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "No se pudo eliminar el registro", Toast.LENGTH_SHORT).show();
                    }
                }){

                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Content-Type", "application/json; charset=utf-8");
                        map.put("Accept", "application/json");
                        map.put("id_categoria", id_categoria);
                        return map;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                finish();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }


}