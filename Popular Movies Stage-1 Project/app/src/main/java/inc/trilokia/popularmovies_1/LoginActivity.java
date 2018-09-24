package inc.trilokia.popularmovies_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //Bound Views
    @BindView(R.id.continue_button)
    Button btn;
    @BindView(R.id.imdb_Key)
    EditText key;

    //API keys available from: https://www.themoviedb.org/account/signup
    public static String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Check IMDB key entered by user and then assign it to API_KEY
                 **/
                //TODO:(For Stage 2) Implement remember me and guest mode
                if (key.getText() != null){
                    API_KEY = key.getText().toString();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                finish();
                }
            }
        });
    }
}
