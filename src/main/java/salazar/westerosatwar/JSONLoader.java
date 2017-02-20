package salazar.westerosatwar;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Salazar on 07-01-2017.
 */
public class JSONLoader {
    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONArray jArray = null;

    public JSONArray makeGetRequest(String url){
        try {
            urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setConnectTimeout(15000);
            conn.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            //Log.d("JSON Loader", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        try {
            jArray = new JSONArray(result.toString());

        } catch (JSONException e) {
            Log.e("JSON Loader", "Error parsing data " + e.toString());
        }
        return jArray;

    }


}
