package ai.elimu.crowdsource.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;

import ai.elimu.crowdsource.BuildConfig;
import ai.elimu.crowdsource.R;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import timber.log.Timber;

/**
 * Prompts the Contributor for access to her Google account. Then stores the account details in the
 * webapp's database.
 *
 * See https://developers.google.com/identity/sign-in/android/sign-in
 */
public class SignInWithGoogleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 0;

    private SignInButton signInButton;

    private ProgressBar signInProgressBar;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_with_google);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        signInProgressBar = findViewById(R.id.sign_in_progressbar);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // If the contributor is already signed in, the GoogleSignInAccount will be non-null.
        GoogleSignInAccount existingGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (existingGoogleSignInAccount != null) {
            updateUI(existingGoogleSignInAccount);
        } else {
            // Configure sign-in to request the contributors's Google ID, basic profile and e-mail address. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(BuildConfig.OAUTH_CLIENT_ID)
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

            signInButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Timber.i("onClick");

        signIn();
    }

    private void signIn() {
        Timber.i("signIn");

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Timber.i("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Timber.i("handleSignInResult");

        try {
            GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(googleSignInAccount);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.w("e.getStatusCode(): " + e.getStatusCode());
            String statusCodeString = CommonStatusCodes.getStatusCodeString(e.getStatusCode());
            Timber.w("statusCodeString: " + statusCodeString);
            Toast.makeText(getApplicationContext(), statusCodeString, Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount googleSignInAccount) {
        Timber.i("updateUI");

        Timber.i("googleSignInAccount: " + googleSignInAccount);
        if (googleSignInAccount != null) {
            // Display the progressbar while connecting to the webapp
            signInButton.setVisibility(View.GONE);
            signInProgressBar.setVisibility(View.VISIBLE);

            // Register the Contributor in the webapp's database
            // TODO

//                // Store the ID in shared preferences
//                String providerIdGoogle = googleSignInAccount.getId();
//                SharedPreferencesHelper.storeProviderIdGoogle(getApplicationContext(), providerIdGoogle);
//
//                // Store the e-mail in shared preferences
//                String email = googleSignInAccount.getEmail();
//                SharedPreferencesHelper.storeEmail(getApplicationContext(), email);
//
//                finish();

        }
    }
}
