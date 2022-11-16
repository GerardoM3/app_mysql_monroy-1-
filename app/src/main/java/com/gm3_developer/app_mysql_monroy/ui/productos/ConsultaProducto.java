package com.gm3_developer.app_mysql_monroy.ui.productos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gm3_developer.app_mysql_monroy.MySingleton;
import com.gm3_developer.app_mysql_monroy.R;
import com.gm3_developer.app_mysql_monroy.dto_productos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ConsultaProducto extends Fragment {

    ListView listViewProductos;
    ArrayAdapter adapter;

    ArrayList<String> lista = null;
    ArrayList<dto_productos> listaProductos;



    public ConsultaProducto() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_producto, container, false);

        listViewProductos = view.findViewById(R.id.listViewProductos);

        allQueryProductos(getContext());

        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                String informacion = "ID Producto: " + listaProductos.get(pos).getId_producto();
                informacion += "Nombre del producto: " + listaProductos.get(pos).getNom_producto();
                informacion += "Descripción del producto: " + listaProductos.get(pos).getDes_producto();
                informacion += "Stock: " + listaProductos.get(pos).getStock();
                informacion += "Precio: " + listaProductos.get(pos).getPrecio();
                informacion += "Unidad Medida: " + listaProductos.get(pos).getUnidad_medida();
                informacion += "Estadodel producto: " + listaProductos.get(pos).getEstado_producto();
                informacion += "Categoría del producto: " + listaProductos.get(pos).getCategoria();

                dto_productos productos = listaProductos.get(pos);
                Intent i = new Intent(getContext(), AEProducto.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("producto", productos);
                i.putExtras(bundle);
                startActivity(i);


            }
        });

        return view;
    }


    public void allQueryProductos(final Context context){
        listaProductos = new ArrayList<dto_productos>();
        lista = new ArrayList<String>();

        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/buscar_Producto.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    int totalEncontrados = array.length();

                    dto_productos obj_productos = null;

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject productosObject = array.getJSONObject(i);
                        int id_producto = productosObject.getInt("id_producto");
                        String nom_producto = productosObject.getString("nom_producto");
                        String des_producto = productosObject.getString("des_producto");
                        double stock = Double.parseDouble(productosObject.getString("stock"));
                        double precio = Double.parseDouble(productosObject.getString("precio"));
                        String unidad_medida = productosObject.getString("unidad_medida");
                        int estado_producto = productosObject.getInt("estado_producto");
                        int categoria = productosObject.getInt("categoria");
                        obj_productos = new dto_productos(id_producto, nom_producto, des_producto,stock,precio, unidad_medida, estado_producto, categoria);
                        listaProductos.add(obj_productos);
                        lista.add(listaProductos.get(i).getId_producto() + " ~ " + listaProductos.get(i).getNom_producto() + " ~ " + listaProductos.get(i).getDes_producto() + " ~ " + listaProductos.get(i).getStock() + " ~ " + listaProductos.get(i).getPrecio() + " ~ " + listaProductos.get(i).getUnidad_medida() + " ~ " + listaProductos.get(i).getEstado_producto() + " ~ " + listaProductos.get(i).getCategoria());

                        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,lista);
                        listViewProductos.setAdapter(adapter);

                        /*Log.i("Id Categoria", String.valueOf(obj_categorias.getId_categoria()));
                        Log.i("Nombre Categoria", obj_categorias.getNom_categoria());
                        Log.i("Estado Categoria", String.valueOf(obj_categorias.getEstado_categoria()));*/

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
}