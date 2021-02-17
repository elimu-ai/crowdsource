package ai.elimu.crowdsource.ui.peer_review;

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

public class PeerReviewFragment extends Fragment {

    private TextView urlTextView;

    private CardView peerReviewAudioCardView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.i("onCreateView");

        View root = inflater.inflate(R.layout.fragment_peer_review, container, false);

        urlTextView = root.findViewById(R.id.url_text_view);
        String baseUrl = ((BaseApplication) getActivity().getApplication()).getBaseUrl();
        urlTextView.setText("Connected to: " + baseUrl);

        peerReviewAudioCardView = root.findViewById(R.id.peer_review_audio_card_view);
        peerReviewAudioCardView.setOnClickListener(v -> {
            Timber.i("peerReviewAudioCardView onClick");
            Intent contributeAudioActivityIntent = new Intent(getActivity().getApplicationContext(), PeerReviewAudioActivity.class);
            startActivity(contributeAudioActivityIntent);
        });

        return root;
    }
}