package mikhail.com.walmartapi.api;


import mikhail.com.walmartapi.model.WalmartObject;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Api Service
 */

public interface WalmartAPI {
    @GET("{pageNumber}/{pageSize}")
    Observable<Response<WalmartObject>> walmartProducts(
            @Path("pageNumber") int pageNumber,
            @Path("pageSize") int pageSize);


}

