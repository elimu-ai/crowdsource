package ai.elimu.crowdsource.rest;

import java.util.List;

import ai.elimu.model.v2.gson.content.LetterToAllophoneMappingGson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * REST API: https://github.com/elimu-ai/webapp/blob/master/src/main/java/ai/elimu/rest/v2/crowdsource/LetterToAllophoneMappingsRestController.java
 */
public interface LetterToAllophoneMappingsService {

    /**
     * @param providerIdGoogle The contributor's Google ID.
     */
    @GET("crowdsource/letter-to-allophone-mappings")
    Call<List<LetterToAllophoneMappingGson>> getAll(@Header("providerIdGoogle") String providerIdGoogle);
}
