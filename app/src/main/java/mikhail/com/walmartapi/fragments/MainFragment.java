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
public class MainFragment extends Fragment {

    public View v;
    protected RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private WalmartObjectAdapter walmartObjectAdapter;
    private List<Products> walmartProducts;
    private Bundle args;
    OnItemClickListener listener;
    LinearLayoutManager linearLayoutManager;
    boolean requestInProgress;
    int itemsToLoad = 0;
    int pageNumber = 1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        setView();
//        if (itemsToLoad == 0){
            walmartApiCall(1,10);
//            itemsToLoad++;
//        }
//        setScrollListener();
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



    public void walmartApiCall(int pageNumber, int items) {

        WalmartAPI.WalmartApiRx apiCall = WalmartAPI.createRx();



        Observable<Response<WalmartObject>> observable =
                apiCall.walmartProducts(ApiKey.apiKey, pageNumber, items);

        observable.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Response<WalmartObject>>() {
                    @Override
                    public void onCompleted() {

                        Log.d("MainActivity", "Call Completed");


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

        walmartProducts.addAll(productsResponse.body().getProducts());
//        walmartObjectAdapter = new WalmartObjectAdapter(walmartProducts);
//        recyclerView.setAdapter(walmartObjectAdapter);
        walmartObjectAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setScrollListener();
    }

    private void setScrollListener(){

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.e("MainFrament","onLoadMore called");
//                itemsToLoad +=10;

//                if (itemsToLoad == 30){
//                    pageNumber += 1;
//                    itemsToLoad = 10;
//                }

                walmartApiCall(pageNumber++,10);
            }
        });
    }

//    private void setBundle(int position) {
//        args = new Bundle();
//        String[] clickedItem = {walmartProducts.get(position).getImage(),
//                walmartProducts.get(position).getLongDescription(),
//                String.valueOf(walmartProducts.get(position).isInStock())};
////        String clickedItem = walmartProducts.get(position).getImage();
//        args.putString("Item", clickedItem);
//    }


    private void productsClickListener() {

        if (walmartObjectAdapter != null) {

            walmartObjectAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {

                    Products p = walmartProducts.get(position);
                    setContributorFragment(p.getImage(), p.getLongDescription(), p.isInStock());
                }
            });
        }

    }

    private void setContributorFragment(String url, String description, boolean inStock) {
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setDescription(description);
        detailsFragment.setImageUrl(url);
        detailsFragment.setInStock(inStock);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frag_container, detailsFragment, "TAG");
        fragmentTransaction.addToBackStack("TAG");
        fragmentTransaction.commit();
//        detailsFragment.setArguments(args);

    }

}
