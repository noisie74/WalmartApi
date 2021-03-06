package mikhail.com.walmartapi.fragments;

import android.content.Context;
import android.os.Bundle;
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
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static mikhail.com.walmartapi.util.AppUtils.isConnected;

/**
 * Main fragment loads when app starts
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
    public DetailsFragment detailsFragment;
    public View v;
    public final String TAG = "DetailsFrag";
    public WalmartAPI mApi;
    public WalmartComponent mApiComponent;
    public Context context;


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


    /**
     * Subscribe to Walmart api Object
     *
     * @param pageNumber
     * @param items
     */

    public void walmartApiCall(int pageNumber, int items) {

        if (!isConnected(this.context)) {
            Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();

        } else {
            mApiComponent = DaggerWalmartComponent.builder()
                    .apiModule(new ApiModule())
                    .build();
            mApi = mApiComponent.provideWalmartService();

            mApi.walmartProducts(pageNumber, items)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<WalmartObject>>() {
                        @Override
                        public void onCompleted() {
                            onRequestComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            onRequestFail(e.getMessage());

                        }

                        @Override
                        public void onNext(Response<WalmartObject> productsResponse) {
                            onRequestSuccess(productsResponse);

                        }
                    });
        }


    }

    public void onRequestFail(String error) {
        Timber.d("MainActivity", "Error: " + error);
        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();

    }

    public void onRequestComplete() {
        Timber.d("Call Completed!");
        if (MainActivity.progressBar.isShown()) {
            MainActivity.progressBar.setVisibility(View.GONE);
        }

    }

    /**
     * successful api call
     * returns a list of walmart products
     *
     * @param productsResponse
     */
    public void onRequestSuccess(Response<WalmartObject> productsResponse) {
        Timber.d("Call Success!");
        walmartProducts.addAll(productsResponse.body().getProducts());
        walmartObjectAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    /**
     * enable lazy loading
     */
    private void setScrollListener() {

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                if (!isConnected(context)) {
                    Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
                }
                walmartApiCall(pageNumber++, itemsLoaded);
                Timber.d("Page loaded: " + String.valueOf(page));
            }
        });
    }

    /**
     * open Details fragment
     */
    private void productsClickListener() {

        if (walmartObjectAdapter != null) {

            walmartObjectAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {

                    Products p = walmartProducts.get(position);
                    setDetailsFragment(p.getImage(), p.getLongDescription(), p.getPrice(), p.isInStock());

                }

            });
        }
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
        fragmentTransaction.add(R.id.frag_container, detailsFragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    private void setPullRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isConnected(context)) {
                    Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_LONG).show();
                    swipeContainer.setRefreshing(false);
                } else {
                    walmartApiCall(pageNumber, itemsLoaded);
                }

            }
        });
    }


}
