package salazar.westerosatwar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private List<King> kingsList=new ArrayList<King>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateKingsList();

    }
    private void populateKingsList() {
        //Log.d("MainActivity","InsidePopulate");

        SQLiteAsyncToGetKingCount sqLiteAsyncObj=new SQLiteAsyncToGetKingCount();
        sqLiteAsyncObj.execute();



    }

    private void populateKingListView() {
        ArrayAdapter<King> listViewAdapter=new KingListAdapter();
        ListView kingListView= (ListView)findViewById(R.id.kingListView);
        kingListView.setAdapter(listViewAdapter);
    }

    private void registerClickCallback() {
        ListView kingListView=(ListView)findViewById(R.id.kingListView);
        kingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                King clickedKing = kingsList.get(position);
                String kingName = clickedKing.getName();
                Intent intent = new Intent(getBaseContext(), KingsDetails.class);
                intent.putExtra("kingName", kingName);
                Toast.makeText(MainActivity.this, kingName+ "selected", Toast.LENGTH_SHORT).show();
                startActivity(intent);


            }
        });
    }
    private class KingListAdapter extends ArrayAdapter<King>{
        public KingListAdapter(){
            super(MainActivity.this, R.layout.king_item_view, kingsList);
            Log.d("kinsListCheckInAdapter", String.valueOf(kingsList.size()));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View kingItemView=convertView;
            //to make sure we have a view
            if (kingItemView==null){
                kingItemView=getLayoutInflater().inflate(R.layout.king_item_view,parent,false);
            }

            //find king
            King currentKing=kingsList.get(position);
            //fill view
            TextView kingID_textView=(TextView)kingItemView.findViewById(R.id.userName);
            kingID_textView.setText(currentKing.getName());

            TextView kingName_textView=(TextView)kingItemView.findViewById(R.id.rating);
            kingName_textView.setText(Double.toString(currentKing.getCurrentRating()));

            TextView kingCurrentRating_textView=(TextView)kingItemView.findViewById(R.id.king_totalBattles);
            kingCurrentRating_textView.setText(String.valueOf(currentKing.getTotalBattles()));



            return kingItemView;
        }


    }
    public class SQLiteAsyncToGetKingCount extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Void...v) {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            int count= db.getKingsCount();
            Log.d("kingCohnt",count+"");
            return count;
        }
        protected void onPostExecute(Integer kingCount) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            TextView kingID_textView=(TextView)findViewById(R.id.kingCount);
            kingID_textView.setText(String.valueOf(kingCount));
            SQLiteAsync sqLiteAsyncObj=new SQLiteAsync();
            sqLiteAsyncObj.execute();
        }

    }

    public class SQLiteAsync extends AsyncTask<Void, Void, List<King>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<King> doInBackground(Void...v) {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            kingsList = db.getAllKingsData();
            Log.d("kingsList in async","'"+String.valueOf(kingsList.get(0).getName()+"'"));
            return kingsList;
        }
        protected void onPostExecute(List<King> v) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            populateKingListView();
            registerClickCallback();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(MainActivity.this, DataLoaderActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
