package salazar.westerosatwar;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;


public class KingsDetails extends ActionBarActivity {
    String kingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kings_details);
        Bundle bundle= getIntent().getExtras();
        kingName =bundle.getString("kingName");
        Log.d("GetKingonCreate: ", kingName);
        SQLiteAsync asyncObj=new SQLiteAsync();
        asyncObj.execute(kingName);
    }

    public class SQLiteAsync extends AsyncTask<String, Void, King> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(KingsDetails.this);
            pDialog.setMessage("Loading data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected King doInBackground(String...v) {
            String kingName=v[0];
            Log.d("GetKingdoInBackground: ", String.valueOf(kingName.length()));
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            King currentKing=db.getKing(kingName);

            return currentKing;
        }
        protected void onPostExecute(King k) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            TextView kingID_textView=(TextView)findViewById(R.id.kingNameTV);
            kingID_textView.setText(String.valueOf(k.getName()));
            TextView kingID_textView1=(TextView)findViewById(R.id.battleLostTV);
            kingID_textView1.setText("Battles Lost: "+k.getBattlesLost());
            TextView kingID_textView2=(TextView)findViewById(R.id.BattleWonTV);
            kingID_textView2.setText("Battles Lost: "+String.valueOf(k.getBattlesWon()));
            TextView kingID_textView3=(TextView)findViewById(R.id.tBattTV);
            kingID_textView3.setText("Total Battles: "+String.valueOf(k.getTotalBattles()));
            TextView kingID_textView4=(TextView)findViewById(R.id.battleTypeTV);
            kingID_textView4.setText("Battle Type: "+String.valueOf(k.getMainBattleType()));
            TextView kingID_textView5=(TextView)findViewById(R.id.ratingTV);
            kingID_textView5.setText("Current Ratings: "+String.valueOf(k.getCurrentRating()));


        }

    }



}
