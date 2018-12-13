package by.kuchinsky.alexandr.mangofit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.rey.material.widget.CheckBox;

import by.kuchinsky.alexandr.mangofit.Common.Common;
import by.kuchinsky.alexandr.mangofit.Model.User;
import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
EditText edtPhone, edtPass;
Button btnSignIn;
    SharedPreferences sp;
    CheckBox ckbRemember;

    final String s_log = "lgn";
    final String s_pass = "pss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);
        edtPass=(MaterialEditText)findViewById(R.id.edtPassword);
        ckbRemember = (CheckBox)findViewById(R.id.ckbRemember);
        btnSignIn  = (Button)findViewById(R.id.btnSignIn);

        Paper.init(this);

        //shared pref

        loadText();



        //work with BD

        final  FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");









        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    //save pass and username
                    if (ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, edtPass.getText().toString());

                    }


                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Пожалуйста подождите...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check user in db(est' on tam ili net
                            //proverka na pustotu
                            if (edtPhone.getText().length() >= 1) {
                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {


                                    //get user info
                                    mDialog.dismiss();

                                    User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                    user.setPhone(edtPhone.getText().toString());  //settim nomer telefona dlya usera


                                    if (user.getPassword().equals(edtPass.getText().toString())) {
                                        Intent home = new Intent(SignIn.this, Home.class);
                                        Common.currentUser = user;
                                        startActivity(home);
                                        finish();

                                    } else {
                                        Toast.makeText(SignIn.this, "Неверный пароль:(", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignIn.this, "Данный пользователь не найден.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
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
                else{
                    Toast.makeText(SignIn.this, "Проверьте Ваше интернет соединение!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });


    }

    private void loadText() {
        sp= getSharedPreferences("MyTextFromEditText", MODE_PRIVATE);
        String saved_text = sp.getString(s_log, "");
        edtPhone.setText(saved_text);
      //  Toast.makeText(this, "Text loaded.", Toast.LENGTH_SHORT).show();


        SharedPreferences sp2= getSharedPreferences("MyTextFromEditText2", MODE_PRIVATE);
        String saved_text2 = sp2.getString(s_pass, "");
        edtPass.setText(saved_text2);

    }

    private void saveText() {

        sp = getSharedPreferences("MyTextFromEditText", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString(s_log, edtPhone.getText().toString());
        et.apply();


       SharedPreferences sp2 = getSharedPreferences("MyTextFromEditText2", MODE_PRIVATE);
        SharedPreferences.Editor et2 = sp2.edit();
        et2.putString(s_pass, edtPass.getText().toString());
        et2.apply();

      //  Toast.makeText(this, "Data saved ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }
}
