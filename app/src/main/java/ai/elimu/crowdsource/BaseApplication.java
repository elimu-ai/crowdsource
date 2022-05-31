package ai.elimu.crowdsource;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import org.jetbrains.annotations.NotNull;
import org.walletconnect.Session;
import org.walletconnect.impls.FileWCSessionStore;
import org.walletconnect.impls.MoshiPayloadAdapter;
import org.walletconnect.impls.OkHttpTransport;
import org.walletconnect.impls.WCSession;
import org.walletconnect.impls.WCSessionStore;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import ai.elimu.crowdsource.server.BridgeServer;
import ai.elimu.crowdsource.util.SharedPreferencesHelper;
import ai.elimu.crowdsource.util.VersionHelper;
import ai.elimu.model.v2.enums.Language;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class BaseApplication extends Application {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static Session.FullyQualifiedConfig config;
    public static Session session;
    private static OkHttpClient client;
    private static Moshi moshi;
    private static BridgeServer bridge;
    private static WCSessionStore storage;

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate();
        this.initMoshi();
        this.initClient();
        this.initBridge();
        this.initSessionStorage();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
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


    private void initClient() {
        OkHttpClient var10000 = (new OkHttpClient.Builder()).build();
        Intrinsics.checkNotNullExpressionValue(var10000, "OkHttpClient.Builder().build()");
        client = var10000;
    }

    private void initMoshi() {
        Moshi var10000 = (new com.squareup.moshi.Moshi.Builder()).add(new KotlinJsonAdapterFactory()).build();
        Intrinsics.checkNotNullExpressionValue(var10000, "Moshi.Builder().build()");
        moshi = var10000;
    }

    private void initBridge() {
        bridge = new BridgeServer(moshi);
        bridge.start();
    }

    private void initSessionStorage() {
        File tmp = new File(this.getCacheDir(), "session_store.json");
        try {
            tmp.createNewFile();
            storage = new FileWCSessionStore(tmp, moshi);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @NotNull
    public static String bytesToHex(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        char[] hexChars = new char[bytes.length * 2];
        int j = 0;

        for (int var4 = bytes.length; j < var4; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 15];
        }

        return new String(hexChars);
    }

    public static void resetSession() throws Exception {
        if (session != null) {
            session.clearCallbacks();
        }
        byte[] randomBytes = new byte[32];
        Random r = new Random();
        r.nextBytes(randomBytes);
        String key = bytesToHex(randomBytes);
        String uuid = UUID.randomUUID().toString();
        config = new Session.FullyQualifiedConfig(uuid, "http://localhost:" + BridgeServer.Companion.getPORT(), key, "wc", 1);
//        config = new Session.FullyQualifiedConfig(uuid, "https://bridge.walletconnect.org", "f70af2060965927b7e709503e71b3cbf", "wc", 1);
        session = new WCSession(
                config,
                new WrappedMoshiPayloadAdapter(new MoshiPayloadAdapter(moshi)),
                storage,
                new WrappedOkHttpTransportBuilder(new OkHttpTransport.Builder(client, moshi)),
                new Session.PeerMeta(
                        "elimu.ai",
                        "Elimu Crowdsource App",
                        "Elimu Crowdsource App",
                        Collections.emptyList()
                ),
                null,
                null
        );
        session.offer();
    }

    static class WrappedMoshiPayloadAdapter implements Session.PayloadAdapter {
        private final MoshiPayloadAdapter wrapped;

        public WrappedMoshiPayloadAdapter(MoshiPayloadAdapter wrapped) {
            this.wrapped = wrapped;
        }

        @NonNull
        @Override
        public Session.MethodCall parse(@NonNull String s, @NonNull String s1) {
            return this.wrapped.parse(s, s1);
        }

        @NonNull
        @Override
        public String prepare(@NonNull Session.MethodCall methodCall, @NonNull String s) {
            return this.wrapped.prepare(methodCall, s);
        }
    }

    static class WrappedOkHttpTransportBuilder implements Session.Transport.Builder {
        private final OkHttpTransport.Builder wrapped;

        public WrappedOkHttpTransportBuilder(OkHttpTransport.Builder wrapped) {
            this.wrapped = wrapped;
        }

        @NonNull
        @Override
        public Session.Transport build(@NonNull String s, @NonNull Function1<? super Session.Transport.Status, Unit> function1, @NonNull Function1<? super Session.Transport.Message, Unit> function11) {
            return this.wrapped.build(s, function1, function11);
        }
    }
}
