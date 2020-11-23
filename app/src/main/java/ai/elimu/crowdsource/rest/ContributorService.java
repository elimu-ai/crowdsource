package ai.elimu.crowdsource.rest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ContributorService {

    @POST("crowdsource/contributor")
    Call<ResponseBody> createContributor(@Body RequestBody requestBody);
}
