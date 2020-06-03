package ai.elimu.crowdsource;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ai.elimu.model.enums.Language;

public class MainActivity extends AppCompatActivity {

    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        languageSpinner = findViewById(R.id.language_spinner);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        ArrayAdapter<Language> arrayAdapter = new ArrayAdapter<Language>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                Language.values()
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(arrayAdapter);
    }
}
