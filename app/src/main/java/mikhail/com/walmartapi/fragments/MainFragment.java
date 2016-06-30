package mikhail.com.walmartapi.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.activity.MainActivity;
import mikhail.com.walmartapi.adapter.EndlessRecyclerViewScrollListener;
import mikhail.com.walmartapi.adapter.WalmartObjectAdapter;
import mikhail.com.walmartapi.api.WalmartAPI;
import mikhail.com.walmartapi.interfaces.OnItemClickListener;
import mikhail.com.walmartapi.model.Products;
import mikhail.com.walmartapi.model.WalmartObject;
import mikhail.com.walmartapi.module.ApiModule;
import mikhail.com.walmartapi.module.DaggerWalmartComponent;
import mikhail.com.walmartapi.module.WalmartComponent;
import mikhail.com.walmartapi.presenter.GetPresenter;
import mikhail.com.walmartapi.util.AppUtils;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static mikhail.com.walmartapi.util.AppUtils.isConnected;

/**
 * Created by Mikhail on 6/22/16.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeContainer;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    private WalmartObjectAdapter walmartObjectAdapter;
    private List<Products> walmartProducts;
    private LinearLayoutManager linearLayoutManager;
    private int pageNumber = 1;
    private int itemsLoaded = 30;
    private int currentClickedPage = 0;
    protected DetailsFragment detailsFragment;
    public View v;
    public final String TAG = "DetailsFrag";
    public WalmartAPI mApi;
    public WalmartComponent mApiComponent;
    Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        context = getContext();

        walmartApiCall(pageNumber, itemsLoaded);
        setView();
        productsClickListener();
        setScrollListener();
        setPullRefresh();

        return v;
    }

    private void setView() {
        try {
            ButterKnife.bind(this, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        initAdapter();
    }

    private void initAdapter() {

        walmartProducts = new ArrayList<>();
        walmartObjectAdapter = new WalmartObjectAdapter(walmartProducts);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(walmartObjectAdapter);
    }


    public void walmartApiCall(int pageNumber, int items) {

        if (!isConnected(this.context)){
            Toast.makeText(getActivity(), "No Network Connection!", Toast.LENGTH_SHORT).show();

        }else {
            mApiComponent = DaggerWalmartComponent.builder()
                    .apiModule(new ApiModule())
                    .build();
            mApi = mApiComponent.provideWalmartService();

            mApi.walmartProducts(pageNumber, items)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<WalmartObject>>() {
                        @Override
                        public void onCompleted() {

                            Timber.d("Call Completed!");
                            if (MainActivity.progressBar.isShown()) {
                                MainActivity.progressBar.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            onRequestFail(e.getMessage());

                        }

                        @Override
                        public void onNext(Response<WalmartObject> productsResponse) {
                            callSuccess(productsResponse);
                            Timber.d("Call Success!");

                        }
                    });
        }


    }

    public void onRequestFail(String error) {
        Timber.d("MainActivity", "Error: " + error);
        Toast.makeText(getActivity(), "Error. Please try again!", Toast.LENGTH_SHORT).show();

    }


    /**
     * successful api call
     * returns a list of top repositories
     *
     * @param productsResponse
     */
    public void callSuccess(Response<WalmartObject> productsResponse) {

        walmartProducts.addAll(productsResponse.body().getProducts());
        walmartObjectAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }


    private void setScrollListener() {

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                walmartApiCall(pageNumber++, itemsLoaded);
                Timber.d("Page loaded: " + String.valueOf(page));
                currentClickedPage++;
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        setCurrentScreenPosition();

    }

    @Override
    public void onResume() {
        super.onResume();
        getPositionOnTheScreen();
    }

    private void getPositionOnTheScreen() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        currentClickedPage = sharedPreferences.getInt("Clicked Item", currentClickedPage);

    }

    private void setCurrentScreenPosition() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Clicked Item", currentClickedPage);
        editor.commit();
    }

    private void productsClickListener() {

        if (walmartObjectAdapter != null) {

            walmartObjectAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {

                    DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager()
                            .findFragmentById(R.id.details_fragment);

                    Products p = walmartProducts.get(position);

                    if (detailsFragment != null){
//                        detailsFragment.showFragment();

                    }
                    setDetailsFragment(p.getImage(), p.getLongDescription(), p.getPrice(), p.isInStock());


//                    setDetailsFragment(p.getImage(), p.getLongDescription(), p.getPrice(), p.isInStock());

//                    if (isTablet(getContext())) {
////                        DetailsFragment detailsFragment = new DetailsFragment();
////                        detailsFragment.showFragment();
//                        setDetailsFragment(p.getImage(), p.getLongDescription(), p.getPrice(), p.isInStock());
//                    }


                }
            });
        }

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private void setDetailsFragment(String imageUrl, String description, String price, boolean inStock) {

        detailsFragment = new DetailsFragment();
        detailsFragment.setDescriptionTextView(description);
        detailsFragment.setImageUrl(imageUrl);
        detailsFragment.setInStockTextView(inStock);
        detailsFragment.setPrice(price);
        initFragment();

    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frag_container, detailsFragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    private void setPullRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isConnected(context)) {
                    Toast.makeText(getActivity(), "No Network Connection!", Toast.LENGTH_LONG).show();
                    swipeContainer.setRefreshing(false);
                } else {
                    walmartApiCall(pageNumber, itemsLoaded);
                }

            }
        });
    }


}
