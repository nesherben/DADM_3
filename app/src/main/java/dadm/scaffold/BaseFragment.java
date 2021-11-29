package dadm.scaffold;

import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {
    public boolean onBackPressed() {
        return false;
    }

    public ScaffoldActivity getScaffoldActivity() {
        return (ScaffoldActivity) getActivity();
    }
}
