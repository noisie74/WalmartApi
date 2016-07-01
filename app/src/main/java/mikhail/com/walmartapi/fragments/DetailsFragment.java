package mikhail.com.walmartapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import mikhail.com.walmartapi.R;

/**
 * Created by Mikhail on 6/23/16.
 */
public class DetailsFragment extends Fragment {

    protected View v;
    protected Context context;
    private String imageUrl;
    private String productDescription;
    private String price;
    private boolean isInStock;

    @BindView(R.id.item_price)
    public TextView priceTextView;
    @BindView(R.id.description)
    public TextView descriptionTextView;
    @BindView(R.id.instock)
    public TextView inStockTextView;
    @BindView(R.id.image_detail)
    public ImageView itemImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_details, container, false);

        showFragment();

        return v;
    }


    public void showFragment(){
        bindData();
        loadData();
    }

    private void bindData() {
        try {
            ButterKnife.bind(this, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {

        if (price != null) {
            priceTextView.setText("Price " + price);
        }
        if (productDescription != null) {

            String insertDescription = String.valueOf(Html.fromHtml(productDescription.replaceAll("<img.+?>", "")));
            descriptionTextView.setText(insertDescription.replaceAll("[\\uFFFD]", ""));
        }
        if (isInStock) {
            inStockTextView.setText(R.string.in_stock);
        } else {
            inStockTextView.setText(R.string.not_available);
        }
        if (imageUrl != null) {
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(itemImage);
        }
    }

    public void setInStockTextView(boolean isInStock) {
        this.isInStock = isInStock;
    }

    public void setDescriptionTextView(String descriptionText) {
        this.productDescription = descriptionText;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
