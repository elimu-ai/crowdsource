package ai.elimu.crowdsource;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.model.enums.Language;

public class SelectLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_language);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        LanguageListDialogFragment languageListDialogFragment = LanguageListDialogFragment.newInstance(Language.values().length);
        languageListDialogFragment.show(getSupportFragmentManager(), "dialog");
    }
}
