package salazar.westerosatwar;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Salazar on 07-01-2017.
 */
public class SharedPrefLibrary {

    static String sharedPrefName="PrefFirstTimeUseFlag";
    public static void saveFlag(Context context,String newFlag) {
    //to check if app is being used for the first time or not

        SharedPreferences sp =context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("FirstTimeUseFlag",newFlag );

        editor.commit();
    }
    public static void saveNoOfKings(Context context,String newFlag) {
        SharedPreferences sp =context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("NoOfKings",newFlag );
        editor.commit();
    }
    public static String loadFlag(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String flag=sp.getString("FirstTimeUseFlag", "false");
        return flag;
    }

    public static String loadKingsNo(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String flag=sp.getString("NoOfKings", "0");
        return flag;
    }

}
