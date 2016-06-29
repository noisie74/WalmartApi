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
import butterknife.ButterKnife;
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
    private String imageUrl;
    private String descriptionText;
    private boolean inStockVar;

    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.instock)
    public TextView inStock;
    @BindView(R.id.image_detail)
    public ImageView image;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_details, container, false);

        bindData(v);
        loadData();

        return v;
    }


    private void bindData(View v){
        try {
            ButterKnife.bind(this, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadData() {

        if (descriptionText != null)
            description.setText((Html.fromHtml(descriptionText.replaceAll("<img.+?>", ""))));
        if (inStockVar)
            inStock.setText(R.string.in_stock);
        if (imageUrl != null)
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
    }

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
