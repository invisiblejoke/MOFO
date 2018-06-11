package com.example.home.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText Name, Email, Pass, ConPass;
    Button reg_Button, regCancel_Button;


    private RequestQueue requestQueue;
    private static final String register_url = "http://mofo.16mb.com/register.php";
    private StringRequest request;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = (EditText) findViewById(R.id.reg_name);
        Email = (EditText) findViewById(R.id.login_email);
        Pass = (EditText) findViewById(R.id.login_password);
        ConPass = (EditText) findViewById(R.id.reg_con_password);
        reg_Button = (Button) findViewById(R.id.reg_button);
        regCancel_Button = (Button) findViewById(R.id.regCancel_button);

        requestQueue = Volley.newRequestQueue(this);

        reg_Button.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                Log.i("infohelp: ", "12");

                final String email = Email.getText().toString();
                final String pass = Pass.getText().toString();
                final String conPass = ConPass.getText().toString();
                final String name = Name.getText().toString();


               //check email
               if (!validEmail(email)) {
                    Email.setError("Invalid Email");
                    Email.requestFocus();
                    Log.i("infohelp: ", "13");
                }

               if (!validEmailLength(email)) {
                    Email.setError("email max length is 70 digit");
                    Email.requestFocus();
                }


                if (!validName(name)) {
                    Name.setError("name length require 6-15 digit ");
                    Name.requestFocus();
                }

                //check password
                if (!validPassword(pass)) {
                    Pass.setError("password length require 6-8 digit");
                    Pass.requestFocus();
                }

                //check 2 password identical
                if(!conPass.equals(pass)) {
                    ConPass.setError("password mismatch");
                    ConPass.requestFocus();
                }



                //check if there is any empty field
                if (email.equals("") ||pass.equals("")|| conPass.equals("")|| name.equals("")) {
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill up all fields");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Log.i("infohelp: ", "13");
                        }

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


            request = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("infohelp: ", "14");
                    Log.i("infohelp: ", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                        JSONObject JO = jsonArray.getJSONObject(0);
                        Log.i("infohelp: ", "13");
                        Log.i("json info: ", jsonObject.toString());

                        String code = JO.getString("code");
                        System.out.println(code);
                        String message = JO.getString("message");
                        Log.i("infohelp: ", "14");
                        if (code.equals("reg_true")) {
                            Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_LONG).show();


                            //go back to Login
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            finish();
                            startActivity(i);


                            Log.i("USERLOGINFO", "run");
                        } else if (code.equals("reg_false")) {
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(RegisterActivity.this, "Something is wrong", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }

            })
            {
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("username",name);
                    hashMap.put("email",email);
                    hashMap.put("password",pass);
                    return hashMap;
                }
            };

                if(validPassword(pass) && conPass.equals(pass) && validEmail(email) && validEmailLength(email) && validName(name)) {
                    requestQueue.add(request);
                }

            }
        });
            regCancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                 }
            });
        }

    // validating email id
    private boolean validEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    // validate email length
    private boolean validEmailLength(String email){
        if (email != null && email.length() <71) {
            return true;
        }
        return false;
    }

    //validate name length
    private boolean validName(String name){
        if (name != null && name.length() >5 && name.length() <16) {
            return true;
        }
        return false;
    }

    // validating password with retype password
    private boolean validPassword(String pass) {
        if (pass != null && pass.length() > 5 && pass.length() <9) {
            return true;
        }
        return false;
    }


}
