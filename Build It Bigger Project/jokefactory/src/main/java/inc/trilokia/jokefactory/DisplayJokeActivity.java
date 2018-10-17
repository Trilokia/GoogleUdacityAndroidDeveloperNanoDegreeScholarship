package inc.trilokia.jokefactory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayJokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);
        TextView textview = findViewById(R.id.joke_text);

//Retrieve the joke from the Intent Extras
        String JokeResult = null;
//the Intent that started us
        Intent intent = getIntent();
        JokeResult = intent.getStringExtra(getString(R.string.app_name));

        if (JokeResult != null) {
            textview.setText(JokeResult);
        } else {
            textview.setText(getString(R.string.load));
        }
    }

}
