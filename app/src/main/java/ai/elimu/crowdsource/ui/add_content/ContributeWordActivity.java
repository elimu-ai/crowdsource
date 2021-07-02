package ai.elimu.crowdsource.ui.add_content;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

import ai.elimu.crowdsource.BaseApplication;
import ai.elimu.crowdsource.R;
import ai.elimu.crowdsource.rest.LetterToAllophoneMappingsService;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.model.v2.gson.content.LetterToAllophoneMappingGson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class ContributeWordActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private long timeStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contribute_word);

        progressBar = findViewById(R.id.word_contribution_progress_bar);

        // TODO
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        // Reset UI state
        progressBar.setVisibility(View.VISIBLE);

        timeStart = System.currentTimeMillis();

        // Download a list of Letter-to-Allophone mappings
        BaseApplication baseApplication = (BaseApplication) getApplication();
        Retrofit retrofit = baseApplication.getRetrofit();
        LetterToAllophoneMappingsService letterToAllophoneMappingsService = retrofit.create(LetterToAllophoneMappingsService.class);
        String providerIdGoogle = SharedPreferencesHelper.getProviderIdGoogle(getApplicationContext());
        Call<List<LetterToAllophoneMappingGson>> call = letterToAllophoneMappingsService.getAll(providerIdGoogle);
        Timber.i("call.request(): " + call.request());
        call.enqueue(new Callback<List<LetterToAllophoneMappingGson>>() {
            @Override
            public void onResponse(Call<List<LetterToAllophoneMappingGson>> call, Response<List<LetterToAllophoneMappingGson>> response) {
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
                    List<LetterToAllophoneMappingGson> letterToAllophoneMappingGsons = response.body();
                    Timber.i("letterToAllophoneMappingGsons.size(): " + letterToAllophoneMappingGsons.size());
                    if (letterToAllophoneMappingGsons.size() > 0) {
                        // TODO
                    } else {
                        // No Letter-to-Allophone mappings have been added to the website
                        Snackbar.make(progressBar, "No Letter-to-Allophone mappings have been added to the website", Snackbar.LENGTH_INDEFINITE).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LetterToAllophoneMappingGson>> call, Throwable t) {
                Timber.e(t, "onFailure");

                // Handle error
                Snackbar.make(progressBar, t.getMessage(), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}
