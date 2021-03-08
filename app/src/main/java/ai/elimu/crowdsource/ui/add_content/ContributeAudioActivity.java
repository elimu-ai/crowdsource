package ai.elimu.crowdsource.ui.add_content;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import ai.elimu.crowdsource.BaseApplication;
import ai.elimu.crowdsource.R;
import ai.elimu.crowdsource.rest.AudioContributionsService;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.model.v2.gson.content.AllophoneGson;
import ai.elimu.model.v2.gson.content.LetterGson;
import ai.elimu.model.v2.gson.content.LetterToAllophoneMappingGson;
import ai.elimu.model.v2.gson.content.WordGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class ContributeAudioActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RECORD_AUDIO = 0;

    private ProgressBar progressBar;

    private LinearLayout recordingContainerLinearLayout;
    private TextView wordLettersTextView;
    private TextView wordAllophonesTextView;
    private ImageButton recordAudioImageButton;
    private ImageButton stopAudioImageButton;
    private LinearLayout playButtonContainer;
    private ImageButton playAudioImageButton;
    private TextView durationTextView;

    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contribute_audio);

        progressBar = findViewById(R.id.audio_contribution_progress_bar);

        recordingContainerLinearLayout = findViewById(R.id.audio_contribution_recording_container);
        wordLettersTextView = findViewById(R.id.audio_contribution_word_letters);
        wordAllophonesTextView = findViewById(R.id.audio_contribution_word_allophones);
        recordAudioImageButton = findViewById(R.id.audio_contribution_record_button);
        stopAudioImageButton = findViewById(R.id.audio_contribution_stop_button);
        playButtonContainer = findViewById(R.id.audio_contribution_play_button_container);
        playAudioImageButton = findViewById(R.id.audio_contribution_play_button);
        durationTextView = findViewById(R.id.audio_contribution_duration_text_view);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // Reset UI state
        progressBar.setVisibility(View.VISIBLE);
        recordingContainerLinearLayout.setVisibility(View.GONE);

        // Download a list of words that the contributor has not yet recorded
        BaseApplication baseApplication = (BaseApplication) getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        AudioContributionsService audioContributionsService = retrofit.create(AudioContributionsService.class);
        String providerIdGoogle = SharedPreferencesHelper.getProviderIdGoogle(getApplicationContext());
        Call<List<WordGson>> call = audioContributionsService.listWordsPendingAudioContribution(providerIdGoogle);
        Timber.i("call.request(): " + call.request());
        call.enqueue(new Callback<List<WordGson>>() {
            @Override
            public void onResponse(Call<List<WordGson>> call, Response<List<WordGson>> response) {
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
                    List<WordGson> wordGsons = response.body();
                    Timber.i("wordGsons.size(): " + wordGsons.size());

                    if (wordGsons.size() > 0) {
                        initializeAudioRecording(wordGsons);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<WordGson>> call, Throwable t) {
                Timber.e(t, "onFailure");

                Timber.e("t.getCause(): " + t.getCause());

                // Handle error
                Snackbar.make(progressBar, t.getCause().toString(), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initializeAudioRecording(List<WordGson> wordGsons) {
        Timber.i("initializeAudioRecording");

        // Get the next word in the list
        WordGson wordGson = wordGsons.get(0);

        String wordLetters = "";
        String wordAllophones = "";
        List<LetterToAllophoneMappingGson> letterToAllophoneMappingGsons = wordGson.getLetterToAllophoneMappings();
        for (LetterToAllophoneMappingGson letterToAllophoneMappingGson : letterToAllophoneMappingGsons) {
            for (LetterGson letterGson : letterToAllophoneMappingGson.getLetters()) {
                wordLetters += letterGson.getText();
            }
            for (AllophoneGson allophoneGson : letterToAllophoneMappingGson.getAllophones()) {
                wordAllophones += allophoneGson.getValueIpa();
            }
        }
        wordLettersTextView.setText(wordLetters);
        wordAllophonesTextView.setText("/" + wordAllophones + "/");

        recordAudioImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("recordAudioImageButton onClick");

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to access the microphone
                    ActivityCompat.requestPermissions(ContributeAudioActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_RECORD_AUDIO);
                } else {
                    // Replace "mic" icon with "stop" icon
                    recordAudioImageButton.setImageResource(R.drawable.ic_round_stop_48);

                    // Display timer
                    // TODO

                    // Record audio from the microphone, and write the recording to a file

                    File audioFile = new File(getApplicationContext().getFilesDir(), "word_" + wordGson.getId() + ".3gp");
                    Timber.i("audioFile: " + audioFile);

                    MediaRecorder mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(audioFile.getPath());
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        Timber.e(e);
                    }
                    mediaRecorder.start();

                    isRecording = true;

                    Thread recordingThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Timber.i("recordingThread run");

                            while (isRecording) {
                                Timber.i("mediaRecorder.getMaxAmplitude(): " + mediaRecorder.getMaxAmplitude());

                                // Indicate contributor's speech volume using pulsating animations

                            }
                        }
                    });
                    recordingThread.start();

                    recordAudioImageButton.setVisibility(View.GONE);
                    stopAudioImageButton.setVisibility(View.VISIBLE);
                    stopAudioImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Timber.i("stopAudioImageButton onClick");

                            mediaRecorder.stop();

                            isRecording = false;

                            // Display a "play" button, and the duration of the recording
                            stopAudioImageButton.setVisibility(View.GONE);
                            playButtonContainer.setVisibility(View.VISIBLE);
                            Uri audioFileUri = Uri.fromFile(audioFile);
                            Timber.i("audioFileUri: " + audioFileUri);

                            // Display the duration of the recording (e.g. "02:500")
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(getApplicationContext(), audioFileUri);
                            String durationMillisecondsAsString = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            Timber.i("durationMillisecondsAsString: " + durationMillisecondsAsString);
                            int durationMilliseconds = Integer.valueOf(durationMillisecondsAsString);
                            int seconds = durationMilliseconds / 1000;
                            int milliseconds = durationMilliseconds % 1000;
                            String duration = "";
                            if (seconds < 10) {
                                duration += "0";
                            }
                            duration += seconds;
                            duration += ":";
                            if (milliseconds < 100) {
                                duration += "0";
                            }
                            if (milliseconds < 10) {
                                duration += "0";
                            }
                            duration += milliseconds;
                            durationTextView.setText(duration);

                            // Play the recorded audio so that the contributor can verify it before uploading
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), audioFileUri);
                            mediaPlayer.start();
                            playAudioImageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Timber.i("playAudioImageButton onClick");

                                    mediaPlayer.start();
                                }
                            });
                        }
                    });
                }
            }
        });

        progressBar.setVisibility(View.GONE);
        recordingContainerLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.i("onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(progressBar, "Permission granted! To record, press the button ☝️", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
