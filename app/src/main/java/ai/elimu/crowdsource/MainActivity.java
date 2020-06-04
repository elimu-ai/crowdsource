package ai.elimu.crowdsource;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.crowdsource.language.SelectLanguageActivity;
import ai.elimu.model.enums.Language;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String languageAsString = sharedPreferences.getString("language", null);
        Log.i(getClass().getName(), "languageAsString: " + languageAsString);
        if (TextUtils.isEmpty(languageAsString)) {
            // Redirect to language selection

            Intent selectLanguageIntent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
            startActivity(selectLanguageIntent);
            finish();
        } else {
            // Redirect to REST API synchronization

            Language language = Language.valueOf(languageAsString);
            Log.i(getClass().getName(), "language: " + language);

            // TODO
        }
    }
}
