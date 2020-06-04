package ai.elimu.crowdsource;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ai.elimu.model.enums.Language;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     LanguageListDialogFragment.newInstance(5).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class LanguageListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_LANGUAGE_COUNT = "language_count";

    // TODO: Customize parameters
    public static LanguageListDialogFragment newInstance(int languageCount) {
        final LanguageListDialogFragment fragment = new LanguageListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_LANGUAGE_COUNT, languageCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language_list_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new LanguageAdapter(getArguments().getInt(ARG_LANGUAGE_COUNT)));

        setCancelable(false);
    }

    @Override
    public void onPrimaryNavigationFragmentChanged(boolean isPrimaryNavigationFragment) {
        Log.i(getClass().getName(), "onPrimaryNavigationFragmentChanged");
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment);
    }

    @Override
    public int getTheme() {
        int bottomSheetTheme = R.style.BottomSheetDialogTheme;
        return bottomSheetTheme;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the language layout
            super(inflater.inflate(R.layout.fragment_language_list_dialog_list_dialog_item, parent, false));
            text = itemView.findViewById(R.id.text);
        }
    }

    private class LanguageAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int languageCount;

        LanguageAdapter(int languageCount) {
            this.languageCount = languageCount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Language language = Language.values()[position];
            holder.text.setText(language.getEnglishName() + " (" + language.getNativeName() + ")");
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(getClass().getName(), "onClick");

                    Log.i(getClass().getName(), "language: " + language);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("language", language.toString()).apply();
                    Intent mainActivityIntent = new Intent(getContext(), MainActivity.class);
                    startActivity(mainActivityIntent);
                    getActivity().finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return languageCount;
        }
    }
}
