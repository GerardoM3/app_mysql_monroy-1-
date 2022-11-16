package com.gm3_developer.app_mysql_monroy.ui.categorias;

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
import com.gm3_developer.app_mysql_monroy.dto_categorias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ConsultaCategoria extends Fragment {

    ListView listViewCategorias;
    ArrayAdapter adapter;

    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;

    public ConsultaCategoria() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_categoria, container, false);

        listViewCategorias = view.findViewById(R.id.listViewCategorias);

        allQueryCategorias(getContext());

        listViewCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                String informacion = "ID Categoria: " + listaCategorias.get(pos).getId_categoria();
                informacion += "Nombre de la categoría: " + listaCategorias.get(pos).getNom_categoria();
                informacion += "Estado de la categoría: " + listaCategorias.get(pos).getEstado_categoria();

                dto_categorias categorias = listaCategorias.get(pos);
                Intent i = new Intent(getContext(),AECategoria.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("categoria", categorias);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        return view;
    }


    public void allQueryCategorias(final Context context){
        listaCategorias = new ArrayList<dto_categorias>();
        lista = new ArrayList<String>();

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
                        lista.add(listaCategorias.get(i).getId_categoria() + " ~ " + listaCategorias.get(i).getNom_categoria() + " ~ " + listaCategorias.get(i).getEstado_categoria());

                        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,lista);
                        listViewCategorias.setAdapter(adapter);

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
}