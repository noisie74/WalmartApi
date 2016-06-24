package mikhail.com.walmartapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.adapter.DetailsAdapter;
import mikhail.com.walmartapi.model.Products;

/**
 * Created by Mikhail on 6/23/16.
 */
public class ContributorFragment extends Fragment {

    protected View v;
    protected Context context;
    private DetailsAdapter detailsAdapter;
    private List<Products> products;
    protected RecyclerView recyclerView;
    private String[] selectedRepository;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        return v;
    }

    private void getClickedItem() {
        Bundle clickedRepository = getArguments();
        selectedRepository = clickedRepository.getStringArray();
    }

    /**
     * initialize recycler view
     * and give adapter empty arrayList
     */
    private void initRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new DetailsAdapter(products);

    }
}
