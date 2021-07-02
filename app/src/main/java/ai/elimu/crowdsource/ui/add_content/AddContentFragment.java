package ai.elimu.crowdsource.ui.add_content;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import ai.elimu.crowdsource.BaseApplication;
import ai.elimu.crowdsource.R;
import timber.log.Timber;

public class AddContentFragment extends Fragment {

    private TextView urlTextView;
    
    private CardView contributeWordCardView;

    private CardView contributeAudioCardView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.i("onCreateView");

        View root = inflater.inflate(R.layout.fragment_add_content, container, false);

        urlTextView = root.findViewById(R.id.url_text_view);
        String baseUrl = ((BaseApplication) getActivity().getApplication()).getBaseUrl();
        urlTextView.setText("Connected to: " + baseUrl);

        contributeWordCardView = root.findViewById(R.id.contribute_word_card_view);
        contributeWordCardView.setOnClickListener(v -> {
            Timber.i("contributeWordCardView onClick");
            Intent contributeWordActivityIntent = new Intent(getActivity().getApplicationContext(), ContributeWordActivity.class);
            startActivity(contributeWordActivityIntent);
        });

        contributeAudioCardView = root.findViewById(R.id.contribute_audio_card_view);
        contributeAudioCardView.setOnClickListener(v -> {
            Timber.i("contributeAudioCardView onClick");
            Intent contributeAudioActivityIntent = new Intent(getActivity().getApplicationContext(), ContributeAudioActivity.class);
            startActivity(contributeAudioActivityIntent);
        });

        return root;
    }
}
