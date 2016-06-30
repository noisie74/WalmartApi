package mikhail.com.walmartapi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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
import mikhail.com.walmartapi.interfaces.OnItemClickListener;
import mikhail.com.walmartapi.model.Products;

/**
 * Created by Mikhail on 6/21/16.
 */
public class WalmartObjectAdapter extends RecyclerView.Adapter<WalmartObjectAdapter.MyViewHolder> {

    private List<Products> walmartProducts;
    public Context context;
    public OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_name)
        public TextView name;
        @BindView(R.id.product_rating)
        public TextView rating;
        @BindView(R.id.product_price)
        public TextView price;
        @BindView(R.id.product_img)
        public ImageView image;

        public MyViewHolder(final View view) {
            super(view);

            try {
                ButterKnife.bind(this, view);
            } catch (Exception e) {
                e.printStackTrace();
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(view, getLayoutPosition());
                }
            });
        }
    }

    public WalmartObjectAdapter(List<Products> walmartProducts) {
        this.walmartProducts = walmartProducts;
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
        holder.name.setText(data.getProductName());
        if (data.getRating() > 0.0) {
            holder.rating.setText("Rating " + String.valueOf(String.format("%.01f", data.getRating())));
        }
        holder.price.setText("Price " + data.getPrice());

        Picasso.with(context)
                .load(data.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

    }

}




