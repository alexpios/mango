package by.kuchinsky.alexandr.mangofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import by.kuchinsky.alexandr.mangofit.Interface.ItemClickListener;
import by.kuchinsky.alexandr.mangofit.Model.Service;
import by.kuchinsky.alexandr.mangofit.ViewHolder.ServiceViewHolder;

public class ServiceList extends AppCompatActivity {
RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
FirebaseDatabase database;
DatabaseReference service_list;
String categoryId="";
FirebaseRecyclerAdapter<Service, ServiceViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        //Firebase

        database = FirebaseDatabase.getInstance();
        service_list= database.getReference("Service");
        //-------------------------------------------------///////

        recyclerView = (RecyclerView)findViewById(R.id.recycler_service);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //poluchaem intent, kotoriy polojili v Home
if (getIntent()!=null){
    categoryId = getIntent().getStringExtra("CategoryID");
    if (!categoryId.isEmpty() && categoryId != null){
        loadListService(categoryId);
    }
}


    }

    private void loadListService(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Service, ServiceViewHolder>(Service.class, R.layout.service_item, ServiceViewHolder.class,
                service_list.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(ServiceViewHolder viewHolder, Service model, int position) {
viewHolder.service_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.service_image);

                final Service local = model;
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Toast.makeText(ServiceList.this, " "+local.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        };

        //adapter set

        recyclerView.setAdapter(adapter);
    }
}
