package com.gm3_developer.app_mysql_monroy;




import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class test_volley{
    public void baseRequest(final Context context){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/json1.php";
        StringRequest requesst= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept","application/json");
                map.put("id","1");

                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(requesst);
    }

    public void pruebaVolley(final Context context){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/json1.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.substring(0, 16));
                Toast.makeText(context, response.substring(0, 16), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Something went wrong!");
                Toast.makeText(context, "Sin conexión a internet", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void peticionGson(final Context context){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/json1.php";
        /*String url1 = Setting_VAR.URL_PRUEBA;*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "" + response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void recibirJson(final Context context){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/json1.php";
        StringRequest requesst = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuestaJSON = new JSONObject(response.toString());

                    String var1 = respuestaJSON.getString("nombre");
                    String var2 = respuestaJSON.getString("apellido");
                    Toast.makeText(context, "Respuesta: " + response.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Nombre: " + var1 + "\nApellido: " + var2, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(requesst);
    }


}

