package com.gm3_developer.app_mysql_monroy.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gm3_developer.app_mysql_monroy.MainActivity;
import com.gm3_developer.app_mysql_monroy.MySingleton;
import com.gm3_developer.app_mysql_monroy.R;
import com.gm3_developer.app_mysql_monroy.dto_usuarios;

import android.content.Context;
import android.content.Intent;
import android.text.Html;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    TextView tv_register;
    EditText et_loginUser, et_loginPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_loginUser = findViewById(R.id.et_loginUser);
        et_loginPass = findViewById(R.id.et_loginPass);
        tv_register = findViewById(R.id.tv_register);
        tv_register.setText(Html.fromHtml("<u>Regístrate</u>"));

        btnLogin = findViewById(R.id.btnLogin);


    }

    public void LogIn(View v){
        String loginUser = et_loginUser.getText().toString();
        String loginPass = et_loginPass.getText().toString();

        if(loginUser.length() == 0){
            et_loginUser.setError("Campo obligatorio");
        }else if(loginPass.length() == 0){
            et_loginPass.setError("Campo obligatorio");
        }else{
            LogOn(login.this, loginUser, loginPass);
        }
    }

    public void LogOn(final Context context, final String usuario, final String clave){
        String url = "https://gerardomonroysis11a.000webhostapp.com/WS/service2020/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if (estado.equals("1")) {
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(login.this, MainActivity.class);
                        startActivity(i);
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

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json;charset=utf-8");
                map.put("usuario", usuario);
                map.put("clave", clave);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void register_go(View v){
        Intent i = new Intent(this, register.class);
        startActivity(i);
    }
}