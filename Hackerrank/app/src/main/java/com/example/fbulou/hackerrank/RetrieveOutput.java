package com.example.fbulou.hackerrank;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackerrank.api.client.ApiException;
import com.hackerrank.api.hackerrank.api.CheckerApi;
import com.hackerrank.api.hackerrank.model.Submission;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveOutput extends AsyncTask<String, Void, String> {

    TextView output = MainActivity.getInstance().output;
    ProgressBar progressBar = MainActivity.getInstance().progressBar;

    String API_KEY = "hackerrank|356927-669|598df6659f448ea81c2da5c74ff11902f3ab8cb3";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        output.setText("Running...");
    }

    @Override
    protected String doInBackground(String... params) {
        String code = params[0];
        /*String link = "http://api.hackerrank.com/checker/submission.json";

        String urlParameters = "source=print 1&lang=5&testcases=[\"1\"]&api_key=" + API_KEY;
*/

        try {
            String url = "http://api.hackerrank.com/checker/submission.json";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "source="+code+"&lang=5&testcases=[\"1\"]&api_key="+API_KEY;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                httpURLConnection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return null;
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (response == null)
            response = "THERE WAS AN ERROR";

        progressBar.setVisibility(View.GONE);
        output.setText("OUTPUT : \n " + response);

    }
}
