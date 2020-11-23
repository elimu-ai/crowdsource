package ai.elimu.crowdsource.peer_review;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.crowdsource.R;
import timber.log.Timber;

public class PeerReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peer_review);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // TODO
    }
}
