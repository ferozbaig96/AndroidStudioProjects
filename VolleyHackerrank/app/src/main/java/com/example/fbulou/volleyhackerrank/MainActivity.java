package com.example.fbulou.volleyhackerrank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView output;
    EditText mEdittext;
    ProgressBar progressBar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.id_output);
        mEdittext = (EditText) findViewById(R.id.id_code);
        progressBar = (ProgressBar) findViewById(R.id.id_progressbar);
    }


    public void submit(View view) {
        final String code = mEdittext.getText().toString().trim();

        if (code.length() != 0) {
            progressBar.setVisibility(View.VISIBLE);
            output.setText("");

            final String API_KEY = "hackerrank|356927-677|1071ad4cc0a104808b76e13ae17b4b9eda7f698f";
            String url = "http://api.hackerrank.com/checker/submission.json";

            requestQueue = Volley.newRequestQueue(this);

            /*Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("source", code);
            params.put("lang", "5");
            params.put("testcases", "[\"1\"]");
            params.put("api_key", API_KEY);
            params.put("wait", "true");
            params.put("format", "json");*/

        /*    JsonObjectRequest zzz=new JsonObjectRequest()

            JsonObjectRequest jor = new JsonObjectRequest(url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.GONE);
                            output.setText(response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json;charset=utf-8");
                    headers.put("User-agent", "My useragent");
                    return headers;

                }
            };

            requestQueue.add(jor);
*/

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response).getJSONObject("result");

                                String message = jsonObject.getString("message"),
                                        stderr = jsonObject.getString("stderr"),
                                        stdout = jsonObject.getString("stdout"),
                                        time = jsonObject.getString("time");

                                stdout=stdout.replaceAll("[\\\\]n","\n");
                                stdout=stdout.substring(2,stdout.length()-2);

                                message=message.substring(2,message.length()-2);
                                stderr=stderr.substring(1,stderr.length()-1);
                                time=time.substring(1,time.length()-1);

                                String data = " Result : " + message + " \n stdout : \n " + stdout + " \n stderr : " + stderr + " \n Time : " + time;
                                progressBar.setVisibility(View.GONE);
                                output.setText(data);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    params.put("source", code);
                    params.put("lang", "5");
                    params.put("testcases", "[\"1\"]");
                    params.put("api_key", API_KEY);
                    params.put("wait", "true");
                    params.put("format", "json");
                    return params;
                }
            };

            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);


        } else
            Toast.makeText(this, "Enter the code", Toast.LENGTH_SHORT).show();
    }
}
