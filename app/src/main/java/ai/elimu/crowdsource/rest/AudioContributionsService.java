package ai.elimu.crowdsource.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.WordGson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * REST API: https://github.com/elimu-ai/webapp/blob/master/src/main/java/ai/elimu/rest/v2/crowdsource/AudioContributionsRestController.java
 */
public interface AudioContributionsService {

    @GET("crowdsource/audio-contributions")
    Call<List<WordGson>> listWordsPendingAudioContribution(@Header("providerIdGoogle") String providerIdGoogle);
}
