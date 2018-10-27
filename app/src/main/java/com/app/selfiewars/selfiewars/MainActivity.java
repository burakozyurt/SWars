package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    ImageView ımageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        intendAlma();

    }

    public void tanimla() {
        ımageView = findViewById(R.id.selfiewarsLogo);
        textView = findViewById(R.id.selfiewarsText);
    }

    public void intendAlma(){
        Intent ıntent = new Intent(this,AuthenticationScreen.class);
        startActivity(ıntent);
    }
}
