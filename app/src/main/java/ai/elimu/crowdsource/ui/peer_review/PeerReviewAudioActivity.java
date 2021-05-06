package ai.elimu.crowdsource.ui.peer_review;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

import ai.elimu.crowdsource.BaseApplication;
import ai.elimu.crowdsource.R;
import ai.elimu.crowdsource.rest.AudioPeerReviewsService;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.model.v2.gson.crowdsource.AudioContributionEventGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class PeerReviewAudioActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private LinearLayout peerReviewContainerLinearLayout;
    private TextView wordLettersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peer_review_audio);

        progressBar = findViewById(R.id.audio_peer_review_progress_bar);

        peerReviewContainerLinearLayout = findViewById(R.id.audio_peer_review_container);
        wordLettersTextView = findViewById(R.id.audio_peer_review_word_letters);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // Reset UI state
        // TODO

        // Download a list of word recordings that the contributor has not yet peer reviewed
        BaseApplication baseApplication = (BaseApplication) getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        AudioPeerReviewsService audioPeerReviewsService = retrofit.create(AudioPeerReviewsService.class);
        String providerIdGoogle = SharedPreferencesHelper.getProviderIdGoogle(getApplicationContext());
        Call<List<AudioContributionEventGson>> call = audioPeerReviewsService.listWordRecordingsPendingPeerReview(providerIdGoogle);
        Timber.i("call.request(): " + call.request());
        call.enqueue(new Callback<List<AudioContributionEventGson>>() {
            @Override
            public void onResponse(Call<List<AudioContributionEventGson>> call, Response<List<AudioContributionEventGson>> response) {
                Timber.i("onResponse");

                Timber.i("response: " + response);
                if (response.code() != 200) {
                    try {
                        Timber.e("response.errorBody().string(): " + response.errorBody().string());
                    } catch (IOException e) {
                        Timber.e(e);
                    }

                    // Handle error
                    Snackbar.make(progressBar, response.code() + ": " + response.message(), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    List<AudioContributionEventGson> audioContributionEventGsons = response.body();
                    Timber.i("audioContributionEventGsons.size(): " + audioContributionEventGsons.size());
                    if (audioContributionEventGsons.size() > 0) {
                        initializeAudioRecordingPeerReview(audioContributionEventGsons);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AudioContributionEventGson>> call, Throwable t) {
                Timber.e(t, "onFailure");

                // Handle error
                Snackbar.make(progressBar, t.getMessage(), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initializeAudioRecordingPeerReview(List<AudioContributionEventGson> audioContributionEventGsons) {
        Timber.i("initializeAudioRecordingPeerReview");

        // Get the first audio recording contribution in the list
        AudioContributionEventGson audioContributionEventGson = audioContributionEventGsons.get(0);

        // Initialize word text
        String wordLetters = audioContributionEventGson.getAudio().getTranscription();
        wordLettersTextView.setText(wordLetters);

        // Initialize play button
        // TODO

        // Initialize peer review form
        // TODO

        progressBar.setVisibility(View.GONE);
        peerReviewContainerLinearLayout.setVisibility(View.VISIBLE);
    }
}
