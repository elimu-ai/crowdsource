package ai.elimu.crowdsource;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.crowdsource.ui.authentication.SignInActivity;
import ai.elimu.crowdsource.ui.language.SelectLanguageActivity;
import ai.elimu.crowdsource.ui.BottomNavigationActivity;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.model.v2.enums.Language;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // Check if language has been selected
        Language language = SharedPreferencesHelper.getLanguage(getApplicationContext());
        Timber.i("language: " + language);
        if (language == null) {
            // Redirect to language selection
            Intent selectLanguageIntent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
            startActivity(selectLanguageIntent);
            finish();
        } else {
            // Check for an existing signed-in Contributor
            String providerIdGoogle = SharedPreferencesHelper.getProviderIdGoogle(getApplicationContext());
            Timber.i("providerIdGoogle: " + providerIdGoogle);
            if (TextUtils.isEmpty(providerIdGoogle)) {
                // Redirect to sign-in with Google
                Intent signInWithGoogleIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInWithGoogleIntent);
                finish();
            } else {
                // Redirect to crowdsourcing activity selection
                Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
