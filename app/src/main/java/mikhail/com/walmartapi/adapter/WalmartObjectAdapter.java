package mikhail.com.walmartapi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import mikhail.com.walmartapi.interfaces.IClickItem;
import mikhail.com.walmartapi.model.Products;

/**
 * Created by Mikhail on 6/21/16.
 */
public class WalmartObjectAdapter extends RecyclerView.Adapter<WalmartObjectAdapter.MyViewHolder> {

    private static final String TAG = "WalmartObjectAdapter";
    private List<Products> walmartProducts;
    public Context context;

    private Activity mActivity;

    private IClickItem mIClickItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_name)
        public TextView name;
        @BindView(R.id.product_rating)
        public TextView rating;
        @BindView(R.id.product_price)
        public TextView price;
        @BindView(R.id.product_desc)
        public TextView description;
        @BindView(R.id.product_img)
        public ImageView image;
        public View parentView;

        public MyViewHolder(View view) {
            super(view);
            this.parentView = view;

            try {
                ButterKnife.bind(this, view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public WalmartObjectAdapter(Activity activity, List<Products> walmartProducts, IClickItem iClickItem) {
        this.walmartProducts = walmartProducts;
        this.mActivity = activity;
        this.mIClickItem = iClickItem;
    }


    public void setProductList(List<Products> walmartProducts) {
        this.walmartProducts = walmartProducts;
    }

    public void onRelease() {
        if (walmartProducts != null) {
            walmartProducts.clear();
            walmartProducts = null;
        }
        if (mActivity != null) {
            mActivity = null;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main, parent, false);

        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Products data = walmartProducts.get(position);
        bindData(data, holder);
    }

    @Override
    public int getItemCount() {
        return walmartProducts.size();
    }

    private void bindData(final Products data, MyViewHolder holder) {
        holder.parentView.setTag(holder);
        holder.name.setText(data.getProductName());
        holder.rating.setText(String.valueOf(data.getRating()));
        holder.price.setText(data.toString());
        holder.description.setText(data.getShortDescription());

        Picasso.with(context)
                .load(data.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }


//    holder.parentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mIClickItem != null) {
//                    mIClickItem.onClick(data);
//                }
//            }
//        });
//
//    }

}
