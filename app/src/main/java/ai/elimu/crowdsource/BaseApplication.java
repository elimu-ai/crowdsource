package ai.elimu.crowdsource;

import android.app.Application;
import android.util.Log;

import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.crowdsource.util.VersionHelper;
import ai.elimu.model.v2.enums.Language;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate();

        // Log config
        Timber.plant(new Timber.DebugTree());
        Timber.i("onCreate");

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
     * E.g. "https://hin.test.elimu.ai" or "https://hin.elimu.ai"
     */
    public String getBaseUrl() {
        Language language = SharedPreferencesHelper.getLanguage(getApplicationContext());
        String url = "https://" + language.getIsoCode();
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
