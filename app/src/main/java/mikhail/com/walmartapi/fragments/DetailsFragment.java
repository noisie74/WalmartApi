package mikhail.com.walmartapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
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
//    private DetailsAdapter detailsAdapter;
//    private List<Products> products;
//    protected RecyclerView recyclerView;
//    private String[] selectedItem;
    private String imageUrl;
    private String descriptionText;
    private boolean inStockVar;

//    @BindView(R.id.description)
    public TextView description;
//    @BindView(R.id.instock)
    public TextView inStock;
//    @BindView(R.id.image_detail)
    public ImageView image;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_details, container, false);

        getClickedItem();
        initRecyclerView(v);
        if(descriptionText!=null)
            description.setText((Html.fromHtml(descriptionText.replaceAll("<img.+?>", ""))));
        if(inStockVar )
            inStock.setText("In Stock");
        if(imageUrl != null)
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
//        walmartApiCall();

        return v;
    }

    private void getClickedItem() {
        Bundle clickedRepository = getArguments();
//        selectedItem = clickedRepository.getStringArray("Item");
    }

    /**
     * initialize recycler view
     * and give adapter empty arrayList
     */
    private void initRecyclerView(View v) {
        description = (TextView) v.findViewById(R.id.description);
        image = (ImageView) v.findViewById(R.id.image_detail);
        inStock = (TextView) v.findViewById(R.id.instock);
//        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        detailsAdapter = new DetailsAdapter(products);

    }


//    public void walmartApiCall() {
//
//        WalmartAPI.WalmartApiRx apiCall = WalmartAPI.createRx();
//
//        Observable<Response<WalmartObject>> observable =
//                apiCall.walmartProducts(ApiKey.apiKey, 1, 20);
//
//        observable.observeOn(AndroidSchedulers.mainThread()).
//                subscribeOn(Schedulers.io()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe(new Subscriber<Response<WalmartObject>>() {
//                    @Override
//                    public void onCompleted() {
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.d(e.getMessage());
//                        Log.d("MainActivity", "Call Failed"+ e.getMessage());
//
//
//                    }
//
//                    @Override
//                    public void onNext(Response<WalmartObject> productsResponse) {
//                        Log.d("MainActivity", "Call Success");
//                        products = productsResponse.body().getProducts();
//                        detailsAdapter = new DetailsAdapter(products);
//                        recyclerView.setAdapter(detailsAdapter);
//
//                    }
//                });
//    }

    public void setInStock(boolean inStock) {
        this.inStockVar = inStock;
    }

    public void setDescription(String description) {
        this.descriptionText = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
