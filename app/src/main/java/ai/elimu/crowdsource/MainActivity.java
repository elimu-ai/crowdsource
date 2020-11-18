package ai.elimu.crowdsource;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.crowdsource.authentication.SignInWithGoogleActivity;
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

        // Check if language has been selected
        String languageAsString = sharedPreferences.getString("language", null);
        Log.i(getClass().getName(), "languageAsString: " + languageAsString);
        if (TextUtils.isEmpty(languageAsString)) {
            // Redirect to language selection
            Intent selectLanguageIntent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
            startActivity(selectLanguageIntent);
            finish();
        } else {
            // Check for an existing signed-in Contributor
            String providerIdGoogle = sharedPreferences.getString("provider_id_google", null);
            Log.i(getClass().getName(), "providerIdGoogle: " + providerIdGoogle);
            if (TextUtils.isEmpty(providerIdGoogle)) {
                // Redirect to sign-in with Google
                Intent signInWithGoogleIntent = new Intent(getApplicationContext(), SignInWithGoogleActivity.class);
                startActivity(signInWithGoogleIntent);
                finish();
            } else {
                // Redirect to REST API synchronization
                // TODO
            }
        }
    }
}
