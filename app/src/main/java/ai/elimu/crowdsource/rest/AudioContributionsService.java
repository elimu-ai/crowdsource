package ai.elimu.crowdsource.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.WordGson;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * REST API: https://github.com/elimu-ai/webapp/blob/master/src/main/java/ai/elimu/rest/v2/crowdsource/AudioContributionsRestController.java
 */
public interface AudioContributionsService {

    /**
     * @param providerIdGoogle The contributor's Google ID.
     */
    @GET("crowdsource/audio-contributions/words")
    Call<List<WordGson>> listWordsPendingRecording(@Header("providerIdGoogle") String providerIdGoogle);

    /**
     * @param providerIdGoogle The contributor's Google ID.
     * @param timeSpentMs Time spent in the app while creating the word's audio recording.
     * @param part The recorded audio file.
     */
    @Multipart
    @POST("crowdsource/audio-contributions/words")
    Call<ResponseBody> uploadWordRecording(@Header("providerIdGoogle") String providerIdGoogle, @Header("timeSpentMs") Long timeSpentMs, @Part MultipartBody.Part part);
}
