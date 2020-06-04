package ai.elimu.crowdsource;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.model.enums.Language;

public class SelectLanguageActivity extends AppCompatActivity {

    private Spinner languageSpinner;

    private Button selectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_language);

        languageSpinner = findViewById(R.id.language_spinner);
        selectButton = findViewById(R.id.select_language_button);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Language language : Language.values()) {
            arrayAdapter.add(language.getEnglishName() + " (" + language.getNativeName() + ")");
        }
        languageSpinner.setAdapter(arrayAdapter);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(getClass().getName(), "onClick");

                Log.i(getClass().getName(), "languageSpinner.getSelectedItem(): " + languageSpinner.getSelectedItem());
                Log.i(getClass().getName(), "languageSpinner.getSelectedItemPosition(): " + languageSpinner.getSelectedItemPosition());
                Language language = Language.values()[languageSpinner.getSelectedItemPosition()];
                Log.i(getClass().getName(), "language: " + language);

                SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
                sharedPreferences.edit().putString("language", language.toString()).apply();

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
        });
    }
}
