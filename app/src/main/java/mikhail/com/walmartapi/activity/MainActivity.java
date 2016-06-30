package mikhail.com.walmartapi.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import mikhail.com.walmartapi.R;
import mikhail.com.walmartapi.fragments.DetailsFragment;
import mikhail.com.walmartapi.fragments.MainFragment;
import mikhail.com.walmartapi.util.AppUtils;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment();
        showProgress(true);


    }

    private void setFragment() {

        MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, mainFragment);
        fragmentTransaction.commit();

    }


    public void showProgress(boolean isShow) {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (progressBar != null){
            progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);

        }

    }

}
