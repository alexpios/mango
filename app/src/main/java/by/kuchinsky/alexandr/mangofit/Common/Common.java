package by.kuchinsky.alexandr.mangofit.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import by.kuchinsky.alexandr.mangofit.Model.User;

public class Common {
    public static User currentUser;
    public static String ConvertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Ожидает проверки";
        }
        else if (status.equals("1")){
            return "Проверяется колличество мест";
        }
        else
            return "Место забронировано";

    }


    public static final String DELETE = "Удалить";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password0";

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info !=null){
                for (int i = 0 ; i <info.length; i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }

        }return false;
    }

}
