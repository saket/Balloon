package is.uncommon.layoutinflationtalk;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import is.uncommon.layoutinflationtalk.library.BalloonContextWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(BalloonContextWrapper.wrap(newBase));
    }

}
