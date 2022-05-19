package ai.elimu.crowdsource.rest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * REST API: https://github.com/elimu-ai/webapp/blob/main/src/main/java/ai/elimu/rest/v2/crowdsource/
 */
public interface ContributorService {

    @POST("crowdsource/contributors")
    Call<ResponseBody> createContributorGoogle(@Body RequestBody requestBody);

    @POST("crowdsource/contributors/web3")
    Call<ResponseBody> createContributorWeb3(@Body RequestBody requestBody);
}
