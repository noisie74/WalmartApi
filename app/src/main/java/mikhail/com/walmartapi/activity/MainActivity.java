package mikhail.com.walmartapi.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.fragments.DetailsFragment;
import mikhail.com.walmartapi.fragments.MainFragment;
import mikhail.com.walmartapi.interfaces.IClickItem;
import mikhail.com.walmartapi.interfaces.ILoadData;
import mikhail.com.walmartapi.model.Products;
import mikhail.com.walmartapi.presenter.GetPresenter;

public class MainActivity extends AppCompatActivity implements IClickItem, ILoadData {

    private static final String BACKSTACK = "MainActivity";
    @BindView(R.id.frag_container)
    FrameLayout mViewContainer;
    @BindView(R.id.progress_bar)
    ProgressBar mLoading;
    private GetPresenter mPresenter;
    Products products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPresenter = new GetPresenter(this);
        mPresenter.loadProducts(false);
        products = new Products();

    }

    public void onRefreshDone() {
        getMainFragment().onRefreshDone();
    }



    @Override
    public void onClick(String name) {
        //control click item
        if (findViewById(R.id.details_container) != null) {

            getDetailsFragment().bindData(products);
        } else {
            FragmentTransaction f = getSupportFragmentManager().beginTransaction();
            f.replace(R.id.frag_container, DetailsFragment.createNewDetailsFragment(products));
            f.addToBackStack(BACKSTACK);
            f.commit();
        }
    }

    @Override
    public void onLoadData() {
        mPresenter.loadProducts(true);
    }



    public void showProgress(boolean isShow) {
        mLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);

    }

    public void onRequestFail(String error) {
        Log.w("MainActivity", "Error: " + error);
        Toast.makeText(this, "Error. Please try again!", Toast.LENGTH_SHORT).show();

    }

    public void onRequestSuccess(List<Products> listProducts) {
        String name = "";
        if (listProducts.size() > 0)
            name = listProducts.get(0).getProductName();

        MainFragment fragment = getMainFragment();
        if (fragment == null) {

            initFragments(listProducts, name);
        } else {
            fragment.onRequestSuccess(listProducts);
            if (findViewById(R.id.details_container) != null) {
                getDetailsFragment().bindData(products);
            }
        }
    }

    private void initFragments(List<Products> listRepo, String name) {
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        MainFragment fragment = MainFragment.createNewDetailsFragment((ArrayList<Products>) listRepo);
        fragment.setIClickItem(this);
        fragment.setILoadData(this);
        f.add(R.id.frag_container, fragment);
        // the fragment_container FrameLayout
        if (findViewById(R.id.details_container) != null) {
            f.add(R.id.details_container, DetailsFragment.createNewDetailsFragment(products));
        }
        f.commit();

    }


    private MainFragment getMainFragment() {
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frag_container);
        return fragment;
    }

    private DetailsFragment getDetailsFragment() {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_container);
        return detailsFragment;
    }
}
