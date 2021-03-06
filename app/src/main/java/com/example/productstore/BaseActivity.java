package com.example.productstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class BaseActivity extends AppCompatActivity {

    RadioGroup radioGroup1;
    RadioButton deals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        deals = (RadioButton)findViewById(R.id.store);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                Log.i("store", "store inside1 bro" + checkedId);
                switch (checkedId)
                {
                    case R.id.store:
                        Log.i("matching", "matching inside1 matching" +  checkedId);
                        in=new Intent(getBaseContext(),MainActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.add:
                        Log.i("matching", "matching inside1 watchlistAdapter" + checkedId);

                        in = new Intent(getBaseContext(), AddNewProductActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);

                        break;

                    case R.id.wishlist:
                        Log.i("matching", "matching inside1 rate" + checkedId);

                        in = new Intent(getBaseContext(),WishlistActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);

                        break;
                    default:
                        break;
                }
            }
        });
    }
}
