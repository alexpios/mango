package by.kuchinsky.alexandr.mangofit;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import by.kuchinsky.alexandr.mangofit.Common.Common;
import by.kuchinsky.alexandr.mangofit.Database.Database;
import by.kuchinsky.alexandr.mangofit.Model.Order;
import by.kuchinsky.alexandr.mangofit.Model.Service;

public class ServiceDetail extends AppCompatActivity {
TextView service_name, service_price, service_description;
ImageView serviceImage;
CollapsingToolbarLayout collapsingToolbarLayout;
//FloatingActionButton btnCart;
ElegantNumberButton numberButton;
String serviceId="";
FirebaseDatabase database;
DatabaseReference services;
    Service currentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);


        //FireBase

        database = FirebaseDatabase.getInstance();
        services = database.getReference("Service");


        //init view

        numberButton=(ElegantNumberButton)findViewById(R.id.numberButton);
     Button btnCart = (Button) findViewById(R.id.btnBron);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        serviceId,
                        currentService.getName(), numberButton.getNumber(),
                        currentService.getPrice(), currentService.getDiscount()

                ));

                Toast.makeText(ServiceDetail.this, "Добавлено в корзину", Toast.LENGTH_SHORT).show();
            }
        });

        service_description=(TextView)findViewById(R.id.service_description);
        service_name=(TextView)findViewById(R.id.service_name);
        service_price=(TextView)findViewById(R.id.service_price);
        serviceImage=(ImageView)findViewById(R.id.img_service);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollpsedAppbar);

        //get service id from intent ServiceList

        if (getIntent() != null){
            serviceId = getIntent().getStringExtra("ServiceID");
            if (!serviceId.isEmpty()){
                if (Common.isConnectedToInternet(getBaseContext())){
                getDetailService(serviceId);}
                else{
                    Toast.makeText(ServiceDetail.this, "Проверьте Ваше интернет соединение!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }







    }

    private void getDetailService(String serviceId) {
        services.child(serviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentService = dataSnapshot.getValue(Service.class);

                //set image
                Picasso.with(getBaseContext()).load(currentService.getImage()).into(serviceImage);
                collapsingToolbarLayout.setTitle(currentService.getName());

                service_price.setText(currentService.getPrice());

                service_name.setText(currentService.getName());
                service_description.setText(currentService.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
