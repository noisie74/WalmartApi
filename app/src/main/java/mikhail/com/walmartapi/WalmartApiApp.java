package mikhail.com.walmartapi;

import android.app.Application;
import timber.log.Timber;

/**
 * Main application class
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
