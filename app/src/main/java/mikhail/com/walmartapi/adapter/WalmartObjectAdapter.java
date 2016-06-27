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
import mikhail.com.walmartapi.interfaces.OnLoadMoreListener;
import mikhail.com.walmartapi.model.Products;

/**
 * Created by Mikhail on 6/21/16.
 */
public class WalmartObjectAdapter extends RecyclerView.Adapter<WalmartObjectAdapter.MyViewHolder> {

    private static final String TAG = "WalmartObjectAdapter";
    private List<Products> walmartProducts;
    public Context context;
    public static OnItemClickListener listener;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    RecyclerView mRecyclerView;

    private Activity mActivity;

    public void setLoaded() {
        isLoading = false;
    }

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
        public View parentView;

        public MyViewHolder(final View view) {
            super(view);
            this.parentView = view;

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

    public WalmartObjectAdapter() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

//
//    public void setProductList(List<Products> walmartProducts) {
//        this.walmartProducts = walmartProducts;
//    }
//
//    public void onRelease() {
//        if (walmartProducts != null) {
//            walmartProducts.clear();
//            walmartProducts = null;
//        }
//        if (mActivity != null) {
//            mActivity = null;
//        }
//    }

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
        if (data.getRating() > 0.0){
            holder.rating.setText("Rating " + String.valueOf(String.format("%.01f",data.getRating())));
        }
        holder.price.setText("Price " + data.getPrice());

//        holder.description.setText(data.getShortDescription());

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
