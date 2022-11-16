package com.gm3_developer.app_mysql_monroy.ui.categorias;

import android.os.Bundle;

import android.content.Context;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Categorias extends Fragment implements View.OnClickListener{
    private TextInputLayout ti_idcategoria, ti_namecategoria;
    private EditText et_idcategoria, et_namecategoria;
    private Spinner sp_estado;
    private Button btnSave, btnNew;

    String datoSelect = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categorias, container, false);
        ti_idcategoria = root.findViewById(R.id.ti_idcategoria);
        ti_namecategoria = root.findViewById(R.id.ti_namecategoria);
        et_idcategoria = root.findViewById(R.id.et_idcategoria);
        et_namecategoria = root.findViewById(R.id.et_namecategoria);
        sp_estado = root.findViewById(R.id.sp_estado);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.estadoCategorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapter);

        sp_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(sp_estado.getSelectedItemPosition()>0){
                    datoSelect = sp_estado.getSelectedItem().toString();
                }else{
                    datoSelect="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        btnSave = root.findViewById(R.id.btnSave);
        btnNew = root.findViewById(R.id.btnNew);
        btnSave.setOnClickListener(this);
        btnNew.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSave:
                String id = et_idcategoria.getText().toString();
                String nombre = et_namecategoria.getText().toString();
                if(id.length() == 0){
                    et_idcategoria.setError("Campo obligatorio");
                }else if(nombre.length() == 0){
                    et_namecategoria.setError("Campo obligatorio");
                }else if(sp_estado.getSelectedItemPosition()>0){
                    Toast.makeText(getContext(), "Bien...", Toast.LENGTH_SHORT).show();
                    save_server(getContext(), Integer.parseInt(id), nombre, Integer.parseInt(datoSelect));
                }else{
                    Toast.makeText(getContext(), "Debes seleccionar un estado para la categoria", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnNew:
                new_categories();
                break;

            default:
        }
    }

    private void save_server(final Context context, final int id_categoria, final String nom_categoria, final int estado_categoria){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/guardar.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,  new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                JSONObject requestJSON = null;
                try{
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    }else if(estado.equals("2")){
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Toast.makeText(context, "No se puede guardar. \nInténtelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type","application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", String.valueOf(id_categoria));
                map.put("nombre", nom_categoria);
                map.put("estado", String.valueOf(estado_categoria));
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void new_categories(){
        et_idcategoria.setText(null);
        et_namecategoria.setText(null);
        sp_estado.setSelection(0);
    }
}