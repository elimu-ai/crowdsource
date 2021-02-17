package ai.elimu.crowdsource.ui.add_content;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.crowdsource.R;
import timber.log.Timber;

public class ContributeAudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contribute_audio);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // TODO
    }
}
