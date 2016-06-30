package mikhail.com.walmartapi.presenter;

import java.util.ArrayList;
import java.util.List;

import mikhail.com.walmartapi.activity.MainActivity;
import mikhail.com.walmartapi.api.ApiKey;
import mikhail.com.walmartapi.api.WalmartAPI;
import mikhail.com.walmartapi.fragments.MainFragment;
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

/**
 * Created by Mikhail on 6/29/16.
 */
public class GetPresenter {

    WalmartAPI mApi;
    WalmartComponent mApiComponent;
    MainFragment mainFragment;
    MainActivity mView;

    public GetPresenter() {
        mApiComponent = DaggerWalmartComponent.builder()
                .apiModule(new ApiModule())
                .build();
        mApi = mApiComponent.provideWalmartService();
    }

//    public void loadProducts(final boolean isRefresh, int pageNumber, int items) {
//        if (!isRefresh)
//            mView.showProgress(true);
//        mApi.walmartProducts(pageNumber, items)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<Response<WalmartObject>>() {
//                    @Override
//                    public void onCompleted() {
//                        mView.showProgress(false);
//                        if (isRefresh) {
////                            mView.onRefreshDone();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mView.onRequestFail(e.toString());
//                    }
//
//                    @Override
//                    public void onNext(Response<WalmartObject> response) {
//                        mainFragment.callSuccess(response);
//                    }
//                });
//    }


        public void walmartApiCall(int pageNumber, int items) {

        mApi.walmartProducts(pageNumber, items)
        .observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Response<WalmartObject>>() {
                    @Override
                    public void onCompleted() {

                        Timber.d("Call Completed!");


                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d(e.getMessage());


                    }

                    @Override
                    public void onNext(Response<WalmartObject> productsResponse) {

                        List<Products> walmartProducts = new ArrayList<>();
                        walmartProducts.addAll(productsResponse.body().getProducts());

//                        mainFragment.callSuccess(walmartProducts);
                        Timber.d("Call Success!");

                    }
                });
    }

}
