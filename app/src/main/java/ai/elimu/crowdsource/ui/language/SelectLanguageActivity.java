package ai.elimu.crowdsource.ui.language;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.crowdsource.R;
import timber.log.Timber;

public class SelectLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_language);
    }

    @Override
    protected void onStart() {
        Timber.i("onStart");
        super.onStart();

        LanguageListDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
    }
}
