package com.gm3_developer.app_mysql_monroy.ui.productos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gm3_developer.app_mysql_monroy.MySingleton;
import com.gm3_developer.app_mysql_monroy.R;
import com.gm3_developer.app_mysql_monroy.dto_categorias;
import com.gm3_developer.app_mysql_monroy.dto_productos;
import com.gm3_developer.app_mysql_monroy.ui.categorias.AECategoria;
import com.gm3_developer.app_mysql_monroy.ui.categorias.ConsultaCategoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AEProducto extends AppCompatActivity {

    private EditText et_nom_producto, et_des_producto, et_stock, et_precio, et_unidad_medida, et_id_producto;
    private Spinner sp_fk_categoria, sp_estado_Producto;
    private TextView tv_fechahora2;
    private Button btnActualizar, btnEliminar;

    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;

    String id_categoria = "";
    String nom_categoria = "";
    int conta = 0;

    String datoStatusProduct = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ae_producto);

        et_id_producto = findViewById(R.id.et_id_producto);
        et_nom_producto = findViewById(R.id.et_nombre_producto);
        et_des_producto = findViewById(R.id.et_des_producto);
        et_stock = findViewById(R.id.et_stock);
        et_precio = findViewById(R.id.et_precio);
        et_unidad_medida = findViewById(R.id.et_unidad_medida);
        sp_estado_Producto = findViewById(R.id.sp_estadoProducto);
        sp_fk_categoria = findViewById(R.id.sp_fk_categoria);
        tv_fechahora2 = findViewById(R.id.tv_fechahora2);
        tv_fechahora2.setText(timedate());
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);

        Bundle objeto = getIntent().getExtras();
        dto_productos dto = null;
        if(objeto!=null){
            dto = (dto_productos) objeto.getSerializable("producto");
            et_id_producto.setText(String.valueOf(dto.getId_producto()));
            et_nom_producto.setText(dto.getNom_producto());
            et_des_producto.setText(dto.getDes_producto());
            et_stock.setText(String.valueOf(dto.getStock()));
            et_precio.setText(String.valueOf(dto.getPrecio()));
            et_unidad_medida.setText(dto.getUnidad_medida());

        }

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dto_productos dto = null;
                String id_producto = et_id_producto.getText().toString();
                String nombre = et_nom_producto.getText().toString();
                String descripcion = et_des_producto.getText().toString();
                String stock = et_stock.getText().toString();
                String precio = et_precio.getText().toString();
                String unidad = et_unidad_medida.getText().toString();

                if (nombre.length() == 0){
                    et_nom_producto.setError("Campo obligatorio");
                }else if(descripcion.length() == 0){
                    et_des_producto.setError("Campo obligatorio");
                }else if(stock.length() == 0){
                    et_stock.setError("Campo obligatorio");
                }else if(precio.length() == 0){
                    et_precio.setError("Campo obligatorio");
                }else if(unidad.length() == 0){
                    et_unidad_medida.setError("Campo obligatorio");
                }else if(sp_estado_Producto.getSelectedItemPosition() == 0){
                    Toast.makeText(AEProducto.this, "Debe seleccionar el estado del producto.", Toast.LENGTH_SHORT).show();
                }else if(sp_fk_categoria.getSelectedItemPosition() == 0){
                    Toast.makeText(AEProducto.this, "Debe seleccionar la categoría.", Toast.LENGTH_SHORT).show();
                }else{
                    modificando_producto(AEProducto.this, id_producto, nombre, descripcion, stock, precio, unidad, datoStatusProduct, id_categoria);
                }
            }
        });


        sp_estado_Producto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sp_estado_Producto.getSelectedItemPosition() > 0){
                    datoStatusProduct = sp_estado_Producto.getSelectedItem().toString();
                }else{
                    datoStatusProduct = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fk_categorias(this);

        sp_fk_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if(conta >= 1 && sp_fk_categoria.getSelectedItemPosition() > 0){
                    String item_spinner = sp_fk_categoria.getSelectedItem().toString();
                    String s[] = item_spinner.split("~");

                    id_categoria = s[0].trim();
                    nom_categoria = s[1];

                    Toast toast = Toast.makeText(AEProducto.this, "Id cat: " + id_categoria + "\n" + "Nombre Categoria: " + nom_categoria, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    id_categoria = "";
                    nom_categoria = "";
                }

                conta++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String timedate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String fecha = sdf.format(cal.getTime());
        return fecha;
    }

    public void fk_categorias(final Context context){
        listaCategorias = new ArrayList<dto_categorias>();
        lista = new ArrayList<String>();
        lista.add("Seleccionar categoría");

        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/buscar_Categoria.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    int totalEncontrados = array.length();

                    dto_categorias obj_categorias = null;

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject categoriasObject = array.getJSONObject(i);
                        int id_categoria = categoriasObject.getInt("id_categoria");
                        String nom_categoria = categoriasObject.getString("nom_categoria");
                        int estado_categoria = Integer.parseInt(categoriasObject.getString("estado_categoria"));
                        obj_categorias = new dto_categorias(id_categoria, nom_categoria, estado_categoria);
                        listaCategorias.add(obj_categorias);
                        lista.add(listaCategorias.get(i).getId_categoria() + "~" + listaCategorias.get(i).getNom_categoria());
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lista);
                        sp_fk_categoria.setAdapter(adaptador);

                        Log.i("Id Categoria", String.valueOf(obj_categorias.getId_categoria()));
                        Log.i("Nombre Categoria", obj_categorias.getNom_categoria());
                        Log.i("Estado Categoria", String.valueOf(obj_categorias.getEstado_categoria()));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error. Compruebe su acceso a Internet.", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }



    private void modificando_producto(final Context context, final String id, final String nom_prod, final String des_prod, final String stock, final String precio, final String uni_medida, final String estado_prod, final String categoria){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/actualizar_producto.php";
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
                Toast.makeText(context, "No se pudo actualizar. \n" + "Inténtelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_producto", id);
                map.put("nom_producto", nom_prod);
                map.put("des_producto", des_prod);
                map.put("stock", stock);
                map.put("precio", precio);
                map.put("unidad_medida", uni_medida);
                map.put("estado_producto", estado_prod);
                map.put("categoria", categoria);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        finish();
    }

    public void eliminar_Producto(View v){
        String id_producto = et_id_producto.getText().toString();

        if(id_producto.length() == 0){
            et_id_producto.setError("Campo obligatorio");
        }else{
            eliminando_producto(AEProducto.this, id_producto);

        }
    }

    private void eliminando_producto(final Context context, final String id_producto) {

        Bundle objeto = getIntent().getExtras();
        dto_productos dto = null;
        dto = (dto_productos) objeto.getSerializable("producto");

        String id_prod = String.valueOf(dto.getId_producto());
        String nom_prod = dto.getNom_producto();
        String des_prod = String.valueOf(dto.getDes_producto());


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Advertencia");
        builder.setMessage("¿Está seguro que desea borrar el siguiente registro? \nID Producto: " + id_prod + "\nNombre del producto: " + nom_prod + "\nDescripción del producto: " + des_prod);
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/eliminar_producto.php";
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
                        Toast.makeText(context, "No se pudo eliminar el registro", Toast.LENGTH_SHORT).show();
                    }
                }){

                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Content-Type", "application/json; charset=utf-8");
                        map.put("Accept", "application/json");
                        map.put("id_producto", id_producto);
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