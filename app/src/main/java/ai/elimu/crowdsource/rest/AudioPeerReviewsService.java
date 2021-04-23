package ai.elimu.crowdsource.rest;

import java.util.List;

import ai.elimu.model.v2.gson.crowdsource.AudioContributionEventGson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * REST API: https://github.com/elimu-ai/webapp/blob/master/src/main/java/ai/elimu/rest/v2/crowdsource/AudioPeerReviewsRestController.java
 */
public interface AudioPeerReviewsService {

    /**
     * @param providerIdGoogle The contributor's Google ID.
     */
    @GET("crowdsource/audio-peer-reviews/words")
    Call<List<AudioContributionEventGson>> listWordRecordingsPendingPeerReview(@Header("providerIdGoogle") String providerIdGoogle);
}
