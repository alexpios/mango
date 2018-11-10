package by.kuchinsky.alexandr.mangofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

//Search

    FirebaseRecyclerAdapter<Service, ServiceViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;



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
    if (!categoryId.isEmpty() && categoryId != null)
    {
        loadListService(categoryId);
    }

    //search

    materialSearchBar =(MaterialSearchBar)findViewById(R.id.searchBar);
    materialSearchBar.setHint("Название услуги");
   // materialSearchBar.setSpeechMode(false); ne nujno, v XML ukazali
    loadSuggest(); // method dlya vivoda podhodyawih iz FireBase
    materialSearchBar.setLastSuggestions(suggestList);
    materialSearchBar.setCardViewElevation(10);
    materialSearchBar.addTextChangeListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //vo vremya napisaniya teksta - menyaem spisok suggested services
            List <String> suggest = new ArrayList<String>();
            for (String search:suggestList)
            {
                if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    suggest.add(search);
            }
            materialSearchBar.setLastSuggestions(suggest);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });
    materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
        @Override
        public void onSearchStateChanged(boolean enabled) {
            //if surchbar OFF - vosstanavlivaem original Suggest Adapter

            if (!enabled)
                recyclerView.setAdapter(adapter);
        }

        @Override
        public void onSearchConfirmed(CharSequence text) {
            //pokazivaem results, if poisk vipolnen

            startSearch(text);

        }

        @Override
        public void onButtonClicked(int buttonCode) {

        }
    });

}


    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Service, ServiceViewHolder>(
                Service.class,
                R.layout.service_item,
                ServiceViewHolder.class,
                service_list.orderByChild("Name").equalTo(text.toString())

        ) {
            @Override
            protected void populateViewHolder(ServiceViewHolder viewHolder, Service model, int position) {


                viewHolder.service_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.service_image);

                final Service local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(ServiceList.this, " "+local.getName(), Toast.LENGTH_SHORT).show();
                        Intent serviceDetail = new Intent(ServiceList.this, ServiceDetail.class);
                        serviceDetail.putExtra("ServiceID", searchAdapter.getRef(position).getKey()); // send service id to new activuty
                        startActivity(serviceDetail);

                    }
                });


            }
        };
        recyclerView.setAdapter(searchAdapter);

    }

    private void loadSuggest() {

        service_list.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapchot:dataSnapshot.getChildren()){
                            Service item = postSnapchot.getValue(Service.class);
                            suggestList.add(item.getName()); //add name service v suggestList


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
                            //Toast.makeText(ServiceList.this, " "+local.getName(), Toast.LENGTH_SHORT).show();
                            Intent serviceDetail = new Intent(ServiceList.this, ServiceDetail.class);
                            serviceDetail.putExtra("ServiceID", adapter.getRef(position).getKey()); // send service id to new activuty
                            startActivity(serviceDetail);

                        }
                    });
            }
        };

        //adapter set

        recyclerView.setAdapter(adapter);
    }
}
