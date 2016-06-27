package mikhail.com.walmartapi.presenter;

import dagger.Provides;
import mikhail.com.walmartapi.activity.MainActivity;
import mikhail.com.walmartapi.api.ApiKey;
import mikhail.com.walmartapi.api.WalmartAPI;
import mikhail.com.walmartapi.model.WalmartObject;
import mikhail.com.walmartapi.module.ApiModule;
import mikhail.com.walmartapi.module.MessageEvent;
import mikhail.com.walmartapi.module.DaggerWalmartComponent;
import mikhail.com.walmartapi.module.WalmartComponent;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mikhail on 6/25/16.
 */
public class GetPresenter {
    WalmartAPI mApi;
    WalmartComponent mApiComponent;
    MainActivity mView;

    public GetPresenter(MainActivity view) {
        this.mView = view;
        mApiComponent = DaggerWalmartComponent.builder()
                .apiModule(new ApiModule())
                .build();
        mApi = mApiComponent.provideWalmartService();
    }

    public void loadProducts(final boolean isRefresh) {
        if (!isRefresh)
            mView.showProgress(true);
        mApi.getWalmartProducts(ApiKey.apiKey, 1, 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<WalmartObject>>() {
                    @Override
                    public void onCompleted() {
                        mView.showProgress(false);
                        if (isRefresh) {
                            mView.onRefreshDone();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onRequestFail(e.toString());
                    }

                    @Override
                    public void onNext(Response<WalmartObject> response) {
                        mView.onRequestSuccess(response.body().getProducts());
                    }
                });
    }
}
