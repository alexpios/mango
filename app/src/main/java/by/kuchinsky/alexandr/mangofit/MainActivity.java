package by.kuchinsky.alexandr.mangofit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import by.kuchinsky.alexandr.mangofit.Common.Common;
import by.kuchinsky.alexandr.mangofit.Model.User;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
Button btnSingUp, btnSignIn;
TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn=(Button)findViewById(R.id.btnLogin);
        btnSingUp=(Button)findViewById(R.id.btnSignUp);
        slogan=(TextView)findViewById(R.id.txtSlogan);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/appetiteNew.ttf");
        slogan.setTypeface(face);

        //init paper
        Paper.init(this);




        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin = new Intent(MainActivity.this, SignIn.class);
                startActivity(signin);
            }
        });
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(MainActivity.this, SignUp.class);
                startActivity(signup);
            }
        });

        //check remember

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if (user != null && pwd !=null){
            if (!user.isEmpty() && !pwd.isEmpty()){
                login(user, pwd);
            }
        }
    }

    private void login(final String phone, final String pwd) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())) {



            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Пожалуйста подождите...");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //check user in db(est' on tam ili net
                    //proverka na pustotu

                        if (dataSnapshot.child(phone).exists()) {


                            //get user info
                            mDialog.dismiss();

                            User user = dataSnapshot.child(phone).getValue(User.class);
                            user.setPhone(phone);


                            if (user.getPassword().equals(pwd)) {
                                Intent home = new Intent(MainActivity.this, Home.class);
                                Common.currentUser = user;
                                startActivity(home);
                                finish();

                            } else {
                                Toast.makeText(MainActivity.this, "Неверный пароль:(", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Данный пользователь не найден.", Toast.LENGTH_SHORT).show();
                        }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //neudacha
                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "Проверьте Ваше интернет соединение!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    }

