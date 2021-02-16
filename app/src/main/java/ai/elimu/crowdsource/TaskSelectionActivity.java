package ai.elimu.crowdsource;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ai.elimu.crowdsource.contribution.ContributeAudioActivity;
import timber.log.Timber;

public class TaskSelectionActivity extends AppCompatActivity {

    private TextView urlTextView;

    private CardView contributeAudioCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_selection);

        urlTextView = findViewById(R.id.url_text_view);
        String baseUrl = ((BaseApplication) getApplication()).getBaseUrl();
        urlTextView.setText("Connected to: " + baseUrl);

        contributeAudioCardView = findViewById(R.id.contribute_audio_card_view);
        contributeAudioCardView.setOnClickListener(v -> {
            Timber.i("contributeAudioCardView onClick");
            Intent contributeAudioActivityIntent = new Intent(getApplicationContext(), ContributeAudioActivity.class);
            startActivity(contributeAudioActivityIntent);
        });
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // TODO
    }
}
