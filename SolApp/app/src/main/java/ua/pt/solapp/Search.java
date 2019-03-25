package ua.pt.solapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Search.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new CityRecycler()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
