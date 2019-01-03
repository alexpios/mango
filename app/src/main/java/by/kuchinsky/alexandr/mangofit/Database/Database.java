package by.kuchinsky.alexandr.mangofit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import by.kuchinsky.alexandr.mangofit.Model.Order;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "mangofitDB";   //.db hz nado ili net, poka ubru, no vrode nado
    private static final int DB_VER = 1;


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId", "ProductName", "Qantity"
        , "Price", "Discount"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        final List<Order> result= new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                c.getString(c.getColumnIndex("ProductName")),
                c.getString(c.getColumnIndex("Qantity")),
                c.getString(c.getColumnIndex("Price")),
                c.getString(c.getColumnIndex("Discount"))

                        ));
            } while (c.moveToNext());

        }
return  result;
    }

    public void addToCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Qantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQantity(),
                order.getPrice(),
                order.getDiscount());

        db.execSQL(query);


    }

    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");

        db.execSQL(query);


    }

    public void addToFav(String serviceId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(ServiceId) VALUES('%s');", serviceId);
        db.execSQL(query);

    }

    public void removeFromFav(String serviceId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE ServiceId='%s';", serviceId);
        db.execSQL(query);

    }
    public boolean isFav(String serviceId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE ServiceId='%s';", serviceId);
       Cursor cursor = db.rawQuery(query,null);
       if (cursor.getCount() <= 0){
           cursor.close();
           return false;
       }
       cursor.close();
       return true;

    }

}
