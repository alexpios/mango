package by.kuchinsky.alexandr.mangofit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import by.kuchinsky.alexandr.mangofit.Common.Common;
import by.kuchinsky.alexandr.mangofit.Model.User;

public class SignUp extends AppCompatActivity {
    MaterialEditText edtPhone, edtName, edtPass;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtName=(MaterialEditText)findViewById(R.id.edtName);
        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);
        edtPass=(MaterialEditText)findViewById(R.id.edtPassword);

        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())){


                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Пожалуйста подождите...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Пользователь с таким номером уже существует!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                                mDialog.dismiss();
                            User user = new User(edtName.getText().toString(), edtPass.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Пользователь зарегестрирован!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else
                {
                    Toast.makeText(SignUp.this, "Проверьте Ваше интернет соединение!", Toast.LENGTH_SHORT).show();
                    return;
                }
        }


        });
    }
}
