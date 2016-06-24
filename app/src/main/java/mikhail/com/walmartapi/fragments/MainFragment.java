package mikhail.com.walmartapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import mikhail.com.walmartapi.activity.MainActivity;
import mikhail.com.walmartapi.adapter.EndlessRecyclerViewScrollListener;
import mikhail.com.walmartapi.adapter.WalmartObjectAdapter;
import mikhail.com.walmartapi.api.ApiKey;
import mikhail.com.walmartapi.api.WalmartAPI;
import mikhail.com.walmartapi.interfaces.IClickItem;
import mikhail.com.walmartapi.model.Products;
import mikhail.com.walmartapi.model.WalmartObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Mikhail on 6/22/16.
 */
public class MainFragment extends Fragment {

    public View v;
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private WalmartObjectAdapter walmartObjectAdapter;
    private List<Products> walmartProducts;
    IClickItem iClickItem;
     LinearLayoutManager linearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        setView();
        walmartApiCall(1,10);
        setScrollListener();

        return v;
    }

    private void setView() {

        walmartProducts = new ArrayList<>();
        walmartObjectAdapter = new WalmartObjectAdapter(walmartProducts, iClickItem);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(walmartObjectAdapter);
    }


    public void setIClickItem(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }

    public void walmartApiCall(int pageNumber, int pageSize) {

        WalmartAPI.WalmartApiRx apiCall = WalmartAPI.createRx();

        Observable<Response<WalmartObject>> observable =
                apiCall.walmartProducts(ApiKey.apiKey, pageNumber, pageSize);

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
                        Log.d("MainActivity", "Call Failed"+ e.getMessage());


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
        walmartObjectAdapter = new WalmartObjectAdapter(walmartProducts, iClickItem);
        recyclerView.setAdapter(walmartObjectAdapter);
        swipeContainer.setRefreshing(false);
    }

    private void setScrollListener(){

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

             walmartApiCall(++page,10);

            }
        });
    }



}
