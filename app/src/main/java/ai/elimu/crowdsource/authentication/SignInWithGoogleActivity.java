package ai.elimu.crowdsource.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * See https://developers.google.com/identity/sign-in/android/sign-in
 */
public class SignInWithGoogleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 0;

    private SignInButton signInButton;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_with_google);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        String requestIdToken = "108974530651-3g9r40r5s7it6p9vjh2e2eplgmm1to0d.apps.googleusercontent.com"; // "debug"
        if ("test".equals(BuildConfig.BUILD_TYPE)) {
            requestIdToken = "...";
        } else if ("qa_test".equals(BuildConfig.BUILD_TYPE)) {
            requestIdToken = "...";
        }
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(requestIdToken)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by GoogleSignInOptions.
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i(getClass().getName(), "onClick");

        signIn();
    }

    private void signIn() {
        Log.i(getClass().getName(), "signIn");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(getClass().getName(), "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.i(getClass().getName(), "handleSignInResult");
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(getClass().getName(), "e.getStatusCode(): " + e.getStatusCode());
            String statusCodeString = CommonStatusCodes.getStatusCodeString(e.getStatusCode());
            Log.w(getClass().getName(), "statusCodeString: " + statusCodeString);
            Toast.makeText(getApplicationContext(), statusCodeString, Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.i(getClass().getName(), "updateUI");

        Log.i(getClass().getName(), "account: " + account);
        // TODO
    }
}
