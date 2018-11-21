package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthUsersInfoActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRefUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_users_info);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRefUser = database.getReference("Users/" + mAuth.getUid());
        //Yapılacaklar
        /*
            1.Kullanıcı bilgileri alınacak eksiksiz bir şekilde tamamlanıp veri tabanına gönderilecek
            2.MainActivity e geçerken token,wildcards ilk düğümü oluşturlacak ve accountstate düğümü true edilecek
         */
    }
}
