package com.example.home.myapplication;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class LoginActivity extends AppCompatActivity {

    TextView signup_text;
    EditText Email, Pass;
    Button login_button;
    AlertDialog.Builder builder;



    private RequestQueue requestQueue;
    private StringRequest request;
    private static final String register_url = "http://mofo.16mb.com/login.php";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.login_email);
        Pass = (EditText) findViewById(R.id.login_password);
        login_button = (Button) findViewById(R.id.login_button);
        signup_text = (TextView) findViewById(R.id.sign_up);

        requestQueue = Volley.newRequestQueue(this);
        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                login_button.setEnabled(false);
                signup_text.setEnabled(false);

                final String email = Email.getText().toString();
                if (!validEmail(email)&&!email.equals("admin")) {
                    Email.setError("Invalid Email");
                    Email.requestFocus();
                }


                if (!validEmailLength(email)) {
                    Email.setError("email max length is 70 digit");
                    Email.requestFocus();
                }


                final String pass = Pass.getText().toString();
                if (!validPassword(pass)&&!pass.equals("admin")) {
                    Pass.setError("password length require 6-8 digit");
                    Pass.requestFocus();
                }


                if (Email.getText().toString().equals("") || Pass.getText().toString().equals("")) {
                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill up all fields");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


                request = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {


                    //info returned
                    @Override
                    public void onResponse(String response) {
                        Log.i("info(respond): ", response);

                        //this need to change
                        try {

                            Log.i("info: ", "Start logging JSON");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray srArray = jsonObject.getJSONArray("server_response");
                            JSONObject srObject = srArray.getJSONObject(0);
                            Log.i("info(server_respond): ",srObject.toString());
                            String code = srObject.getString("code");
                            String message = srObject.getString("message");

                            //check if login is true
                            if (code.equals("login_true")) {


                                JSONArray uArray = jsonObject.getJSONArray("user");
                                //make it into Json object
                                JSONObject uObject = uArray.getJSONObject(0);
                                Log.i("info(user): ", uObject.toString());

                                String username= uObject.getString("username");
                                String email= uObject.getString("email");

                                //User user = new User(username,email);
                                User.username = username;
                                User.email = email;

                                //get the Json array user_setting
                                JSONArray usArray = jsonObject.getJSONArray("user_setting");
                                //make it into Json object
                                JSONObject usObject = usArray.getJSONObject(0);

                                Log.i("info(user setting): ", usObject.toString());


                                //user setting part
                                int distance = usObject.getInt("distance");
                                String userSetting = usObject.getString("userSetting");

                                User.setDistance(distance);
                                User.setUserSetting(userSetting);



                                //get the Json array user_setting
                                JSONArray bArray = jsonObject.getJSONArray("bookmark");


                                //check if its empty
                                if (bArray != null && bArray.length() > 0) {
                                    Log.i("info(bookmark): ", bArray.toString());

                                    //make it into Json object
                                    JSONObject bObject = bArray.getJSONObject(0);

                                    for (int i = 0; i < bArray.length(); i++) {
                                        User.bookmark.add(bArray.get(i).toString());
                                    }


                                    for (int i = 0; i < bArray.length(); i++) {
                                        JSONObject jsonobj = bArray.getJSONObject(i);
                                        String id = jsonobj.getString("id");
                                        String name = jsonobj.getString("name");

                                        Log.i("info(id): ", id);
                                        Log.i("info(name): ", name);

                                        Bookmark.bookmarkArrayList.add(new Bookmark(id, name));
                                        
                                    }

                                    for(int i =0; i<Bookmark.bookmarkArrayList.size(); i++) {
                                        Log.i("infohelp: ", Bookmark.bookmarkArrayList.get(i).getName());
                                    }

                                    Log.i("info(userbookmark): ", User.getBookmark().toString());


                                } else {
                                    Log.i("info(problem): ","something is wrong");
                                }
                                login_button.setEnabled(true);
                                signup_text.setEnabled(true);

                                Toast.makeText(LoginActivity.this, "Welcome back, " + User.getUsername(), Toast.LENGTH_LONG).show();
                                User.setLogin(true);
                                System.out.println(User.getLogin());
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }
                            else if (code.equals("login_false"))
                            {
                                Toast.makeText(LoginActivity.this, message,Toast.LENGTH_LONG).show();
                                login_button.setEnabled(true);
                                signup_text.setEnabled(true);

                            } else {
                                Toast.makeText(LoginActivity.this, "Something is wrong", Toast.LENGTH_LONG).show();
                                login_button.setEnabled(true);
                                signup_text.setEnabled(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
                    // details push to login authentication
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("email",Email.getText().toString());
                        hashMap.put("password",Pass.getText().toString());
                        return hashMap;
                    }
                };

                if(( validEmail(email) && validEmailLength(email) || email.equals("admin") ) && ( validPassword(pass) || pass.equals("admin") )) {
                    requestQueue.add(request);
                }else{
                    login_button.setEnabled(true);
                    signup_text.setEnabled(true);
                }

            }
        });


        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    // validating email id
    private boolean validEmail(String validEmail) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(validEmail);
        return matcher.matches();
    }

    // validating email length
    private boolean validEmailLength(String email){
        if (email != null && email.length() <71) {
            return true;
        }
        return false;
    }


    // validating password with retype password
    private boolean validPassword(String pass) {
        if (pass != null && pass.length() >5 && pass.length() <9) {
            return true;
        }
        return false;
    }



}

