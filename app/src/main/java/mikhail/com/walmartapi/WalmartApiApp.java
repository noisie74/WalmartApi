package mikhail.com.walmartapi;

import android.app.Application;
import timber.log.Timber;

/**
 * Created by Mikhail on 6/21/16.
 */
public class WalmartApiApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

}
