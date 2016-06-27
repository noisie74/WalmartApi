package mikhail.com.walmartapi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.adapter.EndlessRecyclerViewScrollListener;
import mikhail.com.walmartapi.adapter.WalmartObjectAdapter;
import mikhail.com.walmartapi.api.ApiKey;
import mikhail.com.walmartapi.api.WalmartAPI;
import mikhail.com.walmartapi.interfaces.OnItemClickListener;
import mikhail.com.walmartapi.model.Products;
import mikhail.com.walmartapi.model.WalmartObject;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Mikhail on 6/22/16.
 */
public class MainFragmentOld extends Fragment {

    public View v;
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private WalmartObjectAdapter walmartObjectAdapter;
    private List<Products> walmartProducts;
    private Bundle args;
    OnItemClickListener listener;
    LinearLayoutManager linearLayoutManager;
    boolean requestInProgress = true;
    int pageNumber = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        setView();
        if (pageNumber == 0) {
            walmartApiCall();
            pageNumber++;
        }
        setScrollListener();
        productsClickListener();

        return v;
    }

    private void setView() {

        walmartProducts = new ArrayList<>();
        walmartObjectAdapter = new WalmartObjectAdapter(walmartProducts);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(walmartObjectAdapter);
    }


    public void walmartApiCall() {

        WalmartAPI.WalmartApiRx apiCall = WalmartAPI.createRx();

        Observable<Response<WalmartObject>> observable =
                apiCall.getWalmartProducts(ApiKey.apiKey, 1, 10);

        observable.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Response<WalmartObject>>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeContainer.setRefreshing(false);
                        Timber.d(e.getMessage());
                        Log.d("MainActivity", "Call Failed" + e.getMessage());


                    }

                    @Override
                    public void onNext(Response<WalmartObject> productsResponse) {
                        callSuccess(productsResponse);
                        Log.d("MainActivity", "Call Success");

                    }
                });
    }

    /**
     * successful api call
     * returns a list of top repositories
     *
     * @param productsResponse
     */
    private void callSuccess(Response<WalmartObject> productsResponse) {

        walmartProducts = productsResponse.body().getProducts();
        walmartObjectAdapter = new WalmartObjectAdapter(walmartProducts);
        recyclerView.setAdapter(walmartObjectAdapter);
        walmartObjectAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    private void setScrollListener() {

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

//                int curPage = 0;
//                for (int i = curPage; i < 10; i++) {
//                    walmartApiCall(i, 10);
//                }
                walmartApiCall();

            }
        });
    }


    private void setBundle(int position) {
        args = new Bundle();
        String[] clickedItem = {walmartProducts.get(position).getImage(),
                walmartProducts.get(position).getLongDescription(),
                String.valueOf(walmartProducts.get(position).isInStock())};
        args.putStringArray("Item", clickedItem);
    }


    private void productsClickListener() {

        if (walmartObjectAdapter != null) {

            walmartObjectAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {

                    setBundle(position);
                    setContributorFragment(args);
                }
            });
        }

    }

    private void setContributorFragment(Bundle args) {
        DetailsFragmentOld detailsFragment = new DetailsFragmentOld();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frag_container, detailsFragment, "TAG");
        fragmentTransaction.addToBackStack("TAG");
        fragmentTransaction.commit();
        detailsFragment.setArguments(args);

    }

}
