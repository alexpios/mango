package by.kuchinsky.alexandr.mangofit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import by.kuchinsky.alexandr.mangofit.Common.Common;
import by.kuchinsky.alexandr.mangofit.Database.Database;
import by.kuchinsky.alexandr.mangofit.Model.Order;
import by.kuchinsky.alexandr.mangofit.Model.Request;
import by.kuchinsky.alexandr.mangofit.ViewHolder.CartAdapter;
import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

TextView txtTotalPrice;
FButton btnPlace;

List<Order> cart = new ArrayList<>();
CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase

        database =FirebaseDatabase.getInstance();
        requests= database.getReference("Requests");

        //INIT

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (FButton)findViewById(R.id.btnPlaceOrder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialog();
            }
        });
        loadListService();

    }

    private void ShowAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Последний шаг!");
        alertDialog.setMessage("Введите желаемую дату посещения: ");
        final EditText edtDate = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtDate.setLayoutParams(lp);
        alertDialog.setView(edtDate);
        alertDialog.setIcon(R.drawable.cart_shop);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(), edtDate.getText().toString(),
                        txtTotalPrice.getText().toString(), cart

                );

                //submit to firebase man. Usaem System.CurrentMilli to key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                //Delete Cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Благодарим Вас! Информация о брони отправлена менеджеру.", Toast.LENGTH_SHORT).show();

                finish();





            }
        });

alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
   dialog.dismiss();
    }
});
alertDialog.show();
    }

    private void loadListService() {
    cart = new Database(this).getCarts();
    adapter = new CartAdapter(cart, this);
    recyclerView.setAdapter(adapter);


    //calculate obwuu stoimost

        int total = 0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQantity()));
//        Locale locale = new Locale("ru", "BY");
//        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
    //    txtTotalPrice.setText(fmt.format(total));
        txtTotalPrice.setText(total + " б.р.");

    }
}
