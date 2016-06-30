package mikhail.com.walmartapi.api;

import java.security.Key;

import mikhail.com.walmartapi.model.WalmartObject;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Mikhail on 6/21/16.
 */


public interface WalmartAPI {
    @GET("{pageNumber}/{pageSize}")
    Observable<Response<WalmartObject>> walmartProducts(
            @Path("pageNumber") int pageNumber,
            @Path("pageSize") int pageSize);


}

