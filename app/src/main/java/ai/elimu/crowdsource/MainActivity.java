package ai.elimu.crowdsource;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.crowdsource.ui.BottomNavigationActivity;
import ai.elimu.crowdsource.ui.authentication.SignInWithGoogleActivity;
import ai.elimu.crowdsource.ui.authentication.SignInWithWeb3Activity;
import ai.elimu.crowdsource.ui.language.SelectLanguageActivity;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.model.v2.enums.Language;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private Button signInWeb3Button, signInGoogleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        signInWeb3Button = findViewById(R.id.w3_sign_in);
        signInGoogleButton = findViewById(R.id.g_sign_in);
        signInWeb3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInWithWeb3Intent = new Intent(getApplicationContext(), SignInWithWeb3Activity.class);
                startActivity(signInWithWeb3Intent);
            }
        });
        signInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to sign-in with Google
                Intent signInWithGoogleIntent = new Intent(getApplicationContext(), SignInWithGoogleActivity.class);
                startActivity(signInWithGoogleIntent);
            }
        });
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
            String web3Account = SharedPreferencesHelper.getProviderIdWeb3(getApplicationContext());
            Timber.i("providerIdGoogle: " + providerIdGoogle);
            Timber.i("web3 account: " + web3Account);
            if (!TextUtils.isEmpty(providerIdGoogle) || !TextUtils.isEmpty(web3Account)) {
                // Redirect to crowdsourcing activity selection
                Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
