package mikhail.com.walmartapi.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.adapter.EndlessRecyclerViewScrollListener;
import mikhail.com.walmartapi.adapter.WalmartObjectAdapter;
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

    private SwipeRefreshLayout swipeContainer;
    private WalmartObjectAdapter walmartObjectAdapter;
    private List<Products> walmartProducts;
    private LinearLayoutManager linearLayoutManager;
    private int pageNumber = 1;
    private int currentClickedPage = 0;
    protected RecyclerView recyclerView;
    protected DetailsFragment detailsFragment;
    public View v;
    public final String TAG = "DetailsFrag";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        setView();
        walmartApiCall(1, 10);
        productsClickListener();
        setScrollListener();

        return v;
    }

    private void setView() {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
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

        WalmartAPI.WalmartApiRx apiCall = WalmartAPI.createRx();


        Observable<Response<WalmartObject>> observable =
                apiCall.walmartProducts(pageNumber, items);

        observable.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Response<WalmartObject>>() {
                    @Override
                    public void onCompleted() {

                        Timber.d("Call Completed!");


                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeContainer.setRefreshing(false);
                        Timber.d(e.getMessage());


                    }

                    @Override
                    public void onNext(Response<WalmartObject> productsResponse) {
                        callSuccess(productsResponse);
                        Timber.d("Call Success!");

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
        walmartObjectAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }


    private void setScrollListener() {

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                walmartApiCall(pageNumber++, 10);
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

    private void initFragment(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frag_container, detailsFragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

}
