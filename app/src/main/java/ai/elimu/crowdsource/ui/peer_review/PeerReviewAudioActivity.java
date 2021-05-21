package ai.elimu.crowdsource.ui.peer_review;

import android.animation.ObjectAnimator;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import ai.elimu.crowdsource.BaseApplication;
import ai.elimu.crowdsource.R;
import ai.elimu.crowdsource.rest.AudioPeerReviewsService;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.model.v2.gson.content.AudioGson;
import ai.elimu.model.v2.gson.crowdsource.AudioContributionEventGson;
import ai.elimu.model.v2.gson.crowdsource.AudioPeerReviewEventGson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class PeerReviewAudioActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private LinearLayout peerReviewContainerLinearLayout;
    private TextView wordLettersTextView;
    private ImageButton playImageButton;

    private LinearLayout peerReviewFormContainerLinearLayout;
    private Button peerReviewApprovedYesButton;
    private Button peerReviewApprovedNoButton;
    private ProgressBar uploadProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peer_review_audio);

        progressBar = findViewById(R.id.audio_peer_review_progress_bar);

        peerReviewContainerLinearLayout = findViewById(R.id.audio_peer_review_container);
        wordLettersTextView = findViewById(R.id.audio_peer_review_word_letters);
        playImageButton = findViewById(R.id.audio_peer_review_play_button);

        peerReviewFormContainerLinearLayout = findViewById(R.id.audio_peer_review_form_container);
        peerReviewApprovedYesButton = findViewById(R.id.audio_peer_review_approved_yes_button);
        peerReviewApprovedNoButton = findViewById(R.id.audio_peer_review_approved_no_button);
        uploadProgressBar = findViewById(R.id.audio_peer_review_upload_progress_bar);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // Reset UI state
        progressBar.setVisibility(View.VISIBLE);
        peerReviewContainerLinearLayout.setVisibility(View.GONE);
        peerReviewFormContainerLinearLayout.setVisibility(View.GONE);


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
        AudioGson audioGson = audioContributionEventGson.getAudio();

        // Initialize word text
        String wordLetters = audioGson.getTranscription();
        wordLettersTextView.setText(wordLetters);

        // Initialize play button
        BaseApplication baseApplication = (BaseApplication) getApplication();
        String audioBytesUrl = baseApplication.getBaseUrl() + audioGson.getBytesUrl();
        Timber.i("audioBytesUrl: " + audioBytesUrl);
        Uri audioUri = Uri.parse(audioBytesUrl);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), audioUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Timber.e(e);
        }
        playImageButton.setOnClickListener(v -> {
            mediaPlayer.start();
        });
        playImageButton.performClick();

        progressBar.setVisibility(View.GONE);
        peerReviewContainerLinearLayout.setVisibility(View.VISIBLE);

        // Initialize peer review form
        mediaPlayer.setOnCompletionListener(mp -> {
            peerReviewFormContainerLinearLayout.setVisibility(View.VISIBLE);

            // Add animation (slide up from the bottom of the screen)
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(peerReviewFormContainerLinearLayout, View.TRANSLATION_Y, 500f, 0);
            objectAnimator.setInterpolator(new OvershootInterpolator());
            objectAnimator.start();

            // When the "Yes" button is pressed, submit the information to the server
            peerReviewApprovedYesButton.setOnClickListener(v -> {
                Timber.i("peerReviewApprovedYesButton onClick");

                AudioPeerReviewEventGson audioPeerReviewEventGson = new AudioPeerReviewEventGson();
                audioPeerReviewEventGson.setAudioContributionEvent(audioContributionEventGson);
                audioPeerReviewEventGson.setApproved(true);
                audioContributionEventGson.setTime(Calendar.getInstance());

                uploadPeerReview(audioPeerReviewEventGson);
            });

            // When the "No" button is pressed, submit the information to the server
            peerReviewApprovedNoButton.setOnClickListener(v -> {
                Timber.i("peerReviewApprovedNoButton onClick");

                AudioPeerReviewEventGson audioPeerReviewEventGson = new AudioPeerReviewEventGson();
                audioPeerReviewEventGson.setAudioContributionEvent(audioContributionEventGson);
                audioPeerReviewEventGson.setApproved(false);
                audioContributionEventGson.setTime(Calendar.getInstance());

                uploadPeerReview(audioPeerReviewEventGson);
            });
        });
    }

    private void uploadPeerReview(AudioPeerReviewEventGson audioPeerReviewEventGson) {
        Timber.i("uploadPeerReview");

        uploadProgressBar.setVisibility(View.VISIBLE);
        peerReviewApprovedYesButton.setVisibility(View.GONE);
        peerReviewApprovedNoButton.setVisibility(View.GONE);

        BaseApplication baseApplication = (BaseApplication) getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        AudioPeerReviewsService audioPeerReviewsService = retrofit.create(AudioPeerReviewsService.class);
        String providerIdGoogle = SharedPreferencesHelper.getProviderIdGoogle(getApplicationContext());
        Call<ResponseBody> call = audioPeerReviewsService.uploadAudioPeerReview(providerIdGoogle, audioPeerReviewEventGson);
        Timber.i("call.request(): " + call.request());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Timber.i("onResponse");

                Timber.i("response: " + response);
                Timber.i("response.isSuccessful(): " + response.isSuccessful());
                try {
                    if (response.isSuccessful()) {
                        String bodyString = response.body().string();
                        Timber.i("bodyString: " + bodyString);

                        // Load the next word recording to be reviewed
                        onStart();
                    } else {
                        String errorBodyString = response.errorBody().string();
                        Timber.e("errorBodyString: " + errorBodyString);
                        Snackbar.make(uploadProgressBar, "Upload failed: " + response.code() + " " + response.message(), Snackbar.LENGTH_LONG).show();
                        uploadProgressBar.setVisibility(View.GONE);
                        peerReviewApprovedYesButton.setVisibility(View.VISIBLE);
                        peerReviewApprovedNoButton.setVisibility(View.VISIBLE);
                    }
                } catch (IOException e) {
                    Timber.e(e);
                    Snackbar.make(uploadProgressBar, "An error occurred: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    uploadProgressBar.setVisibility(View.GONE);
                    peerReviewApprovedYesButton.setVisibility(View.VISIBLE);
                    peerReviewApprovedNoButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e(t, "onFailure");
                Snackbar.make(uploadProgressBar, "An error occurred: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                uploadProgressBar.setVisibility(View.GONE);
                peerReviewApprovedYesButton.setVisibility(View.VISIBLE);
                peerReviewApprovedNoButton.setVisibility(View.VISIBLE);
            }
        });
    }
}
