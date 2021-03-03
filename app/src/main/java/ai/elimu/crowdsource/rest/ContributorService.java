package ai.elimu.crowdsource.rest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * REST API: https://github.com/elimu-ai/webapp/blob/master/src/main/java/ai/elimu/rest/v2/crowdsource/ContributorsRestController.java
 */
public interface ContributorService {

    @POST("crowdsource/contributor")
    Call<ResponseBody> createContributor(@Body RequestBody requestBody);
}
