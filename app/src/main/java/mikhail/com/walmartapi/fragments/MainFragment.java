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
import mikhail.com.walmartapi.adapter.WalmartObjectAdapter;
import mikhail.com.walmartapi.api.WalmartAPI;
import mikhail.com.walmartapi.interfaces.IClickItem;
import mikhail.com.walmartapi.model.Products;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mikhail on 6/22/16.
 */
public class MainFragment extends Fragment {

    public View v;
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private Context context;
    private WalmartObjectAdapter walmartObjectAdapter;
    private List<Products> walmartProducts;
    IClickItem iClickItem;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        context = getContext();
        setView();


        return v;
    }

    private void setView() {

        walmartProducts = new ArrayList<>();
        walmartObjectAdapter = new WalmartObjectAdapter(this.getActivity(), walmartProducts, iClickItem);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(walmartObjectAdapter);
    }


    public void setIClickItem(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }

    public void walmartApiCall() {
        WalmartAPI.WalmartApiRx apiCall = WalmartAPI.createRx();

        Observable<Response<Products>> observable =
                apiCall.walmartProducts();

        observable.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Response<Products>>() {
                    @Override
                    public void onCompleted() {



                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeContainer.setRefreshing(false);

                    }

                    @Override
                    public void onNext(Response<Products> productsResponse) {
                        callSuccess(repositories);

                    }
                });
    }

    /**
     * successful api call
     * returns a list of top repositories
     * @param repositories
     */
    private void callSuccess(Response<Products> productsResponse) {

        gitHubData = repositories.body().getItems();
        repositoryAdapter = new RepositoryAdapter(gitHubData);
        recyclerView.setAdapter(repositoryAdapter);
        repositoryAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

}
