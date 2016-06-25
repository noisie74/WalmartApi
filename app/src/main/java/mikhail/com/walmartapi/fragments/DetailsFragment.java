package mikhail.com.walmartapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.adapter.DetailsAdapter;
import mikhail.com.walmartapi.adapter.WalmartObjectAdapter;
import mikhail.com.walmartapi.api.ApiKey;
import mikhail.com.walmartapi.api.WalmartAPI;
import mikhail.com.walmartapi.model.Products;
import mikhail.com.walmartapi.model.WalmartObject;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Mikhail on 6/23/16.
 */
public class DetailsFragment extends Fragment {

    protected View v;
    protected Context context;
    private DetailsAdapter detailsAdapter;
    private List<Products> products;
    protected RecyclerView recyclerView;
    private String[] selectedItem;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        getClickedItem();
        initRecyclerView(v);
//        walmartApiCall();

        return v;
    }

    private void getClickedItem() {
        Bundle clickedRepository = getArguments();
        selectedItem = clickedRepository.getStringArray("Item");
    }

    /**
     * initialize recycler view
     * and give adapter empty arrayList
     */
    private void initRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new DetailsAdapter(products);

    }


    public void walmartApiCall() {

        WalmartAPI.WalmartApiRx apiCall = WalmartAPI.createRx();

        Observable<Response<WalmartObject>> observable =
                apiCall.walmartProducts(ApiKey.apiKey, 1, 20);

        observable.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Response<WalmartObject>>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d(e.getMessage());
                        Log.d("MainActivity", "Call Failed"+ e.getMessage());


                    }

                    @Override
                    public void onNext(Response<WalmartObject> productsResponse) {
                        Log.d("MainActivity", "Call Success");
                        products = productsResponse.body().getProducts();
                        detailsAdapter = new DetailsAdapter(products);
                        recyclerView.setAdapter(detailsAdapter);

                    }
                });
    }
}
