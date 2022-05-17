package ai.elimu.crowdsource.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import ai.elimu.model.v2.enums.Language;
import timber.log.Timber;

public class SharedPreferencesHelper {

    private static final String SHARED_PREFS = "shared_prefs";

    public static final String PREF_APP_VERSION_CODE = "pref_app_version_code";
    public static final String PREF_LANGUAGE = "pref_language";
    public static final String PREF_PROVIDER_ID_GOOGLE = "pref_provider_id_google";
    public static final String PREF_PROVIDER_ID_WEB3 = "pref_provider_id_web3";
    public static final String PREF_EMAIL = "pref_email";
    public static final String PREF_FIRSTNAME = "pref_firstname";
    public static final String PREF_LASTNAME = "pref_lastname";
    public static final String PREF_IMAGE_URL = "pref_image_url";

    public static void clearAllPreferences(Context context) {
        Timber.w("clearAllPreferences");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }


    public static void storeAppVersionCode(Context context, int appVersionCode) {
        Timber.i("storeAppVersionCode");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(PREF_APP_VERSION_CODE, appVersionCode).apply();
    }

    public static int getAppVersionCode(Context context) {
        Timber.i("getAppVersionCode");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PREF_APP_VERSION_CODE, 0);
    }


    public static void storeLanguage(Context context, Language language) {
        Timber.i("storeLanguage");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_LANGUAGE, language.toString()).apply();
    }

    public static Language getLanguage(Context context) {
        Timber.i("getLanguage");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String languageAsString = sharedPreferences.getString(PREF_LANGUAGE, null);
        if (TextUtils.isEmpty(languageAsString)) {
            return null;
        } else {
            Language language = Language.valueOf(languageAsString);
            return language;
        }
    }


    public static void storeProviderIdGoogle(Context context, String providerIdGoogle) {
        Timber.i("storeProviderIdGoogle");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_PROVIDER_ID_GOOGLE, providerIdGoogle).apply();
    }


    public static String getProviderIdGoogle(Context context) {
        Timber.i("getProviderIdGoogle");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String providerIdGoogle = sharedPreferences.getString(PREF_PROVIDER_ID_GOOGLE, null);
        if (TextUtils.isEmpty(providerIdGoogle)) {
            return null;
        } else {
            return providerIdGoogle;
        }
    }

    public static void storeWeb3Account(Context context, String web3Account) {
        Timber.i("storeWeb3Account");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_WEB3_ACCOUNT, web3Account).apply();
    }

    public static String getWeb3Account(Context context) {
        Timber.i("getWeb3Account");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String web3Account = sharedPreferences.getString(PREF_WEB3_ACCOUNT, null);
        if (TextUtils.isEmpty(web3Account)) {
            return null;
        } else {
            return web3Account;
        }
    }


    public static void storeProviderIdWeb3(Context context, String providerIdWeb3) {
        Timber.i("storeProviderIdWeb3");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_PROVIDER_ID_WEB3, providerIdWeb3).apply();
    }

    public static String getProviderIdWeb3(Context context) {
        Timber.i("getProviderIdWeb3");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String providerIdWeb3 = sharedPreferences.getString(PREF_PROVIDER_ID_WEB3, null);
        if (TextUtils.isEmpty(providerIdWeb3)) {
            return null;
        } else {
            return providerIdWeb3;
        }
    }


    public static void storeEmail(Context context, String email) {
        Timber.i("storeProviderIdGoogle");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_EMAIL, email).apply();
    }

    public static String getEmail(Context context) {
        Timber.i("getEmail");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(PREF_EMAIL, null);
        if (TextUtils.isEmpty(email)) {
            return null;
        } else {
            return email;
        }
    }


    public static void storeFirstName(Context context, String firstName) {
        Timber.i("storeProviderIdGoogle");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_FIRSTNAME, firstName).apply();
    }

    public static String getFirstName(Context context) {
        Timber.i("getFirstName");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString(PREF_FIRSTNAME, null);
        if (TextUtils.isEmpty(firstName)) {
            return null;
        } else {
            return firstName;
        }
    }


    public static void storeLastName(Context context, String lastName) {
        Timber.i("storeProviderIdGoogle");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_LASTNAME, lastName).apply();
    }

    public static String getLastName(Context context) {
        Timber.i("getLastName");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String lastName = sharedPreferences.getString(PREF_LASTNAME, null);
        if (TextUtils.isEmpty(lastName)) {
            return null;
        } else {
            return lastName;
        }
    }


    public static void storeImageUrl(Context context, String imageUrl) {
        Timber.i("storeProviderIdGoogle");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREF_IMAGE_URL, imageUrl).apply();
    }

    public static String getImageUrl(Context context) {
        Timber.i("getImageUrl");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString(PREF_IMAGE_URL, null);
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        } else {
            return imageUrl;
        }
    }
}
