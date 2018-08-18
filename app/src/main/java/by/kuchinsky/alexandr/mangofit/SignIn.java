package by.kuchinsky.alexandr.mangofit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import by.kuchinsky.alexandr.mangofit.Model.User;

public class SignIn extends AppCompatActivity {
EditText edtPhone, edtPass;
Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);
        edtPass=(MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn  = (Button)findViewById(R.id.btnSignIn);


        //work with BD

        final  FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");









        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Пожалуйста подождите...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check user in db(est' on tam ili net
                        //proverka na pustotu
                        if (edtPhone.getText().length()>=1){
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()){


                            //get user info
                            mDialog.dismiss();

                             User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);







                        if (user.getPassword().equals(edtPass.getText().toString())){
                            Toast.makeText(SignIn.this, "log in success, bro", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignIn.this, "Неверный пароль:(", Toast.LENGTH_SHORT).show();
                        }}

                        else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "Данный пользователь не найден.", Toast.LENGTH_SHORT).show();
                        }}
                        else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "Номер телефона не может быть пустым!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //neudacha
                    }
                });
            }
        });


    }
}
