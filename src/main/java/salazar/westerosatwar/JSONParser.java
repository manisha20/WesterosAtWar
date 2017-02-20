package salazar.westerosatwar;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Salazar on 07-01-2017.
 */
public class JSONParser {
    //class to parse JSON data and convert it into list of King objects
    Map<String, King> hashMap = new HashMap<String,King>();

    public List<King> getKingsList(JSONArray jsonArray){
        List<King> kingsList=new ArrayList<King>();

        try{
        for(int i=0; i < jsonArray.length(); i++) {
            King attackerKingObj=null;
            King defenderKingObj=null;
            double s_attacker=0,s_defender=0;
            JSONObject jObject = jsonArray.getJSONObject(i);

            String attacker_king_name = jObject.getString("attacker_king");
            String defender_king_name=jObject.getString("defender_king");
            if(!checkStringIfEmpty(attacker_king_name)&&!checkStringIfEmpty(defender_king_name)){
                if(hashMap.containsKey(attacker_king_name)){attackerKingObj=hashMap.get(attacker_king_name);}
                else{attackerKingObj=new King();
                    attackerKingObj.setName(attacker_king_name);
                    attackerKingObj.battleType=new ArrayList<String>();
                    hashMap.put(attacker_king_name,attackerKingObj);
                }
                if(hashMap.containsKey(defender_king_name)){defenderKingObj=hashMap.get(defender_king_name);}
                else{defenderKingObj=new King();
                    defenderKingObj.setName(defender_king_name);
                    defenderKingObj.battleType=new ArrayList<String>();
                    hashMap.put(defender_king_name,defenderKingObj);
                }
                defenderKingObj.setTotalBattles(defenderKingObj.getTotalBattles()+1);
                attackerKingObj.setTotalBattles(attackerKingObj.getTotalBattles()+1);

                String attacker_outcome=jObject.getString("attacker_outcome");
                switch (attacker_outcome){
                    case "win":
                        attackerKingObj.setBattlesWon(attackerKingObj.battlesWon + 1);
                        defenderKingObj.setBattlesLost(defenderKingObj.battlesLost + 1);
                        s_attacker=1;s_defender=0;
                        break;
                    case "loss":
                        defenderKingObj.setBattlesWon(defenderKingObj.battlesWon + 1);
                        attackerKingObj.setBattlesLost(attackerKingObj.battlesLost+1);
                        s_attacker=0;s_defender=1;
                        break;
                    case "draw":
                        s_attacker=0.5;s_defender=0.5;
                }
                //recalculating rating
                double[] result=reassignScores(attackerKingObj.getCurrentRating(),defenderKingObj.currentRating,s_attacker,s_defender);
                attackerKingObj.setCurrentRating(result[0]);
                defenderKingObj.setCurrentRating(result[1]);
                attackerKingObj.getBattleType().add(jObject.getString("battle_type"));
                defenderKingObj.getBattleType().add(jObject.getString("battle_type"));
                //Log.d("Object Created", attackerKingObj.getName() + "" + attackerKingObj.getBattlesLost());
                //Log.d("Object Created", defenderKingObj.getName() + "" + defenderKingObj.getBattlesLost());
            }



        }}catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        Log.d("All Objects Created",hashMap.keySet().toString());
        //set the total number of kings here!

        for (String key: hashMap.keySet()) {

            ArrayList<String>list=(hashMap.get(key)).getBattleType();
            if(list.size()>1){
                List<String> subList = list.subList(0, list.size());
                Collections.sort(subList);
                //Log.d("subList", subList.toString());
                String currentMax=subList.get(0);
                int maxCount = 0;
                String current=subList.get(0);
                int count = 0;
                for(int i = 0; i < subList.size(); i++) {
                    String item = subList.get(i);
                    if(item.equals(current) && i<subList.size()) {
                        count++;
                    }
                    else {
                        if(count > maxCount) {
                            maxCount = count;
                            currentMax = current;
                            //System.out.println(currentMax);
                        }
                        count = 1;
                        current = item;
                    }
                }
                if(count > maxCount) {
                    currentMax = current;}
                //Log.d("currentMax",currentMax);
                hashMap.get(key).setMainBattleType(currentMax);
            }
            else hashMap.get(key).setMainBattleType(hashMap.get(key).getBattleType().get(0));
            kingsList.add(hashMap.get(key));
        }
        return kingsList;
    }

    private double[] reassignScores(double currentRatingAttacker, double currentRatingDefender, double s_attacker, double s_defender) {
        double[] result=new double[2];
        double R1=Math.pow(10.0,(currentRatingAttacker/400));
        double R2=Math.pow(10.0, (currentRatingDefender / 400));
        double E1=R1/(R1+R2);
        double E2=R2/(R1+R2);
        double r1new=currentRatingAttacker+32*(s_attacker-E1);
        double r2new=currentRatingDefender+32*(s_defender-E2);
        result[0]=r1new;
        result[1]=r2new;
        //Log.d("Score Cal: ",r1new+" "+r2new);
        return result;

    }

    boolean checkStringIfEmpty(String temp){
        if(temp.equals(null)||temp == "null" || temp.isEmpty())return true;
        else return false;
    }
}
