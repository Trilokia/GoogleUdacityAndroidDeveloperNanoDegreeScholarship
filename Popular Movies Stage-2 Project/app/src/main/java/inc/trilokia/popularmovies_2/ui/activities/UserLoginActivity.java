package inc.trilokia.popularmovies_2.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.popularmovies_2.R;
import inc.trilokia.popularmovies_2.utils.PrefUtils;

public class UserLoginActivity extends AppCompatActivity {
    //Bound Views
    @BindView(R.id.continue_button)
    Button btn;
    @BindView(R.id.imdb_Key)
    EditText key;
    @BindView(R.id.guest)
    TextView guest;
    PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        prefUtils = new PrefUtils(this);
        if(!prefUtils.isFirstTimeLaunch()) {

            startActivity(new Intent(UserLoginActivity.this, MovieListActivity.class));
            Toast.makeText(UserLoginActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
            finish();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Check IMDB key entered by user and then assign it to API_KEY
                 **/
                if (key.getText() != null){

                    String _MY_KEY = key.getText().toString();
                    prefUtils.setApiKey(_MY_KEY);
                    prefUtils.setFirstTimeLaunch(false);
                    startActivity(new Intent(UserLoginActivity.this, MovieListActivity.class));
                   // Toast.makeText(UserLoginActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefUtils.setFirstTimeLaunch(false);
                startActivity(new Intent(UserLoginActivity.this, MovieListActivity.class));
               // Toast.makeText(UserLoginActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                //Toast.makeText(UserLoginActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
