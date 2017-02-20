package salazar.westerosatwar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;


public class DataLoaderActivity extends ActionBarActivity {
    String url = "http://starlord.hackerearth.com/gotjson";
//this class will call all functions to load data from json to db
    /* 1. call json loader to get json data
    2. call json parser to parse json data extracted
    3. insert data into db
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_loader);
        getJSONAsync getJSONAsyncObj = new getJSONAsync();
        getJSONAsyncObj.execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_loader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class getJSONAsync extends AsyncTask<String, Void, JSONArray> {
        JSONLoader jsonLoaderObj = new JSONLoader();
        private ProgressDialog pDialog;

        //private static final String JSON_URL = "http://starlord.hackerearth.com/gotjson";
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(DataLoaderActivity.this);
            pDialog.setMessage("Attempting connection...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONArray jsonData = null;
            String JSON_URL = args[0];
            try {
                Log.d("JSONLoaderAsync", "starting");
                jsonData = jsonLoaderObj.makeGetRequest(JSON_URL);
                if (jsonData != null) {
                    Log.d("JSONLoaderAsync", jsonData.length() + "");
                    return jsonData;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        protected void onPostExecute(JSONArray jsonData) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (jsonData != null) {
                Toast.makeText(DataLoaderActivity.this, "Data downloaded",
                        Toast.LENGTH_SHORT).show();
                //Log.d("JSONLoaderAsync", jsonData.toString());
                JSONParserAsync jsonParserAsyncObj = new JSONParserAsync();
                jsonParserAsyncObj.execute(jsonData);
            }
        }

    }

    public class JSONParserAsync extends AsyncTask<JSONArray, Void, List<King>> {
        JSONParser jsonParserObj = new JSONParser();
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(DataLoaderActivity.this);
            pDialog.setMessage("Parsing data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<King> doInBackground(JSONArray... jsonArrays) {
            JSONArray jsonData = jsonArrays[0];
            List<King> kingsList = null;
            try {
                Log.d("JSONParserAsync", "starting");
                kingsList = jsonParserObj.getKingsList(jsonData);
                if (kingsList != null) {
                    //Log.d("JSONParserAsync", kingsList.toString());
                    return kingsList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return kingsList;
        }

        protected void onPostExecute(List<King> kingsList) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (kingsList != null) {
                Toast.makeText(DataLoaderActivity.this, "Data parsed!",
                        Toast.LENGTH_SHORT).show();
                //Log.d("JSONParserAsync", kingsList.toString());
            }
            //sqlite insertion here!
            SQLiteAsync sqLiteAsyncObj=new SQLiteAsync();
            sqLiteAsyncObj.execute(kingsList);
        }

    }

    public class SQLiteAsync extends AsyncTask<List<King>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<King>... lists) {

            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            List<King>kingsList=lists[0];
            for(int i=0;i<kingsList.size();i++){
                db.addKing(kingsList.get(i));
            }
            SharedPrefLibrary.saveFlag(getApplicationContext(), "true");

            return null;
        }
        protected void onPostExecute(Boolean result) {
            Intent intent = new Intent(DataLoaderActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}