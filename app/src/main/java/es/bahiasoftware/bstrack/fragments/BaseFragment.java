package es.bahiasoftware.bstrack.fragments;

import android.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import es.bahiasoftware.bstrack.activities.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected ProgressBar mProgress;

    protected void loadProgress(boolean enable) {
        if (mProgress != null)
            mProgress.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
