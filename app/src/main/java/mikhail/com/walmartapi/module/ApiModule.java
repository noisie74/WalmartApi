package mikhail.com.walmartapi.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mikhail.com.walmartapi.api.ApiKey;
import mikhail.com.walmartapi.api.WalmartAPI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mikhail on 6/29/16.
 */


@Module
public class ApiModule {

    public static final String API_URL = "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/walmartproducts/"
            + ApiKey.apiKey ;

    @Provides
    @Singleton
    OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    WalmartAPI provideApi(Retrofit retrofit) {
        return retrofit.create(WalmartAPI.class);
    }
}
