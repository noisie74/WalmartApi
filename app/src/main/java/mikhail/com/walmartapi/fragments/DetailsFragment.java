package mikhail.com.walmartapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.model.Products;

/**
 * Created by Mikhail on 6/26/16.
 */
public class DetailsFragment extends BaseFragment {

    public static final String EXTRAS_OWNER = "extras_products_list";
    Context context;


    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.instock)
    public TextView inStock;
    @BindView(R.id.image_detail)
    public ImageView image;

    public static DetailsFragment createNewDetailsFragment(Products products) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRAS_OWNER, products);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        Products products = getArguments().getParcelable(EXTRAS_OWNER);
        context = getContext();

        bindData(products);
        return view;
    }


    public void bindData(Products products) {
        if (products == null)
            return;
        description.setText(products.getLongDescription());

        if (products.isInStock()) {
            String productAvailable = "In Stock";
            inStock.setText(productAvailable);
        }


        Picasso.with(context)
                .load(products.getImage())
                .placeholder(R.drawable.placeholder)
                .into(image);
    }
}
