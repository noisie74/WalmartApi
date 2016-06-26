package mikhail.com.walmartapi.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.fragments.MainFragment;
import mikhail.com.walmartapi.interfaces.IClickItem;
import mikhail.com.walmartapi.interfaces.ILoadData;
import mikhail.com.walmartapi.presenter.GetPresenter;

public class MainActivity extends AppCompatActivity implements IClickItem, ILoadData {

    private static final String BACKSTACK = "MainActivity";
    @BindView(R.id.frag_container)
    FrameLayout mViewContainer;
    @BindView(R.id.progress_bar)
    ProgressBar mLoading;
    private GetPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPresenter = new GetPresenter(this);
        mPresenter.loadRepositories(false);

        setFragment();
    }

    public void onRefreshDone() {
        getMainFragment().onRefreshDone();
    }

    private void setFragment(){

        MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container,mainFragment);
        fragmentTransaction.commit();

    }


    public void showProgress(boolean isShow) {
        mLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);

    }
}
