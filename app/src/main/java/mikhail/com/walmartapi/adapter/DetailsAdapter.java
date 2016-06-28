package mikhail.com.walmartapi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import mikhail.com.walmartapi.model.Products;

/**
 * Created by Mikhail on 6/23/16.
 */
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    private List<Products> products;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.description)
        public TextView description;
        @BindView(R.id.instock)
        public TextView inStock;
        @BindView(R.id.image_detail)
        public ImageView image;



        public ViewHolder(final View view) {
            super(view);


            try {
                ButterKnife.bind(this, view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public DetailsAdapter(List<Products> products) {
        this.products = products;
    }

    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_details, parent, false);

        context = parent.getContext();


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Products data = products.get(position);

        holder.description.setText((Html.fromHtml(data.getLongDescription().replaceAll("<img.+?>", ""))));

        if (data.isInStock()){
            String inStock = "In Stock";
            holder.inStock.setText(inStock);
        }
        Picasso.with(context)
                .load(data.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
