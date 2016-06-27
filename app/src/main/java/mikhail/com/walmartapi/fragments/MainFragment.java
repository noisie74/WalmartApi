package mikhail.com.walmartapi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.adapter.WalmartObjectAdapter;
import mikhail.com.walmartapi.interfaces.IClickItem;
import mikhail.com.walmartapi.interfaces.ILoadData;
import mikhail.com.walmartapi.model.Products;

/**
 * Created by Mikhail on 6/26/16.
 */
public class MainFragment extends BaseFragment {

    private static final String EXTRAS_LIST = "products_list";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    WalmartObjectAdapter mAdapter;

    IClickItem mIClickItem;
    ILoadData mILoadData;
    public static MainFragment createNewDetailsFragment(ArrayList<Products> listProducts){
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRAS_LIST, listProducts);
        fragment.setArguments(bundle);
        return  fragment;
    }

    public void setIClickItem(IClickItem iClickItem){
        this.mIClickItem = iClickItem;
    }

    public void setILoadData(ILoadData iLoadData){
        this.mILoadData = iLoadData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        Bundle bun = getArguments();
        ArrayList<Products> list = bun.getParcelableArrayList(EXTRAS_LIST);
        mAdapter = new WalmartObjectAdapter(this.getActivity(), list, mIClickItem);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        initSwipeLayout();

        getActivity().setTitle(R.string.app_name);
        return view;
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter.onRelease();
            mAdapter = null;
        }

        super.onDestroy();
    }


    public void onRefreshDone(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void onRequestSuccess(List<Products> listProducts) {
        mAdapter.setProductList(listProducts);
        mAdapter.notifyDataSetChanged();
    }

    private void initSwipeLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mILoadData !=null){
                    mILoadData.onLoadData();
                }
            }
        });
    }

}
