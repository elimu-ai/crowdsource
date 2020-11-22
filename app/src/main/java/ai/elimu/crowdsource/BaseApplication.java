package ai.elimu.crowdsource;

import android.app.Application;

import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.crowdsource.util.VersionHelper;
import ai.elimu.model.enums.Language;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        Timber.i("onCreate");
        super.onCreate();

        VersionHelper.updateAppVersion(getApplicationContext());
    }

    public Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getRestUrl() + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * E.g. "http://hin.test.elimu.ai" or "http://hin.elimu.ai"
     */
    public String getBaseUrl() {
        Language language = SharedPreferencesHelper.getLanguage(getApplicationContext());
        String url = "http://" + language.getIsoCode();
        if (!"release".equals(BuildConfig.BUILD_TYPE)) {
            url += ".test";
        }
        url += ".elimu.ai";
        return url;
    }

    public String getRestUrl() {
        return getBaseUrl() + "/rest/v2";
    }
}
